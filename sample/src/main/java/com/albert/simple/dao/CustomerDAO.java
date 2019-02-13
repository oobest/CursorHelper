package com.albert.simple.dao;

import com.albert.simple.domain.Customer;

import java.util.List;

public interface CustomerDAO {

    List<Customer> getAll();

    List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc);

    void save(Customer customer);

    Customer get(Integer id);

    void delete(Integer id);

    /**
     * @param name
     * @return
     */
    long getCountWidthName(String name);

    void update(Customer customer);
}
