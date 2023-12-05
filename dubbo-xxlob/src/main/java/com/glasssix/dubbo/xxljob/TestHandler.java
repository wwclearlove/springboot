package com.glasssix.dubbo.xxljob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestHandler {
    @XxlJob("clockInJobHandler")
    public void deviceHeartHandler() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("参数:{}", param);
        if (StringUtils.isEmpty(param)) {
            param = "5";
        }
        try {
            log.info("设备心跳检测参数：{}", param);
        } catch (Exception e) {
            // 执行结束后返回的方式，同样在执行日志可查看
            XxlJobHelper.handleFail("执行失败");
        }
        // 执行结束后返回的方式，同样在执行日志可查看
        XxlJobHelper.handleSuccess("执行成功");
    }
}
