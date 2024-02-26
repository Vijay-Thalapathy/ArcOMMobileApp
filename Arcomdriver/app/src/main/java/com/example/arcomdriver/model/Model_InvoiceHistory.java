package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Mar 2023*/

public class Model_InvoiceHistory {

    private String invoiceid;
    private String invoiceNum;
    private String invoicedate;
    private String customername;
    private String totalamount;
    private String status;

    public Model_InvoiceHistory(String invoiceid, String invoiceNum, String invoicedate, String customername,String totalamount,String status) {
        this.invoiceid = invoiceid;
        this.invoiceNum = invoiceNum;
        this.invoicedate = invoicedate;
        this.customername = customername;
        this.totalamount = totalamount;
        this.status = status;

    }

    public String getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(String invoiceid) {
        this.invoiceid = invoiceid;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}