package com.util.threadPool;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sunsoft.base.constant.ThreadLocalManager;
import com.sunsoft.srapi.task.service.ISysTaskQueueService;

public class TaskRunThread implements Runnable {
    private final static Logger logger = Logger.getLogger(TaskRunThread.class);
    private ISysTaskQueueService sysTaskQueueService;

    // 共享主线程信息
    private String domain;
    private DataSource dataSource;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private Exception exception;


    @Override
    public void run() {
        if(sysTaskQueueService == null) {
            return;
        }

        /*
         * 提交所有可能用到的ThreadLocal共享到子线程中
         */
        ThreadLocalManager.setDomain(domain);
        ThreadLocalManager.setDataSource(dataSource);
        ThreadLocalManager.setRequest(httpServletRequest);
        ThreadLocalManager.setResponse(httpServletResponse);
        ThreadLocalManager.setExLocal(exception);

        /*
         * 扫描执行任务
         */
        Map<String, Object> taskRunRes = this.sysTaskQueueService.runTask();
        logger.info(taskRunRes);
    }


    public void setSysTaskQueueService(ISysTaskQueueService sysTaskQueueService) {
        this.sysTaskQueueService = sysTaskQueueService;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }


    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }


}
