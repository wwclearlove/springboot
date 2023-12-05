package com.glasssix.dubbo.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.List;


/**
 * @TableName organization
 */
@TableName(value = "organization")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements Serializable {
    public static final String REDIS_KEY = "organization:";

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String areaid;

    private String parentid;

    private Integer sort;


    private Integer type;


    private String communityid;


    @TableField(exist = false)
    private List<Organization> childList;
}
