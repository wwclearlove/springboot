//package com.glasssix.dubbo.xxljob;
//
//import com.glasssix.dubbo.annotation.XxlRegister;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class TestXxlJob {
//    @XxlJob(value = "testJob")
//    @XxlRegister(cron = "0 0 0 * * ? *",
//            author = "wyc",
//            jobDesc = "测试job")
//    public void testJob(){
//        System.out.println("默认关闭");
//    }
//
//
//    @XxlJob(value = "testJob222")
//    @XxlRegister(cron = "59 1-2 0 * * ?",
//            author = "wyc",
//            jobDesc = "测试job2",
//            triggerStatus = 1)
//    public void testJob2(){
//        System.out.println("默认运行");
//    }
//
//}
