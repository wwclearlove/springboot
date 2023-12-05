package com.glasssix.dubbo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.glasssix.dubbo.domain.Aggregate2;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glasssix.dubbo.domain.vo.Aggregate2VO;
import com.glasssix.dubbo.domain.vo.Aggregate2PageVO;
import java.io.InputStream;
import java.util.List;

/**
 * @author Glasssix-LJT
 * @description 针对表【aggregate2(OLAP)】的数据库操作Service
 * @createDate 2022-11-09 16:12:07
 */
public interface Aggregate2Service extends IService<Aggregate2> {

    /**
     *
     * 新增
     *
     * @param aggregate2VO 
     * @return
     */
    boolean save(Aggregate2VO aggregate2VO);

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
     * @param aggregate2VO 
     * @return
     */
    boolean update(Aggregate2VO aggregate2VO);

    /**
     *
     * 根据ID单个查询
     *
     * @param id
     * @return
     */
    Aggregate2VO getById(Long id);

    /**
    *
    * 分页查询
    *
    * @param aggregate2PageVO
    * @return
    */
    IPage<Aggregate2VO> page(Aggregate2PageVO aggregate2PageVO);

    /**
     *
     * 根据分页条件导出
     *
     * @param aggregate2PageVO
     * @param exportKey
     * @return
     */
    void exportAggregate2(Aggregate2PageVO aggregate2PageVO,String exportKey);

    /**
     *
     * 导入
     *
     * @param inputStream
     * @return
     */
    void importAggregate2(InputStream inputStream);

    /**
     *
     * 列表查询
     *
     * @param aggregate2VO 
     * @return
     */
    List<Aggregate2VO> list(Aggregate2VO aggregate2VO);
}