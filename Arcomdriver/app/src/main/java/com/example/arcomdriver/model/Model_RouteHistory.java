package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 07 June 2023*/

public class Model_RouteHistory {

    private String routeID;
    private String routeName;
    private String routeStatus;
    private String driverName;
    private String OrdersCount;
    private String CustomersCount;
    private String driverid;
    private String truckid;

    public Model_RouteHistory(String routeID, String routeName, String routeStatus, String driverName , String OrdersCount, String CustomersCount, String driverid, String truckid) {
        this.routeID = routeID;
        this.routeName = routeName;
        this.routeStatus = routeStatus;
        this.driverName = driverName;
        this.OrdersCount = OrdersCount;
        this.CustomersCount = CustomersCount;
        this.driverid = driverid;
        this.truckid = truckid;

    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOrdersCount() {
        return OrdersCount;
    }

    public void setOrdersCount(String ordersCount) {
        OrdersCount = ordersCount;
    }

    public String getCustomersCount() {
        return CustomersCount;
    }

    public void setCustomersCount(String customersCount) {
        CustomersCount = customersCount;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getTruckid() {
        return truckid;
    }

    public void setTruckid(String truckid) {
        this.truckid = truckid;
    }


}