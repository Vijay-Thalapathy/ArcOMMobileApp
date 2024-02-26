package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 01 Apr 2023*/

public class Model_ItemReportsDetails {

    private String Rd_ordernumber;
    private String Rd_productName;
    private String Rd_ProductQty;
    public Model_ItemReportsDetails(String Rd_ordernumber,String Rd_productName, String Rd_ProductQty) {
        this.Rd_ordernumber = Rd_ordernumber;
        this.Rd_productName = Rd_productName;
        this.Rd_ProductQty = Rd_ProductQty;

    }


    public String getRd_ordernumber() {
        return Rd_ordernumber;
    }

    public void setRd_ordernumber(String rd_ordernumber) {
        Rd_ordernumber = rd_ordernumber;
    }


    public String getRd_productName() {
        return Rd_productName;
    }

    public void setRd_productName(String rd_productName) {
        Rd_productName = rd_productName;
    }

    public String getRd_ProductQty() {
        return Rd_ProductQty;
    }

    public void setRd_ProductQty(String rd_ProductQty) {
        Rd_ProductQty = rd_ProductQty;
    }


}