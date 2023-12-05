package com.glasssix.dubbo.controller;


import com.glasssix.dubbo.config.MinioConfig;
import com.glasssix.dubbo.core.MinioTemplate;
import com.glasssix.dubbo.entity.OssFile;
import com.glasssix.dubbo.entity.StatusCode;
import com.glasssix.dubbo.utils.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;


@RestController
@RequestMapping(value = "/file")
@Slf4j
@CrossOrigin // 允许跨域
public class MinioFileController {
    private static final String MD5_KEY = "wyc:minio:demo:file:md5List";

    @Autowired
    private MinioTemplate minioTemplate;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    StringRedisTemplate redisTemplate;


    @RequestMapping(value = "/home/upload")
    public ModelAndView homeUpload() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    /**
     * 根据文件大小和文件的md5校验文件是否存在
     * 暂时使用Redis实现，后续需要存入数据库
     * 实现秒传接口
     *
     * @param md5 文件的md5
     * @return 操作是否成功
     */
    @GetMapping(value = "/check")
    public Map<String, Object> checkFileExists(String md5) {
        Map<String, Object> resultMap = new HashMap<>();
        if (ObjectUtils.isEmpty(md5)) {
            resultMap.put("status", StatusCode.PARAM_ERROR.getCode());
            return resultMap;
        }
        // 先从Redis中查询
        String url = (String) redisTemplate.boundHashOps(MD5_KEY).get(md5);

        // 文件不存在
        if (ObjectUtils.isEmpty(url)) {
            resultMap.put("status", StatusCode.NOT_FOUND.getCode());
            return resultMap;
        }

        resultMap.put("status", StatusCode.SUCCESS.getCode());
        resultMap.put("url", url);
        // 文件已经存在了
        return resultMap;
    }


    /**
     * 文件上传，适合大文件，集成了分片上传
     */
    @PostMapping(value = "/upload")
    public Map<String, Object> upload(HttpServletRequest req) {
        Long s = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;

        // 获得文件分片数据
        MultipartFile file = multipartRequest.getFile("data");

        // 上传过程中出现异常，状态码设置为50000
        if (file == null) {
            map.put("status", StatusCode.FAILURE.getCode());
            return map;
        }
        // 分片第几片
        int index = Integer.parseInt(multipartRequest.getParameter("index"));
        // 总片数
        int total = Integer.parseInt(multipartRequest.getParameter("total"));
        // 获取文件名
        String fileName = multipartRequest.getParameter("name");

        String md5 = multipartRequest.getParameter("md5");

        // 创建文件桶
        minioTemplate.makeBucket(md5);
        String objectName = String.valueOf(index);

        log.info("index: {}, total:{}, fileName:{}, md5:{}, objectName:{}", index, total, fileName, md5, objectName);

        // 当不是最后一片时，上传返回的状态码为20001
        if (index < total) {
            try {
                // 上传文件
                OssFile ossFile = minioTemplate.putChunkObject(file.getInputStream(), md5, objectName);
                log.info("{} upload success {}", objectName, ossFile);
                Long s2 = System.currentTimeMillis() - s;
                log.info("index: {},耗时:{}", index, s2);
                log.info("index: {}, total:{}, fileName:{}, md5:{}, objectName:{}", index, total, fileName, md5, objectName);
                // 设置上传分片的状态
                map.put("status", StatusCode.ALONE_CHUNK_UPLOAD_SUCCESS.getCode());
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", StatusCode.FAILURE.getCode());
                return map;
            }
        } else {
            // 为最后一片时状态码为20002
            try {
                // 上传文件
                minioTemplate.putChunkObject(file.getInputStream(), md5, objectName);
                Long s2 = System.currentTimeMillis() - s;
                log.info("index: {},耗时:{}", index, s2);
                // 设置上传分片的状态
                map.put("status", StatusCode.ALL_CHUNK_UPLOAD_SUCCESS.getCode());
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", StatusCode.FAILURE.getCode());
                return map;
            }
        }

    }

    /**
     * 文件合并
     *
     * @param shardCount 分片总数
     * @param fileName   文件名
     * @param md5        文件的md5
     * @param fileType   文件类型
     * @param fileSize   文件大小
     * @return 分片合并的状态
     */
    @GetMapping(value = "/merge")
    public Map<String, Object> merge(Integer shardCount, String fileName, String md5, String fileType,
                                     Long fileSize) {
        Map<String, Object> retMap = new HashMap<>();
        Long s = System.currentTimeMillis();
        try {
            // 查询片数据
            List<String> objectNameList = minioTemplate.listObjectNames(md5);
            if (shardCount != objectNameList.size()) {
                // 失败
                retMap.put("status", StatusCode.FAILURE.getCode());
            } else {
                // 开始合并请求
                String targetBucketName = minioConfig.getBucketName();
                String filenameExtension = StringUtils.getFilenameExtension(fileName);
                String fileNameWithoutExtension = UUID.randomUUID().toString();
                String objectName = fileNameWithoutExtension + "." + filenameExtension;
                minioTemplate.composeObject(md5, targetBucketName, objectName);

                log.info("桶：{} 中的分片文件，已经在桶：{},文件 {} 合并成功", md5, targetBucketName, objectName);

                // 合并成功之后删除对应的临时桶
                minioTemplate.removeBucket(md5, true);
                log.info("删除桶 {} 成功", md5);

//                // 计算文件的md5
//                String fileMd5 = null;
//                try (InputStream inputStream = minioTemplate.getObject(targetBucketName, objectName)) {
//                    fileMd5 = Md5Util.calculateMd5(inputStream);
//                } catch (IOException e) {
//                    log.error("", e);
//                }

                // 计算文件真实的类型
                String type = null;
                List<String> typeList = new ArrayList<>();
                try (InputStream inputStreamCopy = minioTemplate.getObject(targetBucketName, objectName)) {
                    typeList.addAll(FileTypeUtil.getFileRealTypeList(inputStreamCopy, fileName, fileSize));
                } catch (IOException e) {
                    log.error("", e);
                }

                // 并和前台的md5进行对比
//                if (!ObjectUtils.isEmpty(fileMd5) && !ObjectUtils.isEmpty(typeList) && fileMd5.equalsIgnoreCase(md5) && typeList.contains(fileType.toLowerCase(Locale.ENGLISH))) {
                // 表示是同一个文件, 且文件后缀名没有被修改过
                String url = minioTemplate.getPresignedObjectUrl(targetBucketName, objectName);

                // 存入redis中
                redisTemplate.boundHashOps(MD5_KEY).put(md5, url);

                // 成功
                retMap.put("status", StatusCode.SUCCESS.getCode());
//                } else {
//                    log.info("非法的文件信息: 分片数量:{}, 文件名称:{}, 文件fileMd5:{}, 文件真实类型:{}, 文件大小:{}",
//                            shardCount, fileName, fileMd5, typeList, fileSize);
//                    log.info("非法的文件信息: 分片数量:{}, 文件名称:{}, 文件md5:{}, 文件类型:{}, 文件大小:{}",
//                            shardCount, fileName, md5, fileType, fileSize);
//
//                    // 并需要删除对象
//                    minioTemplate.deleteObject(targetBucketName, objectName);
//                    retMap.put("status", StatusCode.FAILURE.getCode());
//                }
            }
            Long s2 = System.currentTimeMillis() - s;
            log.info("耗时:{}", s2);
        } catch (Exception e) {
            log.error("", e);
            // 失败
            retMap.put("status", StatusCode.FAILURE.getCode());
        }
        return retMap;
    }
}

