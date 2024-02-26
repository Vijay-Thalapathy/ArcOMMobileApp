package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 June 2023*/

public class Model_SummaryList {

    private String product_id;
    private String product_name;
    private String product_imageurl;
    private String product_quantity;
    private String product_price;
    private String TotalItem_price;
    private String Price_PerUnit;
    private String istaxable;
    private String all_upsc;

    private String all_dec;


    public Model_SummaryList(String product_id, String product_name, String product_imageurl, String product_quantity, String product_price, String Total_price, String Price_PerUnit,String istaxable,String all_upsc,String all_dec) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_imageurl = product_imageurl;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.TotalItem_price = Total_price;
        this.Price_PerUnit = Price_PerUnit;
        this.istaxable = istaxable;
        this.all_upsc = all_upsc;
        this.all_dec = all_dec;

    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_imageurl() {
        return product_imageurl;
    }

    public void setProduct_imageurl(String product_imageurl) {
        this.product_imageurl = product_imageurl;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }


    public String getTotalItem_price() {
        return TotalItem_price;
    }

    public void setTotalItem_price(String totalItem_price) {
        TotalItem_price = totalItem_price;
    }

    public String getPrice_PerUnit() {
        return Price_PerUnit;
    }

    public void setPrice_PerUnit(String price_PerUnit) {
        Price_PerUnit = price_PerUnit;
    }


    public String getIstaxable() {
        return istaxable;
    }

    public void setIstaxable(String istaxable) {
        this.istaxable = istaxable;
    }

    public String getAll_upsc() {
        return all_upsc;
    }

    public void setAll_upsc(String all_upsc) {
        this.all_upsc = all_upsc;
    }

    public String getAll_dec() {
        return all_dec;
    }

    public void setAll_dec(String all_dec) {
        this.all_dec = all_dec;
    }

}