package com.glasssix.dubbo.service;

import com.glasssix.dubbo.pojo.Person;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

/**
* @author Glasssix-LJT
* @description 针对表【person(执勤人员信息表)】的数据库操作Service
* @createDate 2022-04-21 11:44:10
*/
public interface PersonService extends IService<Person> {
    Long getRedisData();
}
