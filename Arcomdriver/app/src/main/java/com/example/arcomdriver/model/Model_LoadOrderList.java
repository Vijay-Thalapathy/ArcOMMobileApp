package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 Apr 2023*/

public class Model_LoadOrderList {

    private String pr_orderID_;
    private String pr_orderNum;
    private String pr_orderRequired;
    private String pr_orderOntruck;
    private String pr_orderAmt;
    private String pr_orderCust;
    private String pr_orderIsLoad;
    private String pr_orderCustID;

    private String pr_warehouseid;

    public Model_LoadOrderList(String pr_orderID_, String pr_orderNum, String pr_orderRequired,
                               String pr_orderOntruck, String pr_orderAmt, String pr_orderCust, String pr_orderIsLoad, String pr_orderCustID, String pr_warehouseid) {

        this.pr_orderID_ = pr_orderID_;
        this.pr_orderNum = pr_orderNum;
        this.pr_orderRequired = pr_orderRequired;
        this.pr_orderOntruck = pr_orderOntruck;
        this.pr_orderAmt = pr_orderAmt;
        this.pr_orderCust = pr_orderCust;
        this.pr_orderIsLoad = pr_orderIsLoad;
        this.pr_orderCustID = pr_orderCustID;
        this.pr_warehouseid = pr_warehouseid;

    }
    public String getPr_orderNum() {
        return pr_orderNum;
    }

    public void setPr_orderNum(String pr_orderNum) {
        this.pr_orderNum = pr_orderNum;
    }

    public String getPr_orderRequired() {
        return pr_orderRequired;
    }

    public void setPr_orderRequired(String pr_orderRequired) {
        this.pr_orderRequired = pr_orderRequired;
    }
    public String getPr_orderOntruck() {
        return pr_orderOntruck;
    }

    public void setPr_orderOntruck(String pr_orderOntruck) {
        this.pr_orderOntruck = pr_orderOntruck;
    }
    public String getPr_orderAmt() {
        return pr_orderAmt;
    }

    public void setPr_orderAmt(String pr_orderAmt) {
        this.pr_orderAmt = pr_orderAmt;
    }
    public String getPr_orderCust() {
        return pr_orderCust;
    }

    public void setPr_orderCust(String pr_orderCust) {
        this.pr_orderCust = pr_orderCust;
    }

    public String getPr_orderID_() {
        return pr_orderID_;
    }
    public void setPr_orderID_(String pr_orderID_) {
        this.pr_orderID_ = pr_orderID_;
    }

    public String getPr_orderIsLoad() {
        return pr_orderIsLoad;
    }
    public void setPr_orderIsLoad(String pr_orderIsLoad) {
        this.pr_orderIsLoad = pr_orderIsLoad;
    }


    public String getPr_orderCustID() {
        return pr_orderCustID;
    }

    public void setPr_orderCustID(String pr_orderCustID) {
        this.pr_orderCustID = pr_orderCustID;
    }

    public String getPr_warehouseid() {
        return pr_warehouseid;
    }

    public void setPr_warehouseid(String pr_warehouseid) {
        this.pr_warehouseid = pr_warehouseid;
    }

}