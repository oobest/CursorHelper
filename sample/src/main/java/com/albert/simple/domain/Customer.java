package com.albert.simple.domain;

import com.albert.lib_db_annotation.AptCursorWrapper;
import com.albert.lib_db_annotation.Cols;

@AptCursorWrapper
public class Customer {

    @Cols("id")
    protected int id;

    @Cols("name")
    protected String name;

    @Cols("address")
    protected String address;

    @Cols("phone")
    protected String phone;


    public Customer() {
        super();
    }


    public Customer(String name, String address, String phone) {
        super();
        this.name = name;
        this.address = address;
        this.phone = phone;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + "]";
    }


}
