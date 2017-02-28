package com.util.threadPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class TaskUtils {
    // 正在执行的task的客户信息队列
    private static Map<String, String> taskRuningMap = new HashMap<String, String>();

    // Task线程池
    private final static BlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<Runnable>(8);  
    private final static ThreadPoolExecutor EXECUTOR_SERVICE = new ThreadPoolExecutor(8, 8, 1, TimeUnit.HOURS, QUEUE, new ThreadPoolExecutor.CallerRunsPolicy());  

    public synchronized static String getTaskRuning(String key) {
        return taskRuningMap.get(key);
    }

    public synchronized static void setTaskRuning(String key, String val) {
        taskRuningMap.put(key, val);
    }

    public synchronized static void removeTaskRuning(String key) {
        taskRuningMap.remove(key);
    }

    public synchronized static Map<String, String> taskRuningMap() {
        return taskRuningMap;
    }


    public static void addTask(Runnable task) {
        EXECUTOR_SERVICE.submit(task);
    }

    public static ExecutorService getExecutor() {
        return EXECUTOR_SERVICE;
    }

    /**
     * 设置提示信息
     */
    public static Map<String, Object> setPromptInfoMap(String code, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

}
