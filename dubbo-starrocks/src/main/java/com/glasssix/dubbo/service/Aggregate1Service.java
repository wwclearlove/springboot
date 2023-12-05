package com.glasssix.dubbo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.glasssix.dubbo.domain.Aggregate1;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glasssix.dubbo.domain.vo.Aggregate1VO;
import com.glasssix.dubbo.domain.vo.Aggregate1PageVO;
import java.io.InputStream;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate1(OLAP)】的数据库操作Service
 * @createDate 2022-11-09 16:12:07
 */
public interface Aggregate1Service extends IService<Aggregate1> {
    /**
     *
     * 新增
     *
     * @param
     * @return
     */
    boolean saveTest();
    /**
     *
     * 新增
     *
     * @param aggregate1VO 
     * @return
     */
    boolean save(Aggregate1VO aggregate1VO);

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
     * @param aggregate1VO 
     * @return
     */
    boolean update(Aggregate1VO aggregate1VO);

    /**
     *
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    Aggregate1VO getById(Long id);

    /**
    *
    * 分页查询
    *
    * @param aggregate1PageVO
    * @return
    */
    IPage<Aggregate1VO> page(Aggregate1PageVO aggregate1PageVO);

    /**
     *
     * 根据分页条件导出
     *
     * @param aggregate1PageVO
     * @param exportKey
     * @return
     */
    void exportAggregate1(Aggregate1PageVO aggregate1PageVO,String exportKey);

    /**
     *
     * 导入
     *
     * @param inputStream
     * @return
     */
    void importAggregate1(InputStream inputStream);

    /**
     *
     * 列表查询
     *
     * @param aggregate1VO 
     * @return
     */
    List<Aggregate1VO> list(Aggregate1VO aggregate1VO);
}