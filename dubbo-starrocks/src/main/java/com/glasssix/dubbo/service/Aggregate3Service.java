package com.glasssix.dubbo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.glasssix.dubbo.domain.Aggregate3;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glasssix.dubbo.domain.vo.Aggregate3VO;
import com.glasssix.dubbo.domain.vo.Aggregate3PageVO;
import java.io.InputStream;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate3(OLAP)】的数据库操作Service
 * @createDate 2022-11-09 16:12:07
 */
public interface Aggregate3Service extends IService<Aggregate3> {

    /**
     *
     * 新增
     *
     * @param aggregate3VO 
     * @return
     */
    boolean save(Aggregate3VO aggregate3VO);

    /**
     *
     * 根据ID单个删除
     *
     * @param id
     * @return
     */
    boolean removeById(Long id);

    /**
     *
     * 根据ID批量删除
     *
     * @param idList
     * @return
     */
    boolean removeByIds(List<Long> idList);

    /**
     *
     * 根据ID更新
     *
     * @param aggregate3VO 
     * @return
     */
    boolean update(Aggregate3VO aggregate3VO);

    /**
     *
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    Aggregate3VO getById(Long id);

    /**
    *
    * 分页查询
    *
    * @param aggregate3PageVO
    * @return
    */
    IPage<Aggregate3VO> page(Aggregate3PageVO aggregate3PageVO);

    /**
     *
     * 根据分页条件导出
     *
     * @param aggregate3PageVO
     * @param exportKey
     * @return
     */
    void exportAggregate3(Aggregate3PageVO aggregate3PageVO,String exportKey);

    /**
     *
     * 导入
     *
     * @param inputStream
     * @return
     */
    void importAggregate3(InputStream inputStream);

    /**
     *
     * 列表查询
     *
     * @param aggregate3VO 
     * @return
     */
    List<Aggregate3VO> list(Aggregate3VO aggregate3VO);
}