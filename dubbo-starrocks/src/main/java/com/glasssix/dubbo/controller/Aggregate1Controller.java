package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.annotation.AuthorityAnnotation;
import com.glasssix.dubbo.utils.Result;
import com.glasssix.dubbo.service.Aggregate1Service;
import com.glasssix.dubbo.domain.vo.Aggregate1VO;
import com.glasssix.dubbo.domain.vo.Aggregate1PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate1(OLAP)】的数据库操作Controller
 * @createDate 2022-11-09 16:12:07
 */
@RestController
@RequestMapping("/v1")
public class Aggregate1Controller {

    //针对表【(aggregate1)】的数据库操作Service
    @Autowired
    private Aggregate1Service aggregate1Service;

    /**
     * 新增
     *
     * @param aggregate1VO
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/aggregate1")
    public Result save(@RequestBody Aggregate1VO aggregate1VO) {
        return Result.check(aggregate1Service.save(aggregate1VO));
    }
    @AuthorityAnnotation
    @PostMapping("/test")
    public Result test() {
        return Result.check(aggregate1Service.saveTest());
    }
    /**
     * 根据ID单个删除
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/aggregate1/{id:\\d+}")
    public Result removeById(@PathVariable("id") Long id) {
        return Result.check(aggregate1Service.removeById(id));
    }

    /**
     * 根据ID批量删除
     *
     * @param idList
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/aggregate1")
    public Result remove(@RequestBody List<Long> idList) {
        return Result.check(aggregate1Service.removeByIds(idList));
    }

    /**
    * 根据ID修改
    *
    * @param aggregate1VO
    * @return
    */
    @AuthorityAnnotation
    @PutMapping("/aggregate1")
    public Result update(@RequestBody Aggregate1VO aggregate1VO) {
        return Result.check(aggregate1Service.update(aggregate1VO));
    }

    /**
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate1/{id:\\d+}")
    public Result getById(@PathVariable("id") Long id) {
        return Result.ok(aggregate1Service.getById(id));
    }

    /**
     * 分页查询
     *
     * @param aggregate1PageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate1")
    public Result page(Aggregate1PageVO aggregate1PageVO) {
        return Result.ok(aggregate1Service.page(aggregate1PageVO));
    }

    /**
     * 导出
     *
     * @param aggregate1PageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate1/export")
    public void exportAggregate1(Aggregate1PageVO aggregate1PageVO, @RequestHeader("export_key") String exportKey) {
        aggregate1Service.exportAggregate1(aggregate1PageVO, exportKey);
    }

    /**
     * 导入
     *
     * @param file
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/aggregate1/import")
    public Result importAggregate1(@RequestParam("file") MultipartFile file) {
        try{
            aggregate1Service.importAggregate1(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }

    /**
     * 列表查询
     *
     * @param  aggregate1VO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate1s")
    public Result list(Aggregate1VO aggregate1VO) {
        return Result.ok(aggregate1Service.list(aggregate1VO));
    }

}
