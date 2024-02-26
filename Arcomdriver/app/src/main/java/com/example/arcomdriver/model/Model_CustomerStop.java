package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 Feb 2023*/

public class Model_CustomerStop {

    private String st_count;
    private String st_name;
    private String st_address;

    private String st_Duration_hours;
    private String st_sequence;

    private String st_type;
    private String st_stopID;

    public Model_CustomerStop(String st_count, String st_name, String st_address, String st_Duration_hours, String st_sequence, String st_type, String st_stopID) {
        this.st_count = st_count;
        this.st_name = st_name;
        this.st_address = st_address;
        this.st_Duration_hours = st_Duration_hours;
        this.st_sequence = st_sequence;
        this.st_type = st_type;
        this.st_stopID = st_stopID;

    }

    public String getSt_count() {
        return st_count;
    }

    public void setSt_count(String st_count) {
        this.st_count = st_count;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public String getSt_address() {
        return st_address;
    }

    public void setSt_address(String st_address) {
        this.st_address = st_address;
    }


    public String getSt_Duration_hours() {
        return st_Duration_hours;
    }

    public void setSt_Duration_hours(String st_Duration_hours) {
        this.st_Duration_hours = st_Duration_hours;
    }


    public String getSt_sequence() {
        return st_sequence;
    }

    public void setSt_sequence(String st_sequence) {
        this.st_sequence = st_sequence;
    }


    public String getSt_type() {
        return st_type;
    }

    public void setSt_type(String st_type) {
        this.st_type = st_type;
    }

    public String getSt_stopID() {
        return st_stopID;
    }

    public void setSt_stopID(String st_stopID) {
        this.st_stopID = st_stopID;
    }

}