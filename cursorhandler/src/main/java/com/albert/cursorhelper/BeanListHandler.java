package com.albert.cursorhelper;

import android.database.Cursor;

import com.albert.lib_db_annotation.Constants;
import com.albert.lib_db_annotation.IParseCursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanListHandler<T> implements CursorHandler<List<T>> {

    private final Class<? extends T> type;

    public BeanListHandler(Class<? extends T> type) {
        this.type = type;
    }

    @Override
    public List<T> handle(Cursor cursor) {
        List<T> result = null;
        if (cursor != null) {
            String cursorWrapperName = type.getName() + Constants.SUFFIX;
            try {
                Class clazz = Class.forName(cursorWrapperName);
                Constructor constructor = clazz.getConstructor(Cursor.class);
                Object object = constructor.newInstance(cursor);
                if (object instanceof IParseCursor) {
                    result = new ArrayList<>();
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        T item = (T) ((IParseCursor) object).getItem();
                        result.add(item);
                        cursor.moveToNext();
                    }
                }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
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
