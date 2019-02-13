package com.albert.cursorhelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BaseDAO<T> {

    private QueryRunner mQueryRunner;

    private Class<T> clazz;

    public BaseDAO(DatabaseHelper helper) {
        mQueryRunner = new QueryRunner(helper);
        Type supperClass = getClass().getGenericSuperclass();
        if (supperClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) supperClass;

            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs != null && typeArgs.length > 0) {
                if (typeArgs[0] instanceof Class) {
                    clazz = (Class<T>) typeArgs[0];
                }
            }
        }
    }

    public <E> E getForValue(Class<? extends E> type, String sql, String... args) {
        try {
            return mQueryRunner.query(sql, new ScalarHandler<>(type), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <E> List<E> getForValueList(Class<? extends E> type, String sql, String... args) {
        try {
            return mQueryRunner.query(sql, new ScalarListHandler<>(type), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> getForList(String sql, String... args) {
        try {
            return mQueryRunner.query(sql, new BeanListHandler<>(clazz), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T get(String sql, String... args) {
        try {
            return mQueryRunner.query(sql, new BeanHandler<>(clazz), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(String sql, String... args) {
        try {
            mQueryRunner.update(sql, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
