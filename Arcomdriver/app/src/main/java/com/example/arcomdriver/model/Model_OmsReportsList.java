package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 28 Apr 2023*/

public class Model_OmsReportsList {

    private String Reports_id;
    private String Reports_name;

    public Model_OmsReportsList(String Reports_id, String Reports_name) {
        this.Reports_id = Reports_id;
        this.Reports_name = Reports_name;
    }

    public String getReports_id() {
        return Reports_id;
    }

    public void setReports_id(String reports_id) {
        Reports_id = reports_id;
    }

    public String getReports_name() {
        return Reports_name;
    }

    public void setReports_name(String reports_name) {
        Reports_name = reports_name;
    }



}