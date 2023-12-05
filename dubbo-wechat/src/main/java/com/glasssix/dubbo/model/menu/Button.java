package com.glasssix.dubbo.model.menu;

import com.glasssix.dubbo.enumeration.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 菜单 - 基类 </p>
 *
 * @author : wyc
 * @description : 参考微信文档定义：https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html
 */
@Data
@ApiModel(description = "菜单 - 基类")
public class Button {

    @ApiModelProperty(value = "菜单标题，不超过16个字节，子菜单不超过60个字节")
    private String name;
    @ApiModelProperty(value = "菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型")
    private String type;

    @ApiModelProperty(value = "菜单KEY值，用于消息接口推送，不超过128字节")
    private String key;
    private Button[] sub_button;


    @ApiModelProperty(value = "(view、miniprogram类型必须) 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url")
    private String url;
}
