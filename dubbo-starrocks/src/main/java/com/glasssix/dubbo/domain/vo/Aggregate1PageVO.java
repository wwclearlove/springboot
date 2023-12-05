package com.glasssix.dubbo.domain.vo;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.glasssix.dubbo.vo.PageQueryVO;
import com.google.common.collect.Lists;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;

import java.util.Date;

/**
* OLAP
* @TableName aggregate1
*/
@Data
public class Aggregate1PageVO extends PageQueryVO {
    /**
     * 
     */
        @NotNull(message = "[]不能为空")
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
            private Long pv;
    

}
