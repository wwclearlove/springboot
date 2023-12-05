package com.glasssix.dubbo.export.controller;

import com.glasssix.dubbo.annotation.AuthorityAnnotation;
import com.glasssix.dubbo.export.domain.vo.ExportConfigPageVO;
import com.glasssix.dubbo.export.domain.vo.ExportConfigVO;
import com.glasssix.dubbo.export.service.ExportConfigService;
import com.glasssix.dubbo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Glasssix
 * @description 针对表【export_config(数据导出配置表)】的数据库操作Controller
 * @createDate 2022-05-19 14:30:38
 */
@RestController
@RequestMapping("/v1")
public class ExportConfigController {

    //针对表【(export_config)】的数据库操作Service
    @Autowired
    private ExportConfigService exportConfigService;

    /**
     * 新增
     *
     * @param exportConfigVO
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/export-config")
    public Result save(@RequestBody ExportConfigVO exportConfigVO) {
        return Result.check(exportConfigService.save(exportConfigVO));
    }

    /**
     * 根据ID单个删除
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/export-config/{id:\\d+}")
    public Result removeById(@PathVariable("id") Long id) {
        return Result.check(exportConfigService.removeById(id));
    }

    /**
     * 根据ID批量删除
     *
     * @param idList
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/export-config")
    public Result remove(@RequestBody List<Long> idList) {
        return Result.check(exportConfigService.removeByIds(idList));
    }

    /**
     * 根据ID修改
     *
     * @param exportConfigVO
     * @return
     */
    @AuthorityAnnotation
    @PutMapping("/export-config")
    public Result update(@RequestBody ExportConfigVO exportConfigVO) {
        return Result.check(exportConfigService.update(exportConfigVO));
    }

    /**
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/export-config/{id:\\d+}")
    public Result getById(@PathVariable("id") Long id) {
        return Result.ok(exportConfigService.getById(id));
    }

    /**
     * 分页查询
     *
     * @param exportConfigPageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/export-config")
    public Result page(ExportConfigPageVO exportConfigPageVO) {
        return Result.ok(exportConfigService.page(exportConfigPageVO));
    }

    /**
     * 导出
     *
     * @param exportConfigPageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/export-config/export")
    public void exportExportConfig(ExportConfigPageVO exportConfigPageVO, @RequestHeader("export_key") String exportKey) {
        exportConfigService.exportExportConfig(exportConfigPageVO, exportKey);
    }

    /**
     * 导入
     *
     * @param file
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/export-config/import")
    public Result importExportConfig(@RequestParam("file") MultipartFile file) {
        try {
            exportConfigService.importExportConfig(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }

    /**
     * 列表查询
     *
     * @param exportConfigVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/export-configs")
    public Result list(ExportConfigVO exportConfigVO) {
        return Result.ok(exportConfigService.list(exportConfigVO));
    }

}
