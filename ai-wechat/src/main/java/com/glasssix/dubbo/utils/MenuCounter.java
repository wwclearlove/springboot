package com.glasssix.dubbo.utils;

import lombok.Data;

import java.util.Set;

/**
 * 菜单选中计数器
 *
 * @Author: wenjiang@glasssix.com
 * @Date: 2022/12/5 15:06
 * @Describe:
 */
@Data
public class MenuCounter {
    /**
     * 总数
     */
    private int total;
    /**
     * 已选中的数量
     */
    private int selectTotal;
	/**
	 * 总数id
	 */
    private Set<Long> childIds;
	/**
	 * 已选中的数量
	 */
    private Set<Long> selectChildIds;

}
