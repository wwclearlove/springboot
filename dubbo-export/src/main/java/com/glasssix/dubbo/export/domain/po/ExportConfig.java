package com.glasssix.dubbo.export.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 数据导出配置表
 *
 * @TableName export_config
 */
@TableName(value = "export_config")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportConfig implements Serializable {

    /**
     * redis缓存KEY
     */
    public static final String REDIS_KEY = "export_config:";

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
