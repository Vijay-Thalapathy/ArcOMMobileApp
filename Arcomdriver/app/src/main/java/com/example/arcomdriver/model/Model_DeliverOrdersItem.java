package com.example.arcomdriver.model;

import java.io.Serializable;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 Mar 2023*/

public class Model_DeliverOrdersItem  implements Serializable {


	private String isSelected;
	private String do_OrderID;
	private String do_OrderNumber;
	private String do_OrderDate;
	private String do_OrderCustomer;
	private String do_OrderAmt;
	private String do_OrderStatus;

	private String do_Products;


	public Model_DeliverOrdersItem(String isSelected, String do_OrderID, String do_OrderNumber, String do_OrderDate, String do_OrderCustomer, String do_OrderAmt, String do_OrderStatus, String do_Products) {


		this.isSelected = isSelected;
		this.do_OrderID = do_OrderID;
		this.do_OrderNumber = do_OrderNumber;
		this.do_OrderDate = do_OrderDate;
		this.do_OrderCustomer = do_OrderCustomer;
		this.do_OrderAmt = do_OrderAmt;
		this.do_OrderStatus = do_OrderStatus;
		this.do_Products = do_Products;

	}


	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}


	public String getDo_OrderID() {
		return do_OrderID;
	}

	public void setDo_OrderID(String do_OrderID) {
		this.do_OrderID = do_OrderID;
	}

	public String getDo_OrderNumber() {
		return do_OrderNumber;
	}

	public void setDo_OrderNumber(String do_OrderNumber) {
		this.do_OrderNumber = do_OrderNumber;
	}

	public String getDo_OrderDate() {
		return do_OrderDate;
	}

	public void setDo_OrderDate(String do_OrderDate) {
		this.do_OrderDate = do_OrderDate;
	}

	public String getDo_OrderCustomer() {
		return do_OrderCustomer;
	}

	public void setDo_OrderCustomer(String do_OrderCustomer) {
		this.do_OrderCustomer = do_OrderCustomer;
	}

	public String getDo_OrderAmt() {
		return do_OrderAmt;
	}

	public void setDo_OrderAmt(String do_OrderAmt) {
		this.do_OrderAmt = do_OrderAmt;
	}

	public String getDo_OrderStatus() {
		return do_OrderStatus;
	}

	public void setDo_OrderStatus(String do_OrderStatus) {
		this.do_OrderStatus = do_OrderStatus;
	}

	public String getDo_Products() {
		return do_Products;
	}

	public void setDo_Products(String do_Products) {
		this.do_Products = do_Products;
	}
}
