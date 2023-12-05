package com.glasssix.dubbo.service;

import com.glasssix.dubbo.domain.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【organization】的数据库操作Service
 * @createDate 2023-01-30 10:15:33
 */
public interface OrganizationService extends IService<Organization> {

    /**
     *
     * 列表查询
     *
     * @param organization
     * @return
     */
    List<Organization> list(Organization organization);
}