package com.glasssix.dubbo.service;

import com.glasssix.dubbo.domain.Aggregate1;
import com.glasssix.dubbo.mapper.Aggregate1Mapper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface AsyncService  {
    void asyncBatchInsert( CountDownLatch countDownLatch, List<Aggregate1> aggregate1s);
}
