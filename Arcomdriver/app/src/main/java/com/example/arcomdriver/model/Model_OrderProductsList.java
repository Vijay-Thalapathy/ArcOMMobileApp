package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 Mar 2023*/

public class Model_OrderProductsList {
    private String OrderProItem_proName;
    private String OrderProItem_proQty;
    private String OrderProItem_DeliveryOn;
    private String OrderProItem_isLoaded;
    private String OrderProItem_Address;
    private String OrderProItem_OrderID;
    private String OrderProItem_ProductID;

    public Model_OrderProductsList(String OrderProItem_proName, String OrderProItem_proQty, String OrderProItem_DeliveryOn, String OrderProItem_isLoaded, String OrderProItem_Address, String OrderProItem_OrderID, String OrderProItem_ProductID) {
        this.OrderProItem_proName = OrderProItem_proName;
        this.OrderProItem_proQty = OrderProItem_proQty;
        this.OrderProItem_DeliveryOn = OrderProItem_DeliveryOn;
        this.OrderProItem_isLoaded = OrderProItem_isLoaded;
        this.OrderProItem_Address = OrderProItem_Address;
        this.OrderProItem_OrderID = OrderProItem_OrderID;
        this.OrderProItem_ProductID = OrderProItem_ProductID;
    }

    public String getOrderProItem_proName() {
        return OrderProItem_proName;
    }

    public void setOrderProItem_proName(String orderProItem_proName) {
        OrderProItem_proName = orderProItem_proName;
    }

    public String getOrderProItem_proQty() {
        return OrderProItem_proQty;
    }

    public void setOrderProItem_proQty(String orderProItem_proQty) {
        OrderProItem_proQty = orderProItem_proQty;
    }

    public String getOrderProItem_DeliveryOn() {
        return OrderProItem_DeliveryOn;
    }

    public void setOrderProItem_DeliveryOn(String orderProItem_DeliveryOn) {
        OrderProItem_DeliveryOn = orderProItem_DeliveryOn;
    }

    public String getOrderProItem_isLoaded() {
        return OrderProItem_isLoaded;
    }

    public void setOrderProItem_isLoaded(String orderProItem_isLoaded) {
        OrderProItem_isLoaded = orderProItem_isLoaded;
    }

    public String getOrderProItem_Address() {
        return OrderProItem_Address;
    }

    public void setOrderProItem_Address(String orderProItem_Address) {
        OrderProItem_Address = orderProItem_Address;
    }

    public String getOrderProItem_OrderID() {
        return OrderProItem_OrderID;
    }

    public void setOrderProItem_OrderID(String orderProItem_OrderID) {
        OrderProItem_OrderID = orderProItem_OrderID;
    }

    public String getOrderProItem_ProductID() {
        return OrderProItem_ProductID;
    }

    public void setOrderProItem_ProductID(String orderProItem_ProductID) {
        OrderProItem_ProductID = orderProItem_ProductID;
    }


}