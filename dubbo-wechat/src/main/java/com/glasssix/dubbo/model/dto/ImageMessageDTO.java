package com.glasssix.dubbo.model.dto;

import com.glasssix.dubbo.enumeration.EnumRequestMessageType;
import lombok.Data;

/**
 * <p> 图片消息 </p>
 *
 * @author : wyc
 * @description :
 * @date : 2020/1/15 14:00
 */
@Data
public class ImageMessageDTO extends BaseMessageDTO {

    private String MsgType = EnumRequestMessageType.IMAGE.getType();

    private String PicUrl;

    private String MediaId;

}
