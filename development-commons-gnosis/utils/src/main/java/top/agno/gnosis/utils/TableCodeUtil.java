package top.agno.gnosis.utils;

import top.agno.gnosis.utils.random.RandomUtils;

public enum TableCodeUtil {
    T_TABLE_SAMPLE("T_TABLE_SAMPLE", "001"),
    ;


    /**
     * 表名
     */
    private String tableName;
    /**
     * 开始prefix
     */
    private String tableCode;

    TableCodeUtil(String tableName, String tableCode) {
        this.tableCode = tableCode;
        this.tableName = tableName;
    }


    /**
     * 获取表主键编码编码.
     *
     * @return String 主键编码
     */
    public String getTableCode() {
        //数据库主键编码长度
        int length = 16;
        return RandomUtils.generateRandomNumber(length, tableCode);

    }


    /**
     * 获取表名称
     *
     * @return
     */
    public String getTableName() {
        return this.tableName;
    }
}
