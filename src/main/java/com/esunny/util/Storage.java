package com.esunny.util;

public class Storage {

    public static final String DB_PATH = ".";
    public static final String DB_NAME = "AUTHORITY_MANAGER_DB";
    
    private DBHandler db;
    
    private Storage() {
        db = DBHandler.createDBHandler(DB_PATH, DB_NAME);
    } 
    
    public void put(String key, String value) {
        db.put(key, value);
    }

    public String get(String key) {
        return db.get(key);
    }
 
    public void delete(String key) {
        db.delete(key);
    }
 
    public void close() {
        db.close();
    }

    public void clear() {
        db.clear();
    }
    
    private static Storage storage;
    
    public static synchronized Storage getInstance() {
        if (storage == null) {
            storage = new Storage();
        }
        return storage;
    }
    
    
}
