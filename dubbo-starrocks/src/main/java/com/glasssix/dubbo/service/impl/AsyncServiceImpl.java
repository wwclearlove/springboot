package com.glasssix.dubbo.service.impl;

import com.glasssix.dubbo.domain.Aggregate1;
import com.glasssix.dubbo.mapper.Aggregate1Mapper;
import com.glasssix.dubbo.service.Aggregate1Service;
import com.glasssix.dubbo.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;


@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {
    @Autowired
    Aggregate1Service aggregate1Service;

    @Async("asyncExecutor")
    @Override
    public void asyncBatchInsert(CountDownLatch countDownLatch, List<Aggregate1> aggregate1s) {
        try{
            log.warn("start Batch Insert");
            //每个线程要做的操作
            aggregate1Service.saveBatch(aggregate1s);
            log.warn("end Batch Insert");
        }finally {
            // 很关键, 无论上面程序是否异常必须执行countDown,否则await无法释放
            countDownLatch.countDown();
        }
    }
}