package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 June 2023*/

public class Model_StopList {

    private String st_count;
    private String st_name;
    private String st_address;

    private String st_stopID;

    private String st_sequence;

    private String st_type;

    private String st_routeDayID;

    public Model_StopList(String st_count, String st_name, String st_address, String st_stopID, String st_sequence, String st_type, String st_routeDayID) {
        this.st_count = st_count;
        this.st_name = st_name;
        this.st_address = st_address;
        this.st_stopID = st_stopID;
        this.st_sequence = st_sequence;
        this.st_type = st_type;
        this.st_routeDayID = st_routeDayID;

    }

    public String getSt_stopID() {
        return st_stopID;
    }

    public void setSt_stopID(String st_stopID) {
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

    public String getSt_routeDayID() {
        return st_routeDayID;
    }

    public void setSt_routeDayID(String st_routeDayID) {
        this.st_routeDayID = st_routeDayID;
    }

}