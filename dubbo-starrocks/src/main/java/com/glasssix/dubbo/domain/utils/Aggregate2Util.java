package com.glasssix.dubbo.domain.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import java.io.Serializable;
import com.glasssix.dubbo.domain.Aggregate2;
import com.glasssix.dubbo.domain.vo.Aggregate2VO;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
* OLAP
* @TableName aggregate2
*/
@Data
@Builder
public class Aggregate2Util {

    public static Aggregate2 toAggregate2(Aggregate2VO aggregate2VO) {
        Aggregate2 aggregate2 = new Aggregate2();
        if (ObjectUtils.isNotEmpty(aggregate2VO)) {
            BeanUtils.copyProperties(aggregate2VO, aggregate2);
        }
        return aggregate2;
    }

    public static Aggregate2VO toAggregate2VO(Aggregate2 aggregate2) {
        Aggregate2VO aggregate2VO = new Aggregate2VO();
        if (ObjectUtils.isNotEmpty(aggregate2)) {
            BeanUtils.copyProperties(aggregate2, aggregate2VO);
        }
        return aggregate2VO;
    }

    public static List<Aggregate2VO> toAggregate2VOList(List<Aggregate2> aggregate2List) {
        List<Aggregate2VO> list = new ArrayList();
        for (Aggregate2 aggregate2 : aggregate2List) {
            list.add(toAggregate2VO(aggregate2));
        }
        return list;
    }

    public static List<Aggregate2> toaggregate2List(List<Aggregate2VO> aggregate2VOList) {
        List<Aggregate2> list = new ArrayList();
        for (Aggregate2VO aggregate2VO : aggregate2VOList) {
            list.add(toAggregate2(aggregate2VO));
        }
        return list;
    }
}
