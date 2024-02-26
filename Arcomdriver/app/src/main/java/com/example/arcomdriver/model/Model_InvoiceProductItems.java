package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Mar 2023*/

public class Model_InvoiceProductItems {

    private String product_name;
    private String product_quantity;
    private String product_price;
    private String Price_PerUnit;
    private String itemistaxable;
    private String all_upsc;
    private String all_description;

    public Model_InvoiceProductItems(String product_name, String product_quantity, String product_price,String Price_PerUnit,String itemistaxable,String all_upsc,String all_description) {

        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.Price_PerUnit = Price_PerUnit;
        this.itemistaxable = itemistaxable;
        this.all_upsc = all_upsc;
        this.all_description = all_description;

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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


    public String getItemistaxable() {
        return itemistaxable;
    }

    public void setItemistaxable(String itemistaxable) {
        this.itemistaxable = itemistaxable;
    }


    public String getAll_upsc() {
        return all_upsc;
    }

    public void setAll_upsc(String all_upsc) {
        this.all_upsc = all_upsc;
    }


    public String getAll_description() {
        return all_description;
    }

    public void setAll_description(String all_description) {
        this.all_description = all_description;
    }

}