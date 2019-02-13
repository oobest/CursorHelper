package com.albert.cursorhelper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScalarListHandler<T> implements CursorHandler<List<T>> {

    private Class<? extends T> type;

    public ScalarListHandler(Class<? extends T> type) {
        this.type = type;
    }
    @Override
    public List<T> handle(Cursor cursor) {
        List<T> result = null;
        if (cursor != null) {
            result = new ArrayList<>();
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
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
                    result.add((T) obj);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        }
        if(result==null){
            result = Collections.EMPTY_LIST;
        }
        return result;
    }
}
