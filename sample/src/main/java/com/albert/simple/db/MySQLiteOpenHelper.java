package com.albert.simple.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLiteOpenHelper";
    private static final String DATABASE_NAME = "my_store.db";//数据库名字
    private static final int DATABASE_VERSION = 1;//数据库版本号
    private static final String CREATE_TABLE = "create table customers ("
            + "id integer primary key autoincrement,"
            + "name text, "
            + "address text, "
            + "phone text)";//数据库里的表

    public MySQLiteOpenHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//调用到SQLiteOpenHelper中
        Log.d(TAG, "New CustomSQLiteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
