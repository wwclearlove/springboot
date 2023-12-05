package com.glasssix.dubbo.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 操作日志表
 *
 * @TableName operation_log
 */
@TableName(value = "operation_log")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationLog implements Serializable {

    /**
     * redis缓存KEY
     */
    public static final String REDIS_KEY = "operation_log:";

    /**
     * 主键 id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 主动创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;


    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime operationTime;


    /**
     * 操作 ip
     */

    private String ip;


    /**
     * 用户 id
     */


    private Long userId;


    /**
     * ip归属地
     */


    private String ipHome;


    /**
     * 操作
     */


    private String operation;


    /**
     * 操作系统
     */


    private String os;


    /**
     * 浏览器
     */


    private String browser;


    /**
     * 链接
     */


    private String operationUrl;


    /**
     * 用户名称
     */


    private String username;


}
