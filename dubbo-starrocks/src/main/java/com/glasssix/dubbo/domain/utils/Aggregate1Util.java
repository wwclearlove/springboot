package com.glasssix.dubbo.domain.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import java.io.Serializable;
import com.glasssix.dubbo.domain.Aggregate1;
import com.glasssix.dubbo.domain.vo.Aggregate1VO;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
* OLAP
* @TableName aggregate1
*/
@Data
@Builder
public class Aggregate1Util {

    public static Aggregate1 toAggregate1(Aggregate1VO aggregate1VO) {
        Aggregate1 aggregate1 = new Aggregate1();
        if (ObjectUtils.isNotEmpty(aggregate1VO)) {
            BeanUtils.copyProperties(aggregate1VO, aggregate1);
        }
        return aggregate1;
    }

    public static Aggregate1VO toAggregate1VO(Aggregate1 aggregate1) {
        Aggregate1VO aggregate1VO = new Aggregate1VO();
        if (ObjectUtils.isNotEmpty(aggregate1)) {
            BeanUtils.copyProperties(aggregate1, aggregate1VO);
        }
        return aggregate1VO;
    }

    public static List<Aggregate1VO> toAggregate1VOList(List<Aggregate1> aggregate1List) {
        List<Aggregate1VO> list = new ArrayList();
        for (Aggregate1 aggregate1 : aggregate1List) {
            list.add(toAggregate1VO(aggregate1));
        }
        return list;
    }

    public static List<Aggregate1> toaggregate1List(List<Aggregate1VO> aggregate1VOList) {
        List<Aggregate1> list = new ArrayList();
        for (Aggregate1VO aggregate1VO : aggregate1VOList) {
            list.add(toAggregate1(aggregate1VO));
        }
        return list;
    }
}
