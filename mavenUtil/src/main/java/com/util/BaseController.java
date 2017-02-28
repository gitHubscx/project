package com.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.util.StringUtil;

/**
 * 基础控制器
 *
 * @author XJin zuzjx@163.com
 * @ClassName BaseController
 * @date 2013-5-9 上午9:03:03
 */
public class BaseController {

    public final static String SUCCESS = "200";
    public final static String NO_LOGIN = "NO_LOGIN";
    public final static String FAILURE = "FAILURE";
    public final static String ERROR = "ERROR";

    /**
     * 公共json返回体
     *
     * @param result  是否成功
     * @param rescode 状态编码
     * @param msg     返回信息
     * @param url     url信息
     * @param data    消息体
     * @param total   总数
     * @return Map<String,Object> 返回的json体
     */
    protected Map<String, Object> createMap(Boolean result, Object rescode, Object msg, Object url, Object data, Object total, Object cost) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("result", result);
        jsonMap.put("rescode", rescode);
        jsonMap.put("msg", msg);
        jsonMap.put("url", url);
        jsonMap.put("data", data);
        jsonMap.put("total", total);
        jsonMap.put("cost", cost);
        return jsonMap;
    }

    /**
     * 计算翻页参数
     * param pageIndex
     * param pageCount
     * param totalSize
     *
     * @return
     * @author using using@qq.com
     */
    public static Map<String, Integer> pageCalc(HttpServletRequest request) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int pageIndex = StringUtil.isEmpty(request.getParameter("pageIndex")) == false ? Integer.parseInt(request.getParameter("pageIndex")) + 1 : 1;
        int pageCount = StringUtil.isEmpty(request.getParameter("pageSize")) == false ? Integer.parseInt(request.getParameter("pageSize")) : 100;
        String totalCount = request.getParameter("totalCount") == null ? "" : request.getParameter("totalCount").toString(); // 获取总条数参数
        int totalSize = 0;
        if (!totalCount.isEmpty()) {
            totalSize = Integer.parseInt(totalCount);
        }

        map.put("pageIndex", pageIndex);
        map.put("pageCount", pageCount);
        map.put("totalSize", totalSize);
        return map;
    }
}
