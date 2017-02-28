package com.util;


import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;

/**
 * 数据访问层根接口<br>
 * <b>所有数据访问接口必须直接或间接继承本接口</b>
 * @author Abner
 */
public interface IBaseDao extends JdbcOperations {

    /**
     * 获得一个存储过程调用参数实例
     *
     * @return 存储过程调用参数实例
     */
    public IProcParam getParamInstances();

    /**
     * 调用存储过程
     */
    public IProcResult callProcedure(String procedureName, IProcParam param);


    /**
     * 存储过程调用参数
     */
    public interface IProcParam {
        /*
         * 添加一个存储过程调用参数<br>
         * <br>
         * 请注意添加顺序</br>
         *
         * @param val       参数的值
         * @param paramType 该参数为入参/出参或双向参数
         * @param sqlType   参数数据类型 参见：{@link java.sql.Types}
         */
        public void put(Object val, final ProcParamType paramType, int sqlType);
    }

    /**
     * 存储过程调用返回结果
     *
     * @author Abner
     */
    public interface IProcResult {

        /*
         * 获取结果集的数量
         *
         * @return 结果集数量
         */
        public int size();

        /*
         * 获取指定索引的结果集<br>
         *
         * @return 指定索引的结果集对象
         */
        public List<Map<String, Object>> getResultSet(int index);

        /*
         * 获取存储过程出参
         *
         * @param index 出参集合中的索引(<b>索引从0开始</b>)
         * @return 指定索引出参的返回值
         */
        public Object getOutParamAt(int index);

        /*
         * 存储过程执行时间
         *
         * @return 存储过程执行时间(ms)
         */
        public long getCost();
    }

    /**
     * 存储过程调用参数类型
     *
     * @author Abner
     */
    public enum ProcParamType {
        /*
         * 入参
         */
        IN,
        /*
         * 出参
         */
        OUT,
        /*
         * 双向
         */
        IN_OUT
    }
}
