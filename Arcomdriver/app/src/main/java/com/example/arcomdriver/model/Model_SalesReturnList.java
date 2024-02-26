package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/

public class Model_SalesReturnList {
    private String srm_returnID;
    private String srm_returnnumber;
    private String srm_returneddate;
    private String srm_ordernumber;
    private String srm_orderinvnumber;
    private String srm_customername;
    private String srm_returnstatus;
    private String srm_totalamount;

    public Model_SalesReturnList(String srm_returnID,String srm_returnnumber, String srm_returneddate, String srm_ordernumber, String srm_orderinvnumber, String srm_customername, String srm_returnstatus, String srm_totalamount) {
        this.srm_returnID = srm_returnID;
        this.srm_returnnumber = srm_returnnumber;
        this.srm_returneddate = srm_returneddate;
        this.srm_ordernumber = srm_ordernumber;
        this.srm_orderinvnumber = srm_orderinvnumber;
        this.srm_customername = srm_customername;
        this.srm_returnstatus = srm_returnstatus;
        this.srm_totalamount = srm_totalamount;
    }

    public String getSrm_returnnumber() {
        return srm_returnnumber;
    }

    public void setSrm_returnnumber(String srm_returnnumber) {
        this.srm_returnnumber = srm_returnnumber;
    }

    public String getSrm_returneddate() {
        return srm_returneddate;
    }

    public void setSrm_returneddate(String srm_returneddate) {
        this.srm_returneddate = srm_returneddate;
    }

    public String getSrm_ordernumber() {
        return srm_ordernumber;
    }

    public void setSrm_ordernumber(String srm_ordernumber) {
        this.srm_ordernumber = srm_ordernumber;
    }

    public String getSrm_orderinvnumber() {
        return srm_orderinvnumber;
    }

    public void setSrm_orderinvnumber(String srm_orderinvnumber) {
        this.srm_orderinvnumber = srm_orderinvnumber;
    }

    public String getSrm_customername() {
        return srm_customername;
    }

    public void setSrm_customername(String srm_customername) {
        this.srm_customername = srm_customername;
    }

    public String getSrm_returnstatus() {
        return srm_returnstatus;
    }

    public void setSrm_returnstatus(String srm_returnstatus) {
        this.srm_returnstatus = srm_returnstatus;
    }

    public String getSrm_totalamount() {
        return srm_totalamount;
    }

    public void setSrm_totalamount(String srm_totalamount) {
        this.srm_totalamount = srm_totalamount;
    }

    public String getSrm_returnID() {
        return srm_returnID;
    }

    public void setSrm_returnID(String srm_returnID) {
        this.srm_returnID = srm_returnID;
    }


}