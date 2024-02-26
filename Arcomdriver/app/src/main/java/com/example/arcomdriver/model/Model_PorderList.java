package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 08 May 2023*/

public class Model_PorderList {

    private String pOrderID;
    private String pOrderNum;
    private String pOrderQty;
    private String pOrderAmt;
    private String pOrderDelivery;

    public Model_PorderList(String pOrderID,String pOrderNum, String pOrderQty, String pOrderAmt, String pOrderDelivery) {
        this.pOrderID = pOrderID;
        this.pOrderNum = pOrderNum;
        this.pOrderQty = pOrderQty;
        this.pOrderAmt = pOrderAmt;
        this.pOrderDelivery = pOrderDelivery;

    }

    public String getpOrderNum() {
        return pOrderNum;
    }

    public void setpOrderNum(String pOrderNum) {
        this.pOrderNum = pOrderNum;
    }

    public String getpOrderQty() {
        return pOrderQty;
    }

    public void setpOrderQty(String pOrderQty) {
        this.pOrderQty = pOrderQty;
    }

    public String getpOrderAmt() {
        return pOrderAmt;
    }

    public void setpOrderAmt(String pOrderAmt) {
        this.pOrderAmt = pOrderAmt;
    }

    public String getpOrderDelivery() {
        return pOrderDelivery;
    }

    public void setpOrderDelivery(String pOrderDelivery) {
        this.pOrderDelivery = pOrderDelivery;
    }

    public String getpOrderID() {
        return pOrderID;
    }

    public void setpOrderID(String pOrderID) {
        this.pOrderID = pOrderID;
    }




}