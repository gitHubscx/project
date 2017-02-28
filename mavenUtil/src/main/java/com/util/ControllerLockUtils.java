package com.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class ControllerLockUtils {
	private static final Logger logger = Logger.getLogger(ControllerLockUtils.class);
    private static Map<String, ReentrantLock> mapLock = new HashMap<String, ReentrantLock>();
    /**
     * 加锁
     * @param method 方法名
     */
    public static boolean lock(String method) {
        String serverName = SecurityContext.getRequest().getServerName();
        String key = MD5Util.getInstance().encrypt16(serverName + method);
        // 取得当前得lock
        if(mapLock.get(key) == null) {
            mapLock.put(key, new ReentrantLock());
        }
        boolean res = mapLock.get(key).tryLock();
        logger.debug("ContrllerLockUtils.lock(); param:" + key + ",res:" + res + ",serverName:" + serverName);
        return res;
    }
    /**
     * 解锁
     * @param method 方法名
     */
    public static void unlock(String method) {
        String serverName = SecurityContext.getRequest().getServerName();
        String key = MD5Util.getInstance().encrypt16(serverName + method);
        if(mapLock.get(key) == null) {
            return;
        }
        mapLock.get(key).unlock(); // 线程解锁
        mapLock.remove(key); // 删除key
        logger.debug("ContrllerLockUtils.unlock(); param:" + key + ",serverName:" + serverName);
    }

}
