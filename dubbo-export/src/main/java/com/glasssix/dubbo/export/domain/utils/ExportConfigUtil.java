package com.glasssix.dubbo.export.domain.utils;

import com.glasssix.dubbo.export.domain.po.ExportConfig;
import com.glasssix.dubbo.export.domain.vo.ExportConfigVO;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据导出配置表
 *
 * @TableName export_config
 */
@Data
@Builder
public class ExportConfigUtil {

    public static ExportConfig toExportConfig(ExportConfigVO exportConfigVO) {
        ExportConfig export_config = new ExportConfig();
        BeanUtils.copyProperties(exportConfigVO, export_config);
        return export_config;
    }

    public static ExportConfigVO toExportConfigVO(ExportConfig exportConfig) {
        ExportConfigVO export_configVO = new ExportConfigVO();
        BeanUtils.copyProperties(exportConfig, export_configVO);
        return export_configVO;
    }

    public static List<ExportConfigVO> toExportConfigVOList(List<ExportConfig> exportConfigList) {
        List<ExportConfigVO> list = new ArrayList();
        for (ExportConfig exportConfig : exportConfigList) {
            list.add(toExportConfigVO(exportConfig));
        }
        return list;
    }

    public static List<ExportConfig> toexportConfigList(List<ExportConfigVO> exportConfigVOList) {
        List<ExportConfig> list = new ArrayList();
        for (ExportConfigVO exportConfigVO : exportConfigVOList) {
            list.add(toExportConfig(exportConfigVO));
        }
        return list;
    }
}
