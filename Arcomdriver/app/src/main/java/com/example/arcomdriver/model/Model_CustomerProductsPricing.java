package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Mar 2023*/

public class Model_CustomerProductsPricing {

    private String pricename;
    private String productid;
    private String customerprice;

    public Model_CustomerProductsPricing(String pricename, String productid, String customerprice) {
        this.pricename = pricename;
        this.productid = productid;
        this.customerprice = customerprice;


    }


    public String getPricename() {
        return pricename;
    }

    public void setPricename(String pricename) {
        this.pricename = pricename;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getCustomerprice() {
        return customerprice;
    }

    public void setCustomerprice(String customerprice) {
        this.customerprice = customerprice;
    }

}