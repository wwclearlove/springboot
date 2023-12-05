package com.glasssix.dubbo.domain;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.collect.Lists;
import lombok.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Date;

/**
* OLAP
* @TableName aggregate2
*/
@TableName(value ="aggregate2")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate2 implements Serializable {

/**
* redis缓存KEY
*/
public static final String REDIS_KEY = "aggregate2:";

    /**
    * 
    */
    @NotNull(message = "[]不能为空")
    @TableField(fill = FieldFill.DEFAULT, value = "created_time")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate createTime;
    

    /**
    * 
    */
        @NotNull(message = "[]不能为空")


            private Integer type;
    

    /**
    * 
    */
        @NotNull(message = "[]不能为空")


            private Integer areaId;
    

    /**
    * 
    */
        @NotNull(message = "[]不能为空")


            private Integer deviceType;
    

    /**
    * 
    */
        @NotNull(message = "[]不能为空")


            private Long pv;
    

}
