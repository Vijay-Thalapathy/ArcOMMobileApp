package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Feb 2023*/

public class Model_AddMultiWarehouseItem {

    private String WarehouseName;
    private String WarehouseQty;

    public Model_AddMultiWarehouseItem(String WarehouseName, String WarehouseQty) {
        this.WarehouseName = WarehouseName;
        this.WarehouseQty = WarehouseQty;

    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    public String getWarehouseQty() {
        return WarehouseQty;
    }

    public void setWarehouseQty(String warehouseQty) {
        WarehouseQty = warehouseQty;
    }


}