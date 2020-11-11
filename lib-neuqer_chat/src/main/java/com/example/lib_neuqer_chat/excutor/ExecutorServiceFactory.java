package com.example.lib_neuqer_chat.excutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Time:2020/3/15 22:20
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ExecutorServiceFactory {

    private ExecutorService heartBeatPool;
    private ExecutorService reconnectPool;

    /**
     * 防止多线程异步地从线程池创建线程
     */
    public synchronized void initHeartBeatGroup() {
        initHeartBeatGroup(1);
    }

    private synchronized void initHeartBeatGroup(int size) {
        destroyHeartBeatGroup();
        heartBeatPool = Executors.newFixedThreadPool(size);
    }

    public synchronized void initReconnectGroup() {
        initReconnectGroup(1);
    }

    private synchronized void initReconnectGroup(int size) {
        destroyReconnectGroup();
        reconnectPool = Executors.newFixedThreadPool(size);
    }

    public void execHeatBeatTask(Runnable r) {
        if (heartBeatPool == null) {
            initHeartBeatGroup();
        }
        heartBeatPool.execute(r);
    }

    public void execReconnectGroup(Runnable r) {
        if (reconnectPool == null) {
            initReconnectGroup();
        }
        reconnectPool.execute(r);
    }

    public synchronized void destroyReconnectGroup() {
        if (reconnectPool != null) {
            try {
                reconnectPool.shutdownNow();
            } catch(Throwable t) {
                t.printStackTrace();
            } finally {
                reconnectPool = null;
            }
        }
    }

    /**
     * 释放心跳线程池
     */
    public synchronized void destroyHeartBeatGroup() {
        if (heartBeatPool != null) {
            try {
                heartBeatPool.shutdownNow();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                heartBeatPool = null;
            }
        }
    }

    public synchronized void destroyAll() {
        destroyHeartBeatGroup();
        destroyReconnectGroup();
    }

}
