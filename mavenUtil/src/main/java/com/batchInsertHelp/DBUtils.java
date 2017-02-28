package com.batchInsertHelp;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBUtils {


    /**
     * 生成批量insert values语句
     *
     * @param tableName   表名称
     * @param listData    数据源
     * @param singleCount 每个insert语句后的 values 的长度，默认值100
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<String> getBathcInertSqlsByListMap(String tableName, List<Map<String, String>> listData, Integer singleCount) {
        List<String> listSql = new ArrayList<String>();
        if (listData == null || listData.size() == 0) {
            return listSql;
        }
        if (singleCount <= 0) {
            singleCount = 100;
        }

        // 处理数据分页
        int pageCount = singleCount;
        int maxPage = listData.size() / pageCount;
        if (listData.size() % pageCount != 0) {
            maxPage = maxPage + 1;
        }
        for (int p = 1; p <= maxPage; p++) {
            PageListBean bean = new PageListBean();
            bean.setCurrentPage(p);
            // 分页后的数据
            List pagerList = bean.getPaper(listData, pageCount);

            /*
             * 处理sql拼接 
             */
            StringBuffer sql = new StringBuffer();
            sql.append("insert into " + tableName + " ");
            sql.append("(");
            // 处理列
            StringBuffer sbKeys = new StringBuffer();
            List<String> keyList = new ArrayList<String>();
            Map<String, String> mapSingleCol = (Map<String, String>) pagerList.get(0);
            for (Map.Entry<String, String> entry : mapSingleCol.entrySet()) {
                keyList.add(entry.getKey());
                sbKeys.append(entry.getKey() + ",");
            }
            sql.append(sbKeys.toString().substring(0, sbKeys.toString().length() - 1));
            sql.append(") \n");
            sql.append("values");

            // 处理数据
            for (int i = 0; i < pagerList.size(); i++) {
                StringBuffer sbValues = new StringBuffer();
                Map<String, String> mapSingleVal = (Map<String, String>) pagerList.get(i);
                sql.append("(");
                // Abner 2015年9月10日09:53:32 注释.
                // 保留至 十一放假结束.
//                for (Map.Entry<String, String> entry : mapSingleVal.entrySet()) {
//                    if (entry.getValue() == null || "".equals(entry.getValue())) {
//                        sbValues.append("DEFAULT,");
//                    } else {
//                        // 拼接并过滤掉 值中出现的'和/
//                        sbValues.append("'" + entry.getValue().replace("'", "~").replace("\\", "/") + "',");
//                    }
//                }

                // Abner 2015年9月10日09:53:59
                // 修正 map为json反序列化时,排序不正确的bug
                for (String k : keyList) {
                    String v = mapSingleVal.get(k);
                    if (v == null || "".equals(v)) {
                        sbValues.append("DEFAULT,");
                    } else {
                        // 拼接并过滤掉 值中出现的'和/
                        sbValues.append("'" + v.replace("'", "~").replace("\\", "/") + "',");
                    }
                }
                sql.append(sbValues.subSequence(0, sbValues.toString().length() - 1));
                sql.append(")\n" + ((i + 1) != pagerList.size() ? "," : ""));
            }
            sql.append(";");
            listSql.add(sql.toString());
        }
        return listSql;
    }


    /**
     * 将List<Object>转换为List<Map<String,String>> 结构
     *
     * @param listObj      数据源
     * @param parentLevel  继承关系层级，默认无继承，取第一层传入1，依此类推
     * @param excludeAttrs 不需要参与转换的属性的名称
     * @return
     */
    public static List<Map<String, String>> convertListObjToListMap(List<? extends Object> listObj, Integer parentLevel, String... excludeAttrs) {
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
        if (listObj == null || listObj.size() == 0) {
            return listData;
        }
        if (parentLevel == null) {
            // 默认当前类
            parentLevel = 0;
        }
        List<String> listExcludeAttrs = new ArrayList<String>();
        if (excludeAttrs != null && excludeAttrs.length != 0) {
            listExcludeAttrs = Arrays.asList(excludeAttrs);
        }
        for (int p = 0; p < listObj.size(); p++) {
            Object obj = listObj.get(p);
            try {
                // 记录单个obj的数据
                Map<String, String> mapSingleObj = new HashMap<String, String>();


                // 取当前类& 父类所有的属性
                int curParentLevel = 0; // 默认当前类开始
                for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                    if (curParentLevel > parentLevel) {
                        break;
                    }

                    // 取属性名称&值
                    Field fields[] = clazz.getDeclaredFields();
                    Field.setAccessible(fields, true);
                    for (int i = 0; i < fields.length; i++) {
                        String names = fields[i].getName();
                        String values = fields[i].get(obj) != null ? fields[i].get(obj).toString() : "";
                        if (!listExcludeAttrs.contains(names)) {
                            // 记录列&数据
                            mapSingleObj.put(names, values);
                        }
                    }
                    curParentLevel++;
                }
                listData.add(mapSingleObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

}
