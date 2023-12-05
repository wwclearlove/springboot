package com.glasssix.dubbo.service;

import java.util.concurrent.TimeUnit;

public interface RedisLock {


    /**
     * 获取分布式锁，原子操作
     * @param lockKey
     * @param requestId 唯一ID, 可以使用UUID.randomUUID().toString();
     * @param expire
     * @param timeUnit
     * @return
     */
    boolean tryLock(String lockKey, String requestId, long expire, TimeUnit timeUnit);

    boolean tryLockForever(String lockKey, String requestId);

    boolean tryLock(String lockKey, String requestId);
    /**
     * 释放锁
     * @param lockKey
     * @param requestId 唯一ID
     * @return
     */
    boolean releaseLock(String lockKey, String requestId);
    /**
     * 获取Redis锁的value值
     * @param lockKey
     * @return
     */
    String get(String lockKey);

    /**
     * 按指定有效期获取自增唯一序号
     * @param key
     * @param liveTime
     * @param timeUnit
     * @return
     */
    long getSeqNo(String key, Long liveTime, TimeUnit timeUnit);

    /**
     * 按指定日期获取自增唯一序号
     * @param key
     * @return
     */
    long getSeqNo(String key);

    String getSeqNoAppend(String key, int len);

    /**
     * 按指定秒数获取自增唯一序号
     * @param key
     * @param l
     * @return
     */
    long getSeqNo(String key, Long l);

    boolean tryLockWithSet(String lockKey, String lockValue, long expire, TimeUnit timeUnit);
}
