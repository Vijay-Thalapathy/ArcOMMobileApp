package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Apr 2023*/

public class Model_OfflineList {

	private boolean isSelected;
	private String OrderId;
	private String OrderNum;
	private String submitdate;
	private String UploadStatus;
	private String draftnumber;
	private String Flag;
	private String order_name;

	public Model_OfflineList(boolean isSelected, String OrderId,String OrderNum,String submitdate,String UploadStatus,String draftnumber,String Flag,String order_name) {

		this.isSelected = isSelected;
		this.OrderId = OrderId;
		this.OrderNum = OrderNum;
		this.submitdate = submitdate;
		this.UploadStatus = UploadStatus;
		this.draftnumber = draftnumber;
		this.Flag = Flag;
		this.order_name = order_name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}

	public String getSubmitdate() {
		return submitdate;
	}

	public void setSubmitdate(String submitdate) {
		this.submitdate = submitdate;
	}

	public String getUploadStatus() {
		return UploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		UploadStatus = uploadStatus;
	}

	public String getDraftnumber() {
		return draftnumber;
	}

	public void setDraftnumber(String draftnumber) {
		this.draftnumber = draftnumber;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getOrder_name() {
		return order_name;
	}

	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}

}
