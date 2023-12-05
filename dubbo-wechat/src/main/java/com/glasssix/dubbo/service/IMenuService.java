package com.glasssix.dubbo.service;


import com.glasssix.dubbo.model.AccessTokenVO;
import com.glasssix.dubbo.model.WeixinResponseResult;
import com.glasssix.dubbo.model.menu.Menu;

/**
 * <p> 菜单 - 服务类 </p>
 *
 * @author : wyc
 * @description :
 */
public interface IMenuService {

    /**
     * 查询菜单
     *
     * @return: java.lang.Object
     */
    Object getMenu( );

    /**
     * 删除菜单
     *
     * @return: com.wyc.demo.modules.weixin.model.WeixinResponseResult
     */
    WeixinResponseResult deleteMenu( );

    /**
     * 创建菜单
     *
     * @param menu        : 创建的菜单数据
     * @return: com.wyc.demo.modules.weixin.model.WeixinResponseResult
     */
    WeixinResponseResult createMenu(Menu menu);
    String getAccessToken();

}
