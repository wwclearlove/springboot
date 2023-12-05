package com.glasssix.dubbo.model.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 菜单树 </p>
 *
 * @author : wyc
 * @description :
 */
@Data
@ApiModel(description = "菜单树")
public class Menu {

    @ApiModelProperty(value = "一级菜单数组，个数应为1~3个")
    private Button[] button;

}
