package com.mucino.project.etl.domain;

public class Product {
	
	private String product_name;
	private String description;
	private String product_code;
	private String price;
	
	public Product() {
		
	}
	
	public Product(String product_name, String description, String product_code, String price) {
		this.product_name = product_name;
		this.description = description;
		this.product_code = product_code;
		this.price = price;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

}
