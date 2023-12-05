package com.glasssix.dubbo.export.domain.vo;

import com.glasssix.dubbo.vo.PageQueryVO;
import lombok.Data;

import javax.validation.constraints.Size;


/**
 * 数据导出配置表
 *
 * @TableName export_config
 */
@Data
public class ExportConfigPageVO extends PageQueryVO {
    /**
     * id
     */
    @Size(max = 100, message = "id不能超过100")
    private String id;


    /**
     * 标题头
     */
    @Size(max = 255, message = "标题头不能超过255")
    private String headers;


    /**
     * 文件名称
     */
    @Size(max = 255, message = "文件名称不能超过255")
    private String filename;


    /**
     * 导出字段
     */
    @Size(max = 255, message = "导出字段不能超过255")
    private String fields;


    /**
     *
     */
    @Size(max = 255, message = "不能超过255")
    private String exportKey;


}
