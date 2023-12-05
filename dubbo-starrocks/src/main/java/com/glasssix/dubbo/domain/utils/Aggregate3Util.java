package com.glasssix.dubbo.domain.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import java.io.Serializable;
import com.glasssix.dubbo.domain.Aggregate3;
import com.glasssix.dubbo.domain.vo.Aggregate3VO;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
* OLAP
* @TableName aggregate3
*/
@Data
@Builder
public class Aggregate3Util {

    public static Aggregate3 toAggregate3(Aggregate3VO aggregate3VO) {
        Aggregate3 aggregate3 = new Aggregate3();
        if (ObjectUtils.isNotEmpty(aggregate3VO)) {
            BeanUtils.copyProperties(aggregate3VO, aggregate3);
        }
        return aggregate3;
    }

    public static Aggregate3VO toAggregate3VO(Aggregate3 aggregate3) {
        Aggregate3VO aggregate3VO = new Aggregate3VO();
        if (ObjectUtils.isNotEmpty(aggregate3)) {
            BeanUtils.copyProperties(aggregate3, aggregate3VO);
        }
        return aggregate3VO;
    }

    public static List<Aggregate3VO> toAggregate3VOList(List<Aggregate3> aggregate3List) {
        List<Aggregate3VO> list = new ArrayList();
        for (Aggregate3 aggregate3 : aggregate3List) {
            list.add(toAggregate3VO(aggregate3));
        }
        return list;
    }

    public static List<Aggregate3> toaggregate3List(List<Aggregate3VO> aggregate3VOList) {
        List<Aggregate3> list = new ArrayList();
        for (Aggregate3VO aggregate3VO : aggregate3VOList) {
            list.add(toAggregate3(aggregate3VO));
        }
        return list;
    }
}
