package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 June 2023*/

public class Model_RptListItem {

    private String rpt_name;
    private String rpt_qty;

    public Model_RptListItem(String rpt_name, String rpt_qty) {
        this.rpt_name = rpt_name;
        this.rpt_qty = rpt_qty;
    }


    public String getRpt_name() {
        return rpt_name;
    }

    public void setRpt_name(String rpt_name) {
        this.rpt_name = rpt_name;
    }

    public String getRpt_qty() {
        return rpt_qty;
    }

    public void setRpt_qty(String rpt_qty) {
        this.rpt_qty = rpt_qty;
    }



}