package com.glasssix.dubbo.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-05-15 11:45
 **/
@Data
@Builder
public class FileUploadVO implements Serializable {

    private String absolutePath;
    private String fileUrl;

}

 