package com.glasssix.dubbo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glasssix.dubbo.domain.OperationLog;
import com.glasssix.dubbo.domain.Organization;
import com.glasssix.dubbo.service.OrganizationService;
import com.glasssix.dubbo.mapper.OrganizationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Glasssix-LJT
 * @description 针对表【organization】的数据库操作Service实现
 * @createDate 2023-01-30 10:15:33
 */
@Slf4j
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization>
        implements OrganizationService {

    //针对表【organization】的数据库操作Mapper
    @Autowired
    OrganizationMapper organizationMapper;


    @Override
    public List<Organization> list(Organization organizationVO) {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getId()), Organization::getId, organizationVO.getId());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getName()), Organization::getName, organizationVO.getName());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getAreaid()), Organization::getAreaid, organizationVO.getAreaid());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getParentid()), Organization::getParentid, organizationVO.getParentid());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getSort()), Organization::getSort, organizationVO.getSort());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getType()), Organization::getType, organizationVO.getType());
        //
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(organizationVO.getCommunityid()), Organization::getCommunityid, organizationVO.getCommunityid());
        //
//        List<Organization> list = organizationMapper.l(queryWrapper);
        List<Organization> list = new ArrayList<>();
        queryWrapper.select("*");
        long l = System.currentTimeMillis();
        baseMapper.exportData(queryWrapper.lambda(), resultContext -> list.add(resultContext.getResultObject()));
        System.err.println("耗时1："+(System.currentTimeMillis()-l));
        long l1 = System.currentTimeMillis();
//        List<Organization> organizations = buildTree(list);
        Page<Organization> page = new Page(1, 10);
        Page<Organization> organizationPage = baseMapper.listData(page, queryWrapper);
        long l2 = System.currentTimeMillis();
        System.err.println("耗时2："+(l2-l1));
//        List<Organization> treeMap = createTreeValues(list);
        List<Organization> list1 = super.list();
        long l3 = System.currentTimeMillis();
        System.err.println("耗时3："+(l3-l2));
        return list;
    }

    private List<Organization> buildTree(List<Organization> nodes) {
        Map<String, List<Organization>> children = nodes.stream().filter(node -> !node.getParentid().equals("0"))
                .collect(Collectors.groupingBy(node -> node.getParentid()));
        nodes.parallelStream().forEach(node -> {
                    List<Organization> Organizations = children.get(node.getId());
                    node.setChildList(Organizations);
                }
        );
        return nodes.stream().
                filter(node -> node.getParentid().equals("0")).
                collect(Collectors.toList());
    }
    public static List<Organization> createTreeValues(List<Organization> OrganizationList) {
        List<Organization> result = new ArrayList<>();
        for (Organization Organization : OrganizationList) {
            if (Organization.getParentid().equals("0")) {
                /*从根节点开始构树*/
                result.add(getChildrenNode(Organization, OrganizationList));
            }
        }
        return result;
    }

    /*构树递归*/
    private static Organization getChildrenNode(Organization Organization, List<Organization> OrganizationList) {
        for (Organization child : OrganizationList) {
            if (child.getParentid().equals(Organization.getId())) {
                /*表示是孩子结点*/
                if (Organization.getChildList() == null) {
                    Organization.setChildList(new ArrayList<>());
                }
                Organization.getChildList().add(getChildrenNode(child, OrganizationList));
            }
        }
        return Organization;
    }
}