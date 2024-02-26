package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 May 2023*/

public class Model_ProductHistory {

    private String id;
    private String productnumber;
    private String productname;
    private String price;
    private String available;
    private String quantityonhand;
    private String status;
    private String productimage;
    private String upccod;
    private String description;


    public Model_ProductHistory(String id,String productnumber,String productname,String price,String available,String quantityonhand,String status,String productimage,String upccode,String description) {
        this.id = id;
        this.productnumber = productnumber;
        this.productname = productname;
        this.price = price;
        this.available = available;
        this.quantityonhand = quantityonhand;
        this.status = status;
        this.productimage = productimage;
        this.upccod = upccode;
        this.description = description;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductnumber() {
        return productnumber;
    }

    public void setProductnumber(String productnumber) {
        this.productnumber = productnumber;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getQuantityonhand() {
        return quantityonhand;
    }

    public void setQuantityonhand(String quantityonhand) {
        this.quantityonhand = quantityonhand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getUpccod() {
        return upccod;
    }

    public void setUpccod(String upccod) {
        this.upccod = upccod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}