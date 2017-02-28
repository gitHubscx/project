package com.util;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository("baseDao")
public class BaseDao extends JdbcTemplate implements IBaseDao {
    private static Logger logger = Logger.getLogger(BaseDao.class);

    @Override
    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * 获取存储过程调用实例
     * 
     * @return 存储过程调用实例
     */
    public IProcParam getParamInstances() {
        return new ProcParam();
    }

    /**
     * 调用存储过程
     * 
     * @param name 存储过程名称
     * @param param 存储过程调用参数
     * @return 存储过程调用结果
     */
    public IProcResult callProcedure(String procedureName, IProcParam param) {
        ProcParam callParam = null;
        Connection connection = null;
        String dbName = ""; // 数据库名称
        if (!(param instanceof ProcParam)) {
            logger.error("call proc: " + procedureName + " param error...");
            return null;
        }
        callParam = (ProcParam) param; // 存储过程参数

        // 存储过程日志
        String procCallLog = String.format("proc：call %s%s", procedureName, callParam);
        ProcResult result = new ProcResult();
        int paramSize = callParam.paramList.size();

        try {
            /*
             * 获得连接对象
             */
            DataSource dataSource = super.getDataSource();
            connection = dataSource.getConnection();
            dbName = connection.getCatalog(); 

            /*
             * 拼接存储过程sql
             */
            long startTime = System.currentTimeMillis();
            StringBuffer sql = new StringBuffer("call " + procedureName + "( ");
            for (int i = 0; i < paramSize; i++) {
                sql.append("?,");
            }
            CallableStatement cstmt = connection.prepareCall(sql.toString().substring(0, sql.length() - 1) + ")");
            callParam.fillParam(cstmt); // 填充参数
            cstmt.execute(); // 执行

            result.cost = System.currentTimeMillis() - startTime;
            /*
             * 获取结果
             */
            for (int i = 0; i < paramSize; i++) {
                ProcParam.Param p = callParam.paramList.get(i);
                // 判断并获取出参
                if (p.paramType == ProcParamType.OUT || p.paramType == ProcParamType.IN_OUT) {
                    Object outValue = cstmt.getObject(i + 1);
                    result.outValues.put(i, outValue);
                    result.outValueList.add(outValue);
                }
            }

            /*
             *  获取结果集
             */
            ResultSet resultSet = cstmt.getResultSet();
            if (resultSet != null) {
                result.resultSetList.add(resultSetToArray(resultSet));
                resultSet.close();
            }
            while (cstmt.getMoreResults()) {
                resultSet = cstmt.getResultSet();
                // 将结果集转换为集合, 并添加到result中
                result.resultSetList.add(resultSetToArray(resultSet));
                if (resultSet != null) {
                    resultSet.close();
                }
            }
            cstmt.close();


            /*
             * 阿里云rds资源释放
             */
            String rdsFlushInfo = "";
            String jdbcUrl = connection.getMetaData().getURL();
            if (jdbcUrl.indexOf("rds") != -1 || jdbcUrl.indexOf("aliyun") != -1) {
                Statement stm = connection.createStatement();
                stm.execute(String.format("SET rds_current_connection rds_db ='%s';", dbName));
                stm.close();
                rdsFlushInfo = "[flush-rds]";
            }

            /*
             * 打印调用日志
             */
            if (!"pCom_Msg_Q".equals(procedureName)) {
                logger.debug(String.format("[%s][time:%s]%s%s", dbName, result.cost, rdsFlushInfo, procCallLog));
            }
        } catch (Exception e) {
            String errorMsg = String.format("[%s][time:%s][%s][db-error-msg:%s]", dbName, result.cost, procCallLog, e.getMessage());
            result.outValueList.add(-2);
            result.outValueList.add(errorMsg);
            logger.error(errorMsg, e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                result.outValueList.add(-2);
                result.outValueList.add(e);
                logger.error("connection.close()-db-error", e);
            }
        }
        return result;
    }


    /**
     * 转换resultset 为集合
     * 
     * @param resultSet 结果集
     * @return 结果集转换后的集合
     * @throws SQLException
     */
    private List<Map<String, Object>> resultSetToArray(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        // 获取列
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        // 迭代行
        while (resultSet.next()) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(md.getColumnLabel(i), resultSet.getObject(i));
            }
            data.add(row);
        }
        return data;
    }

    /**
     * 存储过程调用参数
     * 
     * @author Abner
     */
    public class ProcParam implements IBaseDao.IProcParam {
        // 参数集合
        private List<Param> paramList;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("(");
            for (int i = 0; i < paramList.size(); i++) {
                Param p = paramList.get(i);
                if (i > 0)
                    sb.append(",");

                if (i + 2 == paramList.size()) {
                    sb.append("@i_rest");
                } else if (i + 1 == paramList.size()) {
                    sb.append("@c_rest");
                } else {
                    switch (p.sqlType) {
                    case Types.VARCHAR:
                    case Types.DATE:
                        if (p.val != null && !"null".equals(p.val))
                            sb.append("'" + p.val + "'");
                        else
                            sb.append("null");
                        break;
                    default:
                        sb.append(p.val);
                        break;
                    }
                }
            }
            sb.append(");");
            return sb.toString();
        }

        /**
         * 存储过程调用参数(一个)
         * 
         * @author Abner
         */
        private class Param {
            private IBaseDao.ProcParamType paramType;
            private Object val;
            private int sqlType;

            public Param(ProcParamType paramType, Object val, int sqlType) {
                super();
                this.paramType = paramType;
                this.val = val;
                this.sqlType = sqlType;
            }

            @Override
            public String toString() {
                return "Param [paramType=" + paramType + ", val=" + val + ", sqlType=" + sqlType + "]";
            }
        }

        private ProcParam() {
            paramList = new ArrayList<Param>();
        }

        @Override
        public void put(final Object val, final ProcParamType paramType, int sqlType) {
            paramList.add(new Param(paramType, val, sqlType));
        }

        /**
         * 填充调用参数
         * 
         * @param cs
         */
        private boolean fillParam(CallableStatement cs) {
            try {
                for (int i = 0; i < paramList.size(); i++) {
                    Param p = paramList.get(i);
                    Object val = "null".equals(p.val) ? null : p.val;

                    if (p.paramType == ProcParamType.IN) {
                        cs.setObject(i + 1, val);
                    } else if (p.paramType == ProcParamType.OUT) {
                        cs.registerOutParameter(i + 1, p.sqlType);
                    } else {
                        cs.setObject(i + 1, val);
                        cs.registerOutParameter(i + 1, p.sqlType);
                    }
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    /**
     * 存储过程调用返回结果
     * 
     * @author Abner
     */
    private class ProcResult implements IProcResult {
        // 所有返回结果集
        private List<List<Map<String, Object>>> resultSetList;
        // 出参集合
        private Map<Integer, Object> outValues;
        private List<Object> outValueList;
        private long cost;

        public ProcResult() {
            resultSetList = new ArrayList<List<Map<String, Object>>>();
            outValues = new HashMap<Integer, Object>();
            outValueList = new ArrayList<Object>();
        }

        @Override
        public List<Map<String, Object>> getResultSet(int index) {
            return resultSetList.get(index);
        }

        @Override
        public int size() {
            return resultSetList.size();
        }

        @Override
        public Object getOutParamAt(int index) {
            return outValueList.get(index);
        }

        @Override
        public long getCost() {
            return cost;
        }

    }

}
