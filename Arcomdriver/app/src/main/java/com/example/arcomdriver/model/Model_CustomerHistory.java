package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Feb 2023*/

public class Model_CustomerHistory {

    private String customerId;
    private String customernumber;
    private String customername;
    private String status;
    private String industry;

    public Model_CustomerHistory(String customerId, String customernumber, String customername, String status , String industry) {
        this.customerId = customerId;
        this.customernumber = customernumber;
        this.customername = customername;
        this.status = status;
        this.industry = industry;

    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomernumber() {
        return customernumber;
    }

    public void setCustomernumber(String customernumber) {
        this.customernumber = customernumber;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }


}