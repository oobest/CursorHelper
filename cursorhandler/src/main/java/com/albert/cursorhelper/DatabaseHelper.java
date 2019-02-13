package com.albert.cursorhelper;

import android.database.Cursor;

public interface DatabaseHelper {

    Cursor rawQuery(String sql, String... selectionArgs);

    void execSQL(String sql, String... bindArgs);
}
