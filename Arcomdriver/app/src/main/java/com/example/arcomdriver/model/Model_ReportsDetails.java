package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 02 June 2023*/

public class Model_ReportsDetails {

    private String Rd_id;
    private String Rd_date;
    private String Rd_customerName;
    private String Rd_amt;
    private String Rd_status;

    public Model_ReportsDetails(String Rd_id, String Rd_date, String Rd_customerName, String Rd_amt, String Rd_status) {
        this.Rd_id = Rd_id;
        this.Rd_date = Rd_date;
        this.Rd_customerName = Rd_customerName;
        this.Rd_amt = Rd_amt;
        this.Rd_status = Rd_status;
    }

    public String getRd_id() {
        return Rd_id;
    }

    public void setRd_id(String rd_id) {
        Rd_id = rd_id;
    }

    public String getRd_date() {
        return Rd_date;
    }

    public void setRd_date(String rd_date) {
        Rd_date = rd_date;
    }

    public String getRd_customerName() {
        return Rd_customerName;
    }

    public void setRd_customerName(String rd_customerName) {
        Rd_customerName = rd_customerName;
    }

    public String getRd_amt() {
        return Rd_amt;
    }

    public void setRd_amt(String rd_amt) {
        Rd_amt = rd_amt;
    }

    public String getRd_status() {
        return Rd_status;
    }

    public void setRd_status(String rd_status) {
        Rd_status = rd_status;
    }


}