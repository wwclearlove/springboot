package com.glasssix.dubbo.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 资源
 *
 * @TableName permission_menu
 */
@Data
public class TreeMenuNode implements Serializable {
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


	private boolean isSelected;
	private int selectCount;

	private int childCount;
	private Set<Long> childIds;
	private Set<Long> selectChildIds;

	private List<TreeMenuNode> childList;
}
