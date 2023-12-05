package com.glasssix.dubbo.service.impl;


import com.glasssix.dubbo.service.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 分布式锁工具类
 * @Author:
 * @CreateDate:
 */
@Component
@Slf4j
public class RedisLockImpl implements RedisLock {
    @Resource
    private RedisTemplate redisTemplate;
    public static final String UNLOCK_LUA;
    private static final int ACQUIRE_LOCK_TIME_OUT_IN_MS = 15 * 1000;//获取锁超时时间
    private static final int WAIT_INTERVAL_IN_MS = 100;             //自旋重试间隔

    /**
     * 释放锁脚本，原子操作
     */
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }


    /**
     * 获取分布式锁，原子操作
     *
     * @param lockKey
     * @param requestId 唯一ID, 可以使用UUID.randomUUID().toString();
     * @param expire
     * @param timeUnit
     * @return
     */
    @Override
    public boolean tryLock(String lockKey, String requestId, long expire, TimeUnit timeUnit) {
        try {
            return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) (connection) ->
                    connection.set(lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")),
                            expire < 1 ? Expiration.seconds(5) : Expiration.seconds(timeUnit == null ? TimeUnit.SECONDS.toSeconds(expire) : timeUnit.toSeconds(expire)),
                            RedisStringCommands.SetOption.SET_IF_ABSENT));
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    @Override
    public boolean tryLockForever(String lockKey, String requestId) {
        try {
            RedisCallback<Boolean> callback = (connection) -> connection.set(lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")),
                    Expiration.seconds(TimeUnit.DAYS.toSeconds(365)), RedisStringCommands.SetOption.SET_IF_ABSENT);
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    @Override
    public boolean tryLock(String lockKey, String requestId) {
        try {
            return tryLock(lockKey, requestId, 0, null);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId 唯一ID
     * @return
     */
    @Override
    public boolean releaseLock(String lockKey, String requestId) {
        boolean execute = Boolean.FALSE;
        try {
            execute = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) (connection) ->
                    connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")))
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("释放锁结果:{},{},{}", execute, lockKey, requestId);
        }
        return execute;
    }

    /**
     * 获取Redis锁的value值
     *
     * @param lockKey
     * @return
     */
    @Override
    public String get(String lockKey) {
        try {
            RedisCallback<String> callback = (connection) -> new String(connection.get(lockKey.getBytes()), Charset.forName("UTF-8"));
            return (String) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("get redis occurred an exception", e);
        }
        return null;
    }

    @Override
    public long getSeqNo(String key, Long liveTime, TimeUnit timeUnit) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.incrementAndGet();
        //初始设置过期时间
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {
            entityIdCounter.expire(liveTime, timeUnit);
        }
        return increment;
    }

    @Override
    public long getSeqNo(String key) {
        return getSeqNo(key + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")), 3L, TimeUnit.MINUTES);
    }

    @Override
    public String getSeqNoAppend(String key, int len) {
        return String.format("%0" + len + "d", key);
    }

    @Override
    public long getSeqNo(String key, Long l) {
        return getSeqNo(key, 1L, TimeUnit.SECONDS);
    }


    /**
     * 自旋锁=>重试
     *
     * @param lockKey
     * @return
     */
    @Override
    public boolean tryLockWithSet(String lockKey, String lockValue, long expire, TimeUnit timeUnit) {
        boolean flag = false;
        //此次获取锁的超时时间点
        long timeoutAt = System.currentTimeMillis() + ACQUIRE_LOCK_TIME_OUT_IN_MS;
        while (true) {
            if (timeoutAt < System.currentTimeMillis()) {
                break;
            }
            if (tryLock(lockKey, lockValue, expire, timeUnit)) {
                flag = true;
                break;
            }
            try {
                TimeUnit.NANOSECONDS.sleep(WAIT_INTERVAL_IN_MS);
            } catch (InterruptedException e) {
                log.error("获取分布式锁异常,lockKey:{}", lockKey);
            }
        }
        log.info("获取分布式锁{}耗时:{},lockKey:{}", flag ? "成功" : "失败", System.currentTimeMillis() - timeoutAt + ACQUIRE_LOCK_TIME_OUT_IN_MS, lockKey);
        return flag;
    }

    /**
     * 自旋锁=>重试
     *
     * @param lockKey
     * @return
     */
    public boolean tryLockWithSet(String lockKey) {
        return tryLockWithSet(lockKey, lockKey, 0, null);
    }


}
