package com.esunny.util;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.factory;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.WriteOptions;

public class LevelDBHandler implements DBHandler {
    private String dbPath;
    private String tableName;
    private DB db;
    private WriteOptions wo;
    private ReadOptions ro;

    @Override
    public void finalize() {
        if (db != null) {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void newDBHandler(String dbPath, String tableName) {
        Options options = new Options().createIfMissing(false);
        options.createIfMissing(true);
        try {
            db = factory.open(getTestDirectory(dbPath, tableName), options);
        } catch (IOException e) {
            e.printStackTrace();
            if (db != null)
                try {
                    db.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        wo = new WriteOptions().sync(false);
        ro = new ReadOptions().fillCache(true).verifyChecksums(true);
    }

    public LevelDBHandler(String dbPath, String tableName) {
        super();
        this.dbPath = dbPath;
        this.tableName = tableName;
        newDBHandler(dbPath, tableName);
    }

    private File getTestDirectory(String dbPath, String tableName) {
        File rc = new File(new File(dbPath), tableName);
        rc.mkdirs();
        return rc;
    }

    @Override
    public void put(String key, String value) {
        db.put(bytes(key), bytes(value));
    }

    @Override
    public String get(String key) {
        String str = asString(db.get(bytes(key), ro));
        return str;
    }

    @Override
    public void delete(String key) {
        db.delete(bytes(key));
    }

    @Override
    public void close() {
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        close();
        File rc = new File(new File(dbPath), tableName);
        try {
            factory.destroy(rc, new Options().createIfMissing(true));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
