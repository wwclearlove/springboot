package com.glasssix.dubbo.service;

import com.glasssix.dubbo.model.XxlJobGroup;

import java.util.List;

public interface JobGroupService {

    List<XxlJobGroup> getJobGroup();

    boolean autoRegisterGroup();

    boolean preciselyCheck();

}
