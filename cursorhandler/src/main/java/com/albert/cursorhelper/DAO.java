package com.albert.cursorhelper;

import java.util.List;

public class DAO {
    private QueryRunner mQueryRunner;

    public DAO(DatabaseHelper helper) {
        mQueryRunner = new QueryRunner(helper);
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

    public <T> List<T> getForBeanList(Class<T> clazz, String sql, String... args) {
        try {
            return mQueryRunner.query(sql, new BeanListHandler<>(clazz), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getBean(Class<T> clazz, String sql, String... args) {
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
