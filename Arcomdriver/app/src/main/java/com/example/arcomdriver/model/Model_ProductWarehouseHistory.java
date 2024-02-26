package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 May 2023*/

public class Model_ProductWarehouseHistory {

    private String LocationType;
    private String Location;
    private String Onhand;
    private String Committed;
    private String Available;
    private String Status;


    public Model_ProductWarehouseHistory(String LocationType, String Location, String Onhand, String Committed, String Available, String Status) {
        this.LocationType = LocationType;
        this.Location = Location;
        this.Onhand = Onhand;
        this.Committed = Committed;
        this.Available = Available;
        this.Status = Status;

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}