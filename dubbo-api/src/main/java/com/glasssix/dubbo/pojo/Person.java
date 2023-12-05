package com.glasssix.dubbo.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 执勤人员信息表
 * @TableName person
 */
@TableName(value ="person")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
    /**
     * 人员ID
     */
    @TableId
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 电话
     */
    @TableField("tel")
    private String tel;

    /**
     * 证件号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 照片
     */
    @TableField("photo_url")
    private String photoUrl;

    /**
     * 性别 1/2 男/女
     */
    @TableField("gender")
    private Integer gender;



    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("modify_time")
    private Date modifyTime;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}