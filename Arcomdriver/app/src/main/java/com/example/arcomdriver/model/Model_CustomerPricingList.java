package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/

public class Model_CustomerPricingList {

    private String pricing_id;
    private String pricing_name;
    private String pricing_startdatetime;
    private String pricing_enddatetime;
    private String pricing_productcountlist;
    private String pricing_customercountlist;
    private String pricing_isactive;

    public Model_CustomerPricingList(String pricing_id, String pricing_name, String pricing_startdatetime, String pricing_enddatetime, String pricing_productcountlist, String pricing_customercountlist, String pricing_isactive) {
        this.pricing_id = pricing_id;
        this.pricing_name = pricing_name;
        this.pricing_startdatetime = pricing_startdatetime;
        this.pricing_enddatetime = pricing_enddatetime;
        this.pricing_productcountlist = pricing_productcountlist;
        this.pricing_customercountlist = pricing_customercountlist;
        this.pricing_isactive = pricing_isactive;
    }

    public String getPricing_id() {
        return pricing_id;
    }

    public void setPricing_id(String pricing_id) {
        this.pricing_id = pricing_id;
    }

    public String getPricing_name() {
        return pricing_name;
    }

    public void setPricing_name(String pricing_name) {
        this.pricing_name = pricing_name;
    }

    public String getPricing_startdatetime() {
        return pricing_startdatetime;
    }

    public void setPricing_startdatetime(String pricing_startdatetime) {
        this.pricing_startdatetime = pricing_startdatetime;
    }

    public String getPricing_enddatetime() {
        return pricing_enddatetime;
    }

    public void setPricing_enddatetime(String pricing_enddatetime) {
        this.pricing_enddatetime = pricing_enddatetime;
    }

    public String getPricing_productcountlist() {
        return pricing_productcountlist;
    }

    public void setPricing_productcountlist(String pricing_productcountlist) {
        this.pricing_productcountlist = pricing_productcountlist;
    }

    public String getPricing_customercountlist() {
        return pricing_customercountlist;
    }

    public void setPricing_customercountlist(String pricing_customercountlist) {
        this.pricing_customercountlist = pricing_customercountlist;
    }

    public String getPricing_isactive() {
        return pricing_isactive;
    }

    public void setPricing_isactive(String pricing_isactive) {
        this.pricing_isactive = pricing_isactive;
    }


}