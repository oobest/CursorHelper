package com.albert.cursorhelper;

import android.database.Cursor;

/**
 * 基础数据类型
 *
 * @param <T>
 */
public class ScalarHandler<T> implements CursorHandler<T> {

    private Class<? extends T> type;

    public ScalarHandler(Class<? extends T> type) {
        this.type = type;
    }

    @Override
    public T handle(Cursor cursor) {
        T result = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    Object obj = null;
                    if (type.equals(String.class)) {
                        obj = cursor.getString(0);
                    } else if (type.equals(Integer.class)) {
                        obj = cursor.getInt(0);
                    } else if (type.equals(Long.class)) {
                        obj = cursor.getLong(0);
                    } else if (type.equals(Float.class)) {
                        obj = cursor.getFloat(0);
                    } else if (type.equals(Double.class)) {
                        obj = cursor.getDouble(0);
                    }
                    result = (T) obj;
                }
            } finally {
                cursor.close();
            }
        }
        return result;
    }
}
