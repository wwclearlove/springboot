package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.annotation.AuthorityAnnotation;
import com.glasssix.dubbo.utils.Result;
import com.glasssix.dubbo.service.Aggregate2Service;
import com.glasssix.dubbo.domain.vo.Aggregate2VO;
import com.glasssix.dubbo.domain.vo.Aggregate2PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate2(OLAP)】的数据库操作Controller
 * @createDate 2022-11-09 16:12:07
 */
@RestController
@RequestMapping("/v1")
public class Aggregate2Controller {

    //针对表【(aggregate2)】的数据库操作Service
    @Autowired
    private Aggregate2Service aggregate2Service;

    /**
     * 新增
     *
     * @param aggregate2VO
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/aggregate2")
    public Result save(@RequestBody Aggregate2VO aggregate2VO) {
        return Result.check(aggregate2Service.save(aggregate2VO));
    }

    /**
     * 根据ID单个删除
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/aggregate2/{id:\\d+}")
    public Result removeById(@PathVariable("id") Long id) {
        return Result.check(aggregate2Service.removeById(id));
    }

    /**
     * 根据ID批量删除
     *
     * @param idList
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/aggregate2")
    public Result remove(@RequestBody List<Long> idList) {
        return Result.check(aggregate2Service.removeByIds(idList));
    }

    /**
    * 根据ID修改
    *
    * @param aggregate2VO
    * @return
    */
    @AuthorityAnnotation
    @PutMapping("/aggregate2")
    public Result update(@RequestBody Aggregate2VO aggregate2VO) {
        return Result.check(aggregate2Service.update(aggregate2VO));
    }

    /**
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate2/{id:\\d+}")
    public Result getById(@PathVariable("id") Long id) {
        return Result.ok(aggregate2Service.getById(id));
    }

    /**
     * 分页查询
     *
     * @param aggregate2PageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate2")
    public Result page(Aggregate2PageVO aggregate2PageVO) {
        return Result.ok(aggregate2Service.page(aggregate2PageVO));
    }

    /**
     * 导出
     *
     * @param aggregate2PageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate2/export")
    public void exportAggregate2(Aggregate2PageVO aggregate2PageVO, @RequestHeader("export_key") String exportKey) {
        aggregate2Service.exportAggregate2(aggregate2PageVO, exportKey);
    }

    /**
     * 导入
     *
     * @param file
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/aggregate2/import")
    public Result importAggregate2(@RequestParam("file") MultipartFile file) {
        try{
            aggregate2Service.importAggregate2(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }

    /**
     * 列表查询
     *
     * @param  aggregate2VO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate2s")
    public Result list(Aggregate2VO aggregate2VO) {
        return Result.ok(aggregate2Service.list(aggregate2VO));
    }

}
