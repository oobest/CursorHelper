package com.albert.cursorhelper;

import android.database.Cursor;

import com.albert.lib_db_annotation.Constants;
import com.albert.lib_db_annotation.IParseCursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanHandler<T> implements CursorHandler<T> {

    private final Class<? extends T> type;

    public BeanHandler(Class<? extends T> type) {
        this.type = type;
    }

    @Override
    public T handle(Cursor cursor) {
        T result = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String cursorWrapperName = type.getName() + Constants.SUFFIX;
                try {
                    Class clazz = Class.forName(cursorWrapperName);
                    Constructor constructor = clazz.getConstructor(Cursor.class);
                    Object object = constructor.newInstance(cursor);
                    if (object instanceof IParseCursor) {
                        result = (T) ((IParseCursor) object).getItem();
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }
            }
        }
        return result;
    }
}
