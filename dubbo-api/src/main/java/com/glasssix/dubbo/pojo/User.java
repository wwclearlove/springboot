package com.glasssix.dubbo.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     *
     */
    @TableField("name")
    private String name;

    /**
     *
     */
    @TableField("role")
    private String role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}