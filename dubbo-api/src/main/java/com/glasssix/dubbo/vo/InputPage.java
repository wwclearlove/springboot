package com.glasssix.dubbo.vo;

/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-04-24 09:15
 **/

import lombok.Data;

import java.io.Serializable;

/**
 * 分页 排序
 *
 * @author Glasssix
 */
@Data
public class InputPage implements Serializable {

    private static final Integer DEFAULT_OFFSET = 1;
    private static final Integer DEFAULT_LIMIT = 10;

    //页码
    private Integer offset = 1;
    //页面条数
    private Integer limit = 10;
    //排序字段 多个,隔开  1,2,3
    private String sortby;
    //排序方式 多个,隔开  1,2,3
    private String order;
}

 