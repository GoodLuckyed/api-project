package com.lucky.apigateway.utils;

import com.lucky.apicommon.common.ErrorCode;
import com.lucky.apigateway.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author lucky
 * @date 2024/6/23
 * @description  redisson 分布式锁工具类
 */
@Component
@Slf4j
public class RedissonLockUtil {

    @Resource
    private RedissonClient redissonClient;

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param supplier 供应商
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     * @return
     * @param <T>
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, ErrorCode errorCode,String errorMessage) {
        // 获取锁对象
        RLock rlock = redissonClient.getLock(lockName);
        try {
            // 尝试获取锁
            if(rlock.tryLock(0,-1, TimeUnit.MILLISECONDS)){
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(),errorMessage);
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,e.getMessage());
        } finally {
            // 释放锁
            if(rlock.isHeldByCurrentThread()){
                log.info("unlock: " + Thread.currentThread().getId());
                rlock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     * @param waitTime 等待时间
     * @param leaseTime 锁释放时间
     * @param unit 时间单位
     * @param lockName 锁名称
     * @param supplier 供应商
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     * @return
     * @param <T>
     */
    public <T> T redissonDistributedLocks(long waitTime, long leaseTime, TimeUnit unit, String lockName, Supplier<T> supplier, ErrorCode errorCode, String errorMessage) {
        // 获取锁对象
        RLock rLock = redissonClient.getLock(lockName);
        try {
            // 尝试获取锁
            if (rLock.tryLock(waitTime, leaseTime, unit)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            // 释放锁
            if (rLock.isHeldByCurrentThread()) {
                log.info("unlock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     * @param waitTime 等待时间
     * @param unit 时间单位
     * @param lockName 锁名称
     * @param supplier 供应商
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     * @return
     * @param <T>
     */
    public <T> T redissonDistributedLocks(long waitTime, TimeUnit unit, String lockName, Supplier<T> supplier, ErrorCode errorCode, String errorMessage) {
        // 获取锁对象
        RLock rLock = redissonClient.getLock(lockName);
        try {
            // 尝试获取锁
            if (rLock.tryLock(waitTime, unit)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            // 释放锁
            if (rLock.isHeldByCurrentThread()) {
                log.info("unlock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param supplier 供应商
     * @param errorCode 错误代码
     * @return
     * @param <T>
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, ErrorCode errorCode) {
        return redissonDistributedLocks(lockName, supplier, errorCode, errorCode.getMessage());
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param supplier 供应商
     * @param errorMessage 错误消息
     * @return
     * @param <T>
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier,String errorMessage) {
        return redissonDistributedLocks(lockName, supplier, ErrorCode.OPERATION_ERROR, errorMessage);
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param supplier 供应商
     * @return
     * @param <T>
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier){
        return redissonDistributedLocks(lockName, supplier,ErrorCode.OPERATION_ERROR);
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param runnable 可运行
     * @param errorCode 错误代码
     * @param errormessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable,ErrorCode errorCode,String errormessage){
        // 获取锁对象
        RLock rLock = redissonClient.getLock(lockName);
        try {
            // 尝试获取锁
            if(rLock.tryLock(0,-1,TimeUnit.MILLISECONDS)){
                runnable.run();
            }else {
                throw new BusinessException(errorCode.getCode(),errormessage);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,e.getMessage());
        } finally {
            // 释放锁
            if(rLock.isHeldByCurrentThread()){
                log.info("unlock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     * @param waitTime 等待时间
     * @param leaseTime 锁释放时间
     * @param unit 时间单位
     * @param lockName 锁名称
     * @param runnable 可运行
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(long waitTime, long leaseTime, TimeUnit unit, String lockName, Runnable runnable, ErrorCode errorCode, String errorMessage) {
        // 获取锁对象
        RLock rLock = redissonClient.getLock(lockName);
        try {
            // 尝试获取锁
            if (rLock.tryLock(waitTime, leaseTime, unit)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            // 释放锁
            if (rLock.isHeldByCurrentThread()) {
                log.info("unlock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     * @param waitTime 等待时间
     * @param unit 时间单位
     * @param lockName 锁名称
     * @param runnable 可运行
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(long waitTime, TimeUnit unit, String lockName, Runnable runnable, ErrorCode errorCode, String errorMessage) {
        // 获取锁对象
        RLock rLock = redissonClient.getLock(lockName);
        try {
            // 尝试获取锁
            if (rLock.tryLock(waitTime, unit)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            // 释放锁
            if (rLock.isHeldByCurrentThread()) {
                log.info("unlock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param runnable 供应商
     * @param errorCode 错误代码
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, ErrorCode errorCode) {
        redissonDistributedLocks(lockName, runnable, errorCode, errorCode.getMessage());
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param runnable 供应商
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, String errorMessage) {
        redissonDistributedLocks(lockName, runnable, ErrorCode.OPERATION_ERROR, errorMessage);
    }

    /**
     * redisson分布式锁
     * @param lockName 锁名称
     * @param runnable 供应商
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable) {
        redissonDistributedLocks(lockName, runnable, ErrorCode.OPERATION_ERROR, ErrorCode.OPERATION_ERROR.getMessage());
    }
}
