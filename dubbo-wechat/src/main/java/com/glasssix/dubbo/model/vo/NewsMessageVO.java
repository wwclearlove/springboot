package com.glasssix.dubbo.model.vo;

import com.glasssix.dubbo.enumeration.EnumResponseMessageType;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p> 图文消息 </p>
 *
 * @author : wyc
 * @description :
 * @date : 2020/1/15 14:04
 */
@Data
@ApiModel(description = "图文消息")
public class NewsMessageVO extends BaseMessageVO {

    private String MsgType = EnumResponseMessageType.NEWS.getType();

    private int ArticleCount = 0;

    private List<Article> Articles;

    @Data
    @ApiModel(description = "图文消息中Article类的定义")
    public static class Article {

        @ApiModelProperty(value = "图文消息标题")
        private String Title;

        @ApiModelProperty(value = "图文消息描述")
        private String Description = "";

        @ApiModelProperty(value = "图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200")
        private String PicUrl = "";

        @ApiModelProperty(value = "点击图文消息跳转链接")
        private String Url = "";

    }

    public void addArticle(Article article) {
        if (Articles == null) {
            Articles = Lists.newLinkedList();
        }
        Articles.add(article);
        ArticleCount++;
    }

}
