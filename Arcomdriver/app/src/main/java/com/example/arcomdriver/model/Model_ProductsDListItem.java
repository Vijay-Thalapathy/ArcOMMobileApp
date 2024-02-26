package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 Mar 2023*/

public class Model_ProductsDListItem {
    private String el_proName;
    private String el_proQty;
    private String el_DeliveryOn;
    private String el_isLoaded;
    private String el_Address;
    private String el_OID;
    private String el_productID;
    private String el_totalAmount;

    public Model_ProductsDListItem(String el_proName, String el_proQty, String el_DeliveryOn, String el_isLoaded, String el_Address, String el_OID, String el_productID, String el_totalAmount) {
        this.el_proName = el_proName;
        this.el_proQty = el_proQty;
        this.el_DeliveryOn = el_DeliveryOn;
        this.el_isLoaded = el_isLoaded;
        this.el_Address = el_Address;
        this.el_OID = el_OID;
        this.el_productID = el_productID;
        this.el_totalAmount = el_totalAmount;
    }

    public String getEl_proName() {
        return el_proName;
    }

    public void setEl_proName(String el_proName) {
        this.el_proName = el_proName;
    }

    public String getEl_proQty() {
        return el_proQty;
    }

    public void setEl_proQty(String el_proQty) {
        this.el_proQty = el_proQty;
    }

    public String getEl_DeliveryOn() {
        return el_DeliveryOn;
    }

    public void setEl_DeliveryOn(String el_DeliveryOn) {
        this.el_DeliveryOn = el_DeliveryOn;
    }

    public String getEl_isLoaded() {
        return el_isLoaded;
    }

    public void setEl_isLoaded(String el_isLoaded) {
        this.el_isLoaded = el_isLoaded;
    }

    public String getEl_Address() {
        return el_Address;
    }

    public void setEl_Address(String el_Address) {
        this.el_Address = el_Address;
    }

    public String getEl_OID() {
        return el_OID;
    }

    public void setEl_OID(String el_OID) {
        this.el_OID = el_OID;
    }

    public String getEl_productID() {
        return el_productID;
    }

    public void setEl_productID(String el_productID) {
        this.el_productID = el_productID;
    }

    public String getEl_totalAmount() {
        return el_totalAmount;
    }

    public void setEl_totalAmount(String el_totalAmount) {
        this.el_totalAmount = el_totalAmount;
    }




}