package com.glasssix.dubbo.service.impl;


import com.glasssix.dubbo.constant.Constants;
import com.glasssix.dubbo.exception.PermissionsException;
import com.glasssix.dubbo.service.RedisService;
import com.glasssix.dubbo.utils.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: g6
 * @description:
 * @author: wenjiang
 * @create: 2020-08-09 10:51
 **/
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    StringRedisTemplate template;

    @Autowired
    RedisClient redisClient;


    @Override
    public void convertAndSend(String channel, Object message) {
        template.convertAndSend(channel, message);
    }

    @Override
    public boolean setOneMinuteExp(String key, Object data) {
        return redisClient.set(key, data, 60);
    }

    @Override
    public boolean setThirtySecondsExp(String key, Object data) {
        return redisClient.set(key, data, 30);
    }

    @Override
    public boolean setFiveMinuteExp(String key, Object data) {
        return redisClient.set(key, data, 300);
    }

    @Override
    public boolean setTwoMinuteExp(String key, Object data) {
        return redisClient.set(key, data, 120);
    }

    @Override
    public boolean setOneDayExp(String key, Object data) {
        return redisClient.set(key, data, 86400);
    }

    @Override
    public boolean setOneWeekExp(String key, Object data) {
        return redisClient.set(key, data, 604800);
    }

    @Override
    public boolean setOneMonthExp(String key, Object data) {
        return redisClient.set(key, data, 2592000);
    }

    @Override
    public boolean setOneYearExp(String key, Object data) {
        return redisClient.set(key, data, 31536000);
    }

    @Override
    public boolean persistSet(String key, Object data) {
        return redisClient.set(key, data);
    }

    @Override
    public boolean setTodayExp(String key, Object data) {
        return redisClient.set(key, data, getSecondsNextEarlyMorning());
    }

    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    @Override
    public boolean set(String key, Object data, long expireTime) {
        return redisClient.set(key, data, expireTime);
    }

    @Override
    public boolean hset(String key, String item, Object data) {
        return redisClient.hset(key, item, data);
    }

    @Override
    public Object hget(String key, String item) {
        return redisClient.hget(key, item);
    }

    @Override
    public List<Object> hgets(String key) {
        return redisClient.hgets(key);
    }

    @Override
    public Boolean hmset(String key, Map map) {
        return redisClient.hmset(key, map);
    }

    @Override
    public Object get(String key) {
        return redisClient.get(key);
    }

    @Override
    public List mGet(List keys) {
        return redisClient.mGet(keys);
    }

    @Override
    public boolean hasKey(String key) {
        return redisClient.hasKey(key);
    }

    @Override
    public Set keys(String key) {
        return redisClient.keys(key);
    }

    @Override
    public long expire(String key) {
        return redisClient.getExpire(key);
    }

    @Override
    public boolean expire(String key, long time) {
        return redisClient.expire(key, time);
    }

    @Override
    public void remove(String key) {
        redisClient.del(key);
    }

    @Override
    public void remove(String... key) {
        redisClient.del(key);
    }

    @Override
    public void removeByPrefix(String prefix) {
        redisClient.deleteByPrefix(prefix);
    }

    @Override
    public Long getId() {
        return redisClient.incr(Constants.GLASS_GLOBAL_ID);
    }

    @Override
    public Long incr(String key) {
        return redisClient.incr(key);
    }


    @Override
    public void hdel(String key, String epcId) {
        redisClient.hdel(key, epcId);
    }




}

