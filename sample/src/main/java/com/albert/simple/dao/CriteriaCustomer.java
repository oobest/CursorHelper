package com.albert.simple.dao;

public class CriteriaCustomer {
	private String name;
	private String address;
	private String phone;
	
	

	public CriteriaCustomer(String name, String address, String phone) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	public String getName() {
		if (name == null)
			return "%%";
		else
			return "%" + name + "%";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		if (address == null)
			return "%%";
		else
			return "%" + address + "%";
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		if (phone == null)
			return "%%";
		else
			return "%" + phone + "%";
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
