package com.glasssix.dubbo.controller;

import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.constants.XxlReturnT;
import com.glasssix.dubbo.pojo.XxlJobInfo;
import com.glasssix.dubbo.utils.XxlJobUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/job")
public class AlarmClockController {
    @Value("${xxl.job.admin.addresses}")
    String url;

    @GetMapping(value = "/pageList")
    public Object pageList() throws IOException {
        HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("length", 10);
        objectObjectHashMap.put("jobGroup", 2);
        objectObjectHashMap.put("triggerStatus", -1);
        String s = XxlJobUtil.pageList(url, objectObjectHashMap);
        return s;
    }

    @PostMapping(value = "/add")
    public Object add() throws IOException {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobGroup(2);
        xxlJobInfo.setJobDesc("我来试试");
        xxlJobInfo.setAddTime(LocalDateTime.now());
        xxlJobInfo.setUpdateTime(LocalDateTime.now());
        xxlJobInfo.setAuthor("JCccc");
//        xxlJobInfo.setAlarmEmail("1280488753@com");
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setScheduleConf("0/5 * * * * ?");
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setExecutorHandler("clockInJobHandler");
        xxlJobInfo.setExecutorParam("test");
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(1);
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setGlueSource("");
        xxlJobInfo.setGlueRemark("GLUE代码初始化");
        xxlJobInfo.setGlueUpdatetime(LocalDateTime.now());
        XxlReturnT xxlReturnT = XxlJobUtil.addJob(url, JSON.toJSONString(xxlJobInfo));
        return xxlReturnT;
    }

    @PostMapping(value = "/stop/{jobId}")
    public XxlReturnT stop(@PathVariable("jobId") Integer jobId) throws IOException {
        XxlReturnT xxlReturnT = XxlJobUtil.stopJob(url, jobId);
        return xxlReturnT;

    }

    @PostMapping(value = "/delete/{jobId}")
    public XxlReturnT delete(@PathVariable("jobId") Integer jobId) throws IOException {


        XxlReturnT xxlReturnT = XxlJobUtil.deleteJob(url, jobId);
        return xxlReturnT;
    }


    @PostMapping(value = "/start/{jobId}")
    public XxlReturnT start(@PathVariable("jobId") Integer jobId) throws IOException {
        XxlReturnT xxlReturnT = XxlJobUtil.startJob(url, jobId);
        return xxlReturnT;
    }

}