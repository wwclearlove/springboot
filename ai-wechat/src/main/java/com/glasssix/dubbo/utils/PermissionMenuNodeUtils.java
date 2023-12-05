package com.glasssix.dubbo.utils;


import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 封装菜单特殊结构的工具类
 *
 * @Author: wenjiang@glasssix.com
 * @Date: 2022/12/5 11:41
 * @Describe:
 */
public class PermissionMenuNodeUtils {

	public static Map<Long, MenuCounter> build(List<TreeMenuNode> treeNodes) {
		Map<Long, MenuCounter> map = Maps.newHashMap();
		/**
		 *                                0
		 *                          /           \
		 *                        123           130
		 *                      /    \        /     \
		 *                    124   125      131     132
		 *                  /   \   /  \     /  \    /  \
		 *                 126 127 128 129  133 134 135 136
		 * 只支持 节点路径长度必须一致的情况下才可以
		 * 此Demo可以实现 根据0 获取到[126 127 128 129 133 134 135 136]
		 * 				根据123 获取到[126 127 128 129]
		 * 注：比如 126 127节点没有  此时获取到的0根节点 就会出现 [124 128 129 133 134 135 136]
		 */
		// 按照父级ID分组
		Map<Long, List<TreeMenuNode>> groupByParentIdMap = treeNodes.stream().collect(Collectors.groupingBy(TreeMenuNode::getParentId));
		// 存放 0:对应的所有根节点ID数据
		Set<Long> topToLowerChildIdSet = new HashSet<>();
		//存放选中的节点ID数据
		Set<Long> topSelectedToLowerChildIdSet = new HashSet<>();
		// 取出顶级数据(也就是父级ID为0的数据  当然顶层的父级ID也可以自定义  这里只是演示  所以给了0)
		List<TreeMenuNode> topTreeNodes = groupByParentIdMap.get(0L);
		for (TreeMenuNode node : topTreeNodes) {
			getMinimumChildIdArray(groupByParentIdMap, node.getId(), topToLowerChildIdSet, topSelectedToLowerChildIdSet, map);
		}
		return map;
	}


	/**
	 * 根据父级节点获取最低层次 那一级的节点数据
	 * 1
	 * /   \
	 * 2     3
	 * / \   / \
	 * 4  5  6  7
	 * 上面的树形结构调用此方法  根据1 可以获取到  [4 5 6 7]
	 * 根据3 可以获得到  [6 7]
	 *
	 * @param groupByParentIdMap   所有的元素集合(根据父级ID进行了分组) 分组方法可以使用lambda 如下:
	 *                             Map<String, List<Person>> peopleByCity = personStream.collect(Collectors.groupingBy(Person::getCity));
	 * @param pid                  父级ID
	 * @param topToLowerChildIdSet 存储最深根节点的数据集合
	 */
	public static Set<Long> getMinimumChildIdArray(Map<Long, List<TreeMenuNode>> groupByParentIdMap, Long pid, Set<Long> topToLowerChildIdSet, Set<Long> topSelectedToLowerChildIdSet, Map<Long, MenuCounter> map) {
		// 存放当前pid对应的所有根节点ID数据
		Set<Long> currentPidLowerChildIdSet = new HashSet<>();
		//存放选中的
		Set<Long> currentSelectedPidLowerChildIdSet = new HashSet<>();
		// 获取当前pid下所有的子节点
		List<TreeMenuNode> childTreeNodes = groupByParentIdMap.get(pid);
		if (CollUtil.isEmpty(childTreeNodes)) {
			return null;
		}
		MenuCounter menuCounter = new MenuCounter();
		for (TreeMenuNode treeNode : childTreeNodes) {
			Set<Long> lowerChildIdSet = getMinimumChildIdArray(groupByParentIdMap, treeNode.getId(), currentPidLowerChildIdSet, currentSelectedPidLowerChildIdSet, map);
			if (CollUtil.isEmpty(lowerChildIdSet)) {
				// 如果返回null  表示当前遍历的treeNode节点为最底层的节点
				currentPidLowerChildIdSet.add(treeNode.getId());
				if (treeNode.isSelected()) {
					currentSelectedPidLowerChildIdSet.add(treeNode.getId());
				}
			}
		}
		menuCounter.setTotal(currentPidLowerChildIdSet.size());
		menuCounter.setChildIds(currentPidLowerChildIdSet);
		menuCounter.setSelectTotal(currentSelectedPidLowerChildIdSet.size());
		menuCounter.setSelectChildIds(currentSelectedPidLowerChildIdSet);
		map.put(pid, menuCounter);
		// 把当前获取到的根节点数据 一并保存到上一个节点父级ID集合中
		topToLowerChildIdSet.addAll(currentPidLowerChildIdSet);
		topSelectedToLowerChildIdSet.addAll(currentSelectedPidLowerChildIdSet);
		return currentPidLowerChildIdSet;
	}
}
