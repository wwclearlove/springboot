package com.glasssix.dubbo.model.dto;

import com.glasssix.dubbo.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 文本消息 </p>
 *
 * @author : wyc
 * @description :
 * @date : 2020/1/15 14:00
 */
@Data
@ApiModel(description = "文本消息")
public class TextMessageDTO extends BaseMessageDTO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumRequestMessageType.TEXT.getType();

    @ApiModelProperty(value = "文本消息内容")
    private String Content;

}
