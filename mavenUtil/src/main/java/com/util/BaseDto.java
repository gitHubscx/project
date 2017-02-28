package com.util;


import java.io.Serializable;

public class BaseDto implements Serializable {

    // 排序
    private String orderBy;
    // 分页开始页数
    private String startPos;
    // 分页记录数
    private String pageSize;
    // 其他条件
    private String otherCondStr;

    public String getStartPos() {
        return startPos;
    }

    public void setStartPos(String startPos) {
        this.startPos = startPos;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOtherCondStr() {
        return otherCondStr;
    }

    public void setOtherCondStr(String otherCondStr) {
        this.otherCondStr = otherCondStr;
    }

}
