package com.glasssix.dubbo.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 资源
 *
 * @TableName permission_menu
 */
@Data
public class PermissionMenuVO implements Serializable {
	/**
	 * 业务ID
	 */
	private Long id;


	/**
	 * 名称
	 */
	private String name;


	/**
	 * 路由
	 */
	private String url;


	/**
	 * 父级菜单
	 */
	private Long parentId;


	/**
	 * 编辑时间
	 */
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updateTime;


	/**
	 * 创建时间
	 */
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createTime;


	/**
	 * ICON
	 */
	private String icon;


	/**
	 * 菜单/按钮
	 */
	private Integer menuType;


	/**
	 * 排序
	 */
	private Integer sort;


	/**
	 * 功能编码
	 */
	private String code;


	/**
	 * 链接
	 */
	private String optUrl;


	/**
	 * 扩展字段
	 */
	private String ext;


	/**
	 * 请求方式
	 */
	private String method;


	private boolean isSelected;
	private int selectCount;

	private int childCount;
	private Set<Long> childIds;
	private Set<Long> selectChildIds;

	private List<PermissionMenuVO> childList;
}
