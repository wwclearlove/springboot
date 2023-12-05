package com.glasssix.dubbo.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;

import java.util.Date;

/**
 * OLAP
 *
 * @TableName aggregate1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate1VO implements Serializable {
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonProperty("created_time")
//    @JSONField(name="created_time")
    private LocalDate createdTime;


    /**
     *
     */
    @NotNull(message = "[]不能为空")
//    @JsonProperty("type")
//    @JSONField(name="type")
    private Integer type;


    /**
     *
     */
    @NotNull(message = "[]不能为空")
//    @JsonProperty("area_id")
//    @JSONField(name="area_id")
    private Integer areaId;


    /**
     *
     */
    @NotNull(message = "[]不能为空")
//    @JsonProperty("pv")
//    @JSONField(name="pv")
    private Long pv;


}
