package com.glasssix.dubbo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 菜单类型 </p>
 *
 * @author : wyc
 * @description :
 */
@Getter
@AllArgsConstructor
public enum MenuType {

    // 点击式菜单
    CLICK("click"),
    // 链接式菜单
    VIEW("view");

    private String type;

}
