package com.albert.simple.dao.impl;


import android.content.Context;

import com.albert.cursorhelper.BaseDAO;
import com.albert.simple.dao.CriteriaCustomer;
import com.albert.simple.dao.CustomerDAO;
import com.albert.simple.db.MyDatabaseHelper;
import com.albert.simple.domain.Customer;

import java.util.List;

public class CustomerDAODBImpl extends BaseDAO<Customer> implements CustomerDAO {

    public CustomerDAODBImpl(Context context) {
        super(MyDatabaseHelper.getInstance(context));
    }

    @Override
    public List<Customer> getAll() {
        String sql = "SELECT id, name, address, phone From customers";
        return getForList(sql);
    }

    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO customers(name,address,phone) VALUES(?,?,?)";
        long count = getCountWidthName(customer.getName());
        if (count == 0) {
            update(sql, customer.getName(), customer.getAddress(), customer.getPhone());
        }
    }

    @Override
    public Customer get(Integer id) {
        String sql = "SELECT id, name, address, phone From customers WHERE id = ?";
        return get(sql, String.valueOf(id));
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM customers WHERE id=?";
        update(sql, String.valueOf(id));
    }

    @Override
    public long getCountWidthName(String name) {
        String sql = "SELECT count(name) From customers WHERE name = ?";
        Long l = getForValue(Long.class, sql, name);
        return l == null ? 0 : l;
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET name=?, address=?, phone=? WHERE id=?";
        update(sql, customer.getName(), customer.getAddress(), customer.getPhone(),
                String.valueOf(customer.getId()));
    }

    @Override
    public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc) {
        String sql = "SELECT id, name, address, phone FROM customers WHERE "
                + "name LIKE ? and address LIKE ? and phone LIKE ?";
        return getForList(sql, cc.getName(), cc.getAddress(), cc.getPhone());
    }


}
