package com.glasssix.dubbo.service;



import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: g6
 * @description:
 * @author: wenjiang
 * @create: 2020-07-01 10:48
 **/
public interface RedisService<T> {

    /**
     * 放值
     *
     * @param channel
     * @param message
     * @return
     */
    void convertAndSend(String channel, Object message);

    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setOneMinuteExp(String key, Object data);
    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setThirtySecondsExp(String key, Object data);
    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setFiveMinuteExp(String key, Object data);

    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setTwoMinuteExp(String key, Object data);


    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setOneDayExp(String key, Object data);


    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setOneWeekExp(String key, Object data);

    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setOneMonthExp(String key, Object data);

    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean setOneYearExp(String key, Object data);


    /**
     * 持久化放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean persistSet(String key, Object data);

    /**
     * 设置今天过期
     *
     * @param key
     * @param data
     * @return
     */
    boolean setTodayExp(String key, Object data);


    /**
     * 放值
     *
     * @param key
     * @param data
     * @return
     */
    boolean set(String key, Object data, long expireTime);


    /**
     * 删除
     *
     * @param key
     * @return
     */
    void remove(String key);

    void remove(String... key);

    /**
     * 模糊删除
     *
     * @param prefix
     */
    void removeByPrefix(String prefix);

    /**
     * Hash set
     *
     * @param key
     * @param item
     * @param data
     * @return
     */
    boolean hset(String key, String item, Object data);


    /**
     * Hash get
     *
     * @param key
     * @param item
     * @return
     */
    Object hget(String key, String item);

    /**
     * Hash get
     *
     * @param key
     * @return
     */
    List<Object> hgets(String key);

    Boolean hmset(String key, Map<String, Object> map);

    /**
     * 取值
     *
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 批量取值
     *
     * @param keys
     * @return
     */
    List mGet(List<String> keys);

    /**
     * 判断key存在
     *
     * @param key
     * @return
     */
    boolean hasKey(String key);

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    long expire(String key);

    /**
     * 设置过期时间
     *
     * @param key
     * @param time 剩余时间毫秒
     * @return
     */
    boolean expire(String key, long time);

    /**
     * 获取自增ID
     *
     * @param
     * @return
     */
    Long getId();


    /**
     * 计数
     *
     * @param key
     * @return
     */
    Long incr(String key);

    void hdel(String key, String epcId);

    Set keys(String key);


}

