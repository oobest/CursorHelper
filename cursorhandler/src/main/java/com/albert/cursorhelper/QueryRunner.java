package com.albert.cursorhelper;

import android.database.Cursor;

public class QueryRunner {

    private DatabaseHelper mDatabaseHelper;

    public QueryRunner(DatabaseHelper databaseHelper) {
        this.mDatabaseHelper = databaseHelper;
    }

    public <T> T query(String sql, CursorHandler<T> cursorHandler, String... params) throws Exception {
        Cursor cursor = mDatabaseHelper.rawQuery(sql, params);
        return cursorHandler.handle(cursor);
    }

    public void update(String sql, String... bindArgs) {
        mDatabaseHelper.execSQL(sql, bindArgs);
    }
}
