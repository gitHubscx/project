package com.util;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class IbatisBaseDao extends SqlMapClientDaoSupport {

    @Autowired
    @Resource(name = "sqlMapClient")
    public void init(SqlMapClient sqlMapClient) {
        setSqlMapClient(sqlMapClient);
    }
}

