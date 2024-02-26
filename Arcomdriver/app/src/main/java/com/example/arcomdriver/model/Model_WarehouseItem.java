package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 June 2023*/

public class Model_WarehouseItem {

    private String Id;
    private String LocationType;
    private String Location;
    private String Onhand;
    private String Committed;
    private String Available;
    private String Stock;
    private String LocationTypeID;
    private String WarehouseID;


    public Model_WarehouseItem(String Id,String LocationType,String  Location,String  Onhand,String Committed,String Available,String Stock,String LocationTypeID,String WarehouseID) {
        this.Id = Id;
        this.LocationType = LocationType;
        this.Location = Location;
        this.Onhand = Onhand;
        this.Committed = Committed;
        this.Available = Available;
        this.Stock = Stock;
        this.LocationTypeID = LocationTypeID;
        this.WarehouseID = WarehouseID;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getOnhand() {
        return Onhand;
    }

    public void setOnhand(String onhand) {
        Onhand = onhand;
    }

    public String getCommitted() {
        return Committed;
    }

    public void setCommitted(String committed) {
        Committed = committed;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getLocationTypeID() {
        return LocationTypeID;
    }

    public void setLocationTypeID(String locationTypeID) {
        LocationTypeID = locationTypeID;
    }


    public String getWarehouseID() {
        return WarehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        WarehouseID = warehouseID;
    }

}