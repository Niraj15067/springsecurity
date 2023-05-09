package com.neml.springsecurity.model;

public class ProductModel {

	private int id;
	private int qty;
	private String name;
	
	public ProductModel()
	{
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProductModel(int id, int qty, String name) {
		super();
		this.id = id;
		this.qty = qty;
		this.name = name;
	}
	@Override
	public String toString() {
		return "ProductModel [id=" + id + ", qty=" + qty + ", name=" + name + "]";
	}
	
	
}
