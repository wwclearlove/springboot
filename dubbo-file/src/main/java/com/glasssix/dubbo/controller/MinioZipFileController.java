package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.config.MinioConfig;
import com.glasssix.dubbo.utils.UUIDUtils;
import com.glasssix.dubbo.utils.WebCompressUtil;
import com.glasssix.dubbo.utils.Result;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/file")
@Slf4j
@CrossOrigin // 允许跨域
public class MinioZipFileController {

    @Autowired
    MinioClient minioClient;
    @Autowired
    private MinioConfig minioConfig;

    @PostMapping("/upload/zip")
    public Result importPerChildren(@RequestParam("file") MultipartFile file) {
        String randName = UUIDUtils.uuid()+"/";
        String originalFilename = file.getOriginalFilename();
        String objectName = randName + originalFilename;
        try {
            InputStream  in = file.getInputStream();
            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).stream(in, in.available(), -1).contentType(file.getContentType()).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        randName=randName+ originalFilename.substring(0, originalFilename.lastIndexOf("."))+"/";
        List<String> list = WebCompressUtil.unCompress(file, minioClient, minioConfig.getBucketName(), randName);
        list.add(objectName);
        return Result.ok(list);
    }


}
