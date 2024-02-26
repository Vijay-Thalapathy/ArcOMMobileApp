package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Mar 2023*/

public class Model_SalesItemList {

    private String product_id;
    private String product_name;
    private String product_imageurl;
    private String product_quantity;
    private String product_price;
    private String Price_PerUnit;
    private String Order_productid;
    private String Istaxable;
    private String UpscCode;
    private String all_description;
    private String priceTag;
    private String ReturnReason;

    private String defaultQTy;


    public Model_SalesItemList(String product_id, String product_name, String product_imageurl, String product_quantity, String product_price, String Price_PerUnit, String Order_productid, String Istaxable, String UpscCode, String all_description, String priceTag, String ReturnReason, String defaultQTy) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_imageurl = product_imageurl;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.Price_PerUnit = Price_PerUnit;
        this.Order_productid = Order_productid;
        this.Istaxable = Istaxable;
        this.UpscCode = UpscCode;
        this.all_description = all_description;
        this.priceTag = priceTag;
        this.ReturnReason = ReturnReason;
        this.defaultQTy = defaultQTy;

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

    public String getPrice_PerUnit() {
        return Price_PerUnit;
    }

    public void setPrice_PerUnit(String price_PerUnit) {
        Price_PerUnit = price_PerUnit;
    }

    public String getOrder_productid() {
        return Order_productid;
    }

    public void setOrder_productid(String order_productid) {
        Order_productid = order_productid;
    }


    public String getIstaxable() {
        return Istaxable;
    }

    public void setIstaxable(String istaxable) {
        Istaxable = istaxable;
    }

    public String getUpscCode() {
        return UpscCode;
    }

    public void setUpscCode(String upscCode) {
        UpscCode = upscCode;
    }


    public String getAll_description() {
        return all_description;
    }

    public void setAll_description(String all_description) {
        this.all_description = all_description;
    }

    public String getPriceTag() {
        return priceTag;
    }

    public void setPriceTag(String priceTag) {
        this.priceTag = priceTag;
    }

    public String getReturnReason() {
        return ReturnReason;
    }

    public void setReturnReason(String returnReason) {
        ReturnReason = returnReason;
    }

    public String getDefaultQTy() {
        return defaultQTy;
    }

    public void setDefaultQTy(String defaultQTy) {
        this.defaultQTy = defaultQTy;
    }

}