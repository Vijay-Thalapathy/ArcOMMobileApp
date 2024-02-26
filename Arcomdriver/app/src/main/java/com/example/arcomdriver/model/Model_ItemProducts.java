package com.example.arcomdriver.model;

import java.io.Serializable;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 Mar 2023*/

public class Model_ItemProducts implements Serializable {

	private boolean isSelected;
	private String product_id;
	private String product_name;
	private String product_imageurl;
	private String product_quantity;
	private String product_price;
	private String TotalItem_price;
	private String Price_PerUnit;
	private String Order_productid;
	private String istaxable;
	private String Upsc_code;
	private String Description;

	public Model_ItemProducts( boolean isSelected,String product_id, String product_name, String product_imageurl,String product_quantity,String product_price,String Total_price,String Price_PerUnit,String Order_productid,String istaxable,String Upsc_code,String Description) {


		this.isSelected = isSelected;
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_imageurl = product_imageurl;
		this.product_quantity = product_quantity;
		this.product_price = product_price;
		this.TotalItem_price = Total_price;
		this.Price_PerUnit = Price_PerUnit;
		this.Order_productid = Order_productid;
		this.istaxable = istaxable;
		this.Upsc_code = Upsc_code;
		this.Description = Description;
	}



	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}


	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_imageurl() {
		return product_imageurl;
	}

	public void setProduct_imageurl(String product_imageurl) {
		this.product_imageurl = product_imageurl;
	}

	public String getProduct_quantity() {
		return product_quantity;
	}

	public void setProduct_quantity(String product_quantity) {
		this.product_quantity = product_quantity;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}


	public String getTotalItem_price() {
		return TotalItem_price;
	}

	public void setTotalItem_price(String totalItem_price) {
		TotalItem_price = totalItem_price;
	}

	public String getPrice_PerUnit() {
		return Price_PerUnit;
	}

	public void setPrice_PerUnit(String price_PerUnit) {
		Price_PerUnit = price_PerUnit;
	}

	public String getOrder_productid() {
		return Order_productid;
	}

	public void setOrder_productid(String order_productid) {
		Order_productid = order_productid;
	}


	public String getIstaxable() {
		return istaxable;
	}

	public void setIstaxable(String istaxable) {
		this.istaxable = istaxable;
	}


	public String getUpsc_code() {
		return Upsc_code;
	}

	public void setUpsc_code(String upsc_code) {
		Upsc_code = upsc_code;
	}


	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}


}
