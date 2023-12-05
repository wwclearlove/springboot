package com.glasssix.dubbo.controller;

import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.domain.Aggregate1;
import com.glasssix.dubbo.domain.vo.Aggregate1VO;
import com.glasssix.dubbo.service.Aggregate1Service;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@Slf4j
public class ProducerController {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    AdminClient adminClient;
    @Autowired
    private Aggregate1Service aggregate1Service;

    public LocalDate randomLong() {
        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //输入 随机起始时间
        String str = "2021-01-01";
        //解析时间
        Date d1 = null;
        try {
            d1 = sdf.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long before = d1.getTime();
        //获取当前时间
        Date d2 = new Date();
        long after = d2.getTime();
        Random r = new Random();
        long l = (long) (before + (r.nextFloat() * (after - before + 1)));
        return Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @GetMapping("/register/{num}")
    public void sendMessage(@PathVariable("num") String num) {
        kafkaTemplate.send("test", String.format("消息内容:%s", num));
    }

    @GetMapping("/register2")
    public void sendMessage2() {
        Random r = new Random();
//        int type = r.nextInt(2) + 1;
//        int areaId = (int) (Math.random() * 100 + 1);
//        Aggregate1 aggregate = new Aggregate1(randomLong(), type, areaId, 1L);
//        String json = JSON.toJSONString(aggregate);
//        System.out.println(json);

//        kafkaTemplate.send("topic4", json);
//        int j = 1;
        List<Aggregate1> list = Lists.newArrayList();
        for (int i = 0; i < 50000000; i++) {
            int type = r.nextInt(2) + 1;
            int areaId = (int) (Math.random() * 100 + 1);
            Aggregate1 aggregate = new Aggregate1(randomLong(), type, areaId, 1L);
            String json = JSON.toJSONString(aggregate);
            System.out.println(i);
            list.add(aggregate);
            if (list.size() == 1000) {
                aggregate1Service.saveBatch(list);
                list.clear();
            }
            kafkaTemplate.send("topic123", json);
        }
        if(list.size()>0){
            aggregate1Service.saveBatch(list);
        }
        log.info("发送成功：topic-");
    }

    @GetMapping("/getAllTopic")
    public void getAllTopic() throws Exception {
        ListTopicsResult listTopics = adminClient.listTopics();
        Set<String> topics = listTopics.names().get();

        for (String topic : topics) {
            System.err.println(topic);

        }
    }
}
