package com.glasssix.dubbo.controller;


import com.glasssix.dubbo.model.WeixinResponseResult;
import com.glasssix.dubbo.model.menu.Menu;
import com.glasssix.dubbo.service.IMenuService;
import com.glasssix.dubbo.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> 微信菜单 - 接口 </p>
 *
 * @author : wyc
 * @description :
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Api(tags = "微信菜单 - 接口")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @GetMapping(value = "/getMenu")
    public Result getMenu() {
        Object result = menuService.getMenu();
        return Result.ok(result);
    }

    @GetMapping(value = "/deleteMenu")
    @ApiOperation(value = "删除菜单", httpMethod = "GET", response = Result.class, notes = "删除菜单")
    public Result deleteMenu() {
        WeixinResponseResult result = menuService.deleteMenu();
        // 删除失败处理
        if (result != null && result.getErrcode() != 0) {
            return Result.error(result.getErrmsg());
        }
        return Result.ok(result);
    }

    @PostMapping(value = "/createMenu")
    @ApiOperation(value = "创建菜单", httpMethod = "POST", response = Result.class, notes = "创建菜单")
    public Result createMenu(@RequestBody Menu menu) {
        WeixinResponseResult result = menuService.createMenu(menu);
        // 创建失败处理
        if (result != null && result.getErrcode() != 0) {
            return Result.error(result.getErrmsg());
        }
        return Result.ok(result);
    }

}
