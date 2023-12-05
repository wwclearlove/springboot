package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.domain.Organization;
import com.glasssix.dubbo.service.OrganizationService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【organization】的数据库操作Controller
 * @createDate 2023-01-30 10:15:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class OrganizationController {

    //针对表【(organization)】的数据库操作Service

    private final OrganizationService organizationService;


    /**
     * 列表查询
     *
     * @param organizationVO
     * @return
     */
    @GetMapping("/organizations")
    public List<Organization> list(Organization organizationVO) {
        return organizationService.list(organizationVO);
    }

}
