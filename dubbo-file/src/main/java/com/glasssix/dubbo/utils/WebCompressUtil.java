package com.glasssix.dubbo.utils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WebCompressUtil {

    private static final int BUFFER_SIZE = 2048;


    public static List<String> unTar(InputStream in, MinioClient minioClient, String bucketName, String parentDir) {
        List<String> fileNames = new ArrayList<>();
        try {
            TarArchiveInputStream tarIn = new TarArchiveInputStream(in, BUFFER_SIZE);
            TarArchiveEntry entry = null;
            while ((entry = tarIn.getNextTarEntry()) != null) {
                if (entry.isFile()) {
                    minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(parentDir + entry.getName()).stream(tarIn, tarIn.available(), -1).build());
                    fileNames.add(parentDir + entry.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileNames;
    }


    public static List<String> unZip(InputStream in, MinioClient minioClient, String bucketName, String parentDir) {
        List<String> fileNames = new ArrayList<>();
        try {
            // 创建客户端
            ZipArchiveInputStream is = new ZipArchiveInputStream(new BufferedInputStream(in, BUFFER_SIZE));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                if (!entry.isDirectory()) {
                    try {
                        // 使用putObject上传一个文件到存储桶中。
                        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(parentDir + entry.getName()).stream(is, is.available(), -1).build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileNames.add(parentDir + entry.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }


    /**
     * 解压方法
     *
     * @param file 解压文件
     * @return
     * @throws Exception
     */
    public static List<String> unCompress(MultipartFile file, MinioClient minioClient, String bucketName, String parentDir) {
        List<String> ret = new ArrayList<>();
        try {
            String fileName = file.getOriginalFilename();
            String upperName = fileName.toUpperCase();
            if (upperName.endsWith(".ZIP")) {
                ret = unZip(file.getInputStream(), minioClient, bucketName, parentDir);
            } else if (upperName.endsWith(".TAR")) {
                ret = unTar(file.getInputStream(), minioClient, bucketName, parentDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }


}
