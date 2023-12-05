package com.glasssix.dubbo.controller;

import com.glasssix.dubbo.annotation.AuthorityAnnotation;
import com.glasssix.dubbo.utils.Result;
import com.glasssix.dubbo.service.Aggregate3Service;
import com.glasssix.dubbo.domain.vo.Aggregate3VO;
import com.glasssix.dubbo.domain.vo.Aggregate3PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate3(OLAP)】的数据库操作Controller
 * @createDate 2022-11-09 16:12:07
 */
@RestController
@RequestMapping("/v1")
public class Aggregate3Controller {

    //针对表【(aggregate3)】的数据库操作Service
    @Autowired
    private Aggregate3Service aggregate3Service;

    /**
     * 新增
     *
     * @param aggregate3VO
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/aggregate3")
    public Result save(@RequestBody Aggregate3VO aggregate3VO) {
        return Result.check(aggregate3Service.save(aggregate3VO));
    }

    /**
     * 根据ID单个删除
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/aggregate3/{id:\\d+}")
    public Result removeById(@PathVariable("id") Long id) {
        return Result.check(aggregate3Service.removeById(id));
    }

    /**
     * 根据ID批量删除
     *
     * @param idList
     * @return
     */
    @AuthorityAnnotation
    @DeleteMapping("/aggregate3")
    public Result remove(@RequestBody List<Long> idList) {
        return Result.check(aggregate3Service.removeByIds(idList));
    }

    /**
    * 根据ID修改
    *
    * @param aggregate3VO
    * @return
    */
    @AuthorityAnnotation
    @PutMapping("/aggregate3")
    public Result update(@RequestBody Aggregate3VO aggregate3VO) {
        return Result.check(aggregate3Service.update(aggregate3VO));
    }

    /**
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate3/{id:\\d+}")
    public Result getById(@PathVariable("id") Long id) {
        return Result.ok(aggregate3Service.getById(id));
    }

    /**
     * 分页查询
     *
     * @param aggregate3PageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate3")
    public Result page(Aggregate3PageVO aggregate3PageVO) {
        return Result.ok(aggregate3Service.page(aggregate3PageVO));
    }

    /**
     * 导出
     *
     * @param aggregate3PageVO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate3/export")
    public void exportAggregate3(Aggregate3PageVO aggregate3PageVO, @RequestHeader("export_key") String exportKey) {
        aggregate3Service.exportAggregate3(aggregate3PageVO, exportKey);
    }

    /**
     * 导入
     *
     * @param file
     * @return
     */
    @AuthorityAnnotation
    @PostMapping("/aggregate3/import")
    public Result importAggregate3(@RequestParam("file") MultipartFile file) {
        try{
            aggregate3Service.importAggregate3(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }

    /**
     * 列表查询
     *
     * @param  aggregate3VO
     * @return
     */
    @AuthorityAnnotation
    @GetMapping("/aggregate3s")
    public Result list(Aggregate3VO aggregate3VO) {
        return Result.ok(aggregate3Service.list(aggregate3VO));
    }

}
