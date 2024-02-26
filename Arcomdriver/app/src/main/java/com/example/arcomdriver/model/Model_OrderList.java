package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 May 2023*/

public class Model_OrderList {

    private String Order_id;
    private String Order_name;
    private String Order_number;
    private String Order_status;
    private String Order_date;
    private String Order_amount;

    private String SyncStatus;
    private String netStatus;


    public Model_OrderList(String Order_id, String Order_name, String Order_number, String Order_status, String Order_date, String Order_amount, String SyncStatus, String netStatus) {
        this.Order_id = Order_id;
        this.Order_name = Order_name;
        this.Order_number = Order_number;
        this.Order_status = Order_status;
        this.Order_date = Order_date;
        this.Order_amount = Order_amount;
        this.SyncStatus = SyncStatus;
        this.netStatus = netStatus;

    }

    public String getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(String order_id) {
        Order_id = order_id;
    }

    public String getOrder_name() {
        return Order_name;
    }

    public void setOrder_name(String order_name) {
        Order_name = order_name;
    }

    public String getOrder_number() {
        return Order_number;
    }

    public void setOrder_number(String order_number) {
        Order_number = order_number;
    }

    public String getOrder_status() {
        return Order_status;
    }

    public void setOrder_status(String order_status) {
        Order_status = order_status;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String order_date) {
        Order_date = order_date;
    }

    public String getOrder_amount() {
        return Order_amount;
    }

    public void setOrder_amount(String order_amount) {
        Order_amount = order_amount;
    }

    public String getSyncStatus() {
        return SyncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        SyncStatus = syncStatus;
    }

    public String getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(String netStatus) {
        this.netStatus = netStatus;
    }

}