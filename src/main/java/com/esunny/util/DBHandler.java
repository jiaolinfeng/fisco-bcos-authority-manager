package com.esunny.util;

public interface DBHandler {
    static final String DBPATH = "walletdb";

    public static DBHandler createDBHandler(String dbPath, String tableName) {
        return new LevelDBHandler(dbPath, tableName);
    }

    public static DBHandler createDBHandler(String tableName) {
        return new LevelDBHandler(DBPATH, tableName);
    }

    public void put(String key, String value);

    public String get(String key);

    // 删除
    public void delete(String key);

    // 可选
    public void close();

    public void clear();

}
