package com.albert.simple.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.albert.cursorhelper.DatabaseHelper;

public class MyDatabaseHelper implements DatabaseHelper {

    private volatile static MyDatabaseHelper INSTANCE = null;

    private SQLiteDatabase mDatabase;

    private MyDatabaseHelper(Context context) {
        SQLiteOpenHelper openHelper = new MySQLiteOpenHelper(context.getApplicationContext());
        mDatabase = openHelper.getWritableDatabase();
    }

    public static MyDatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyDatabaseHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Cursor rawQuery(String sql, String... selectionArgs) {
        synchronized (this) {
            Cursor cursor = mDatabase.rawQuery(sql, selectionArgs);
            return cursor;
        }
    }

    @Override
    public void execSQL(String sql, String... bindArgs) {
        synchronized (this) {
            if (bindArgs.length > 0) {
                mDatabase.execSQL(sql, bindArgs);
            } else {
                mDatabase.execSQL(sql);
            }
        }
    }
}
