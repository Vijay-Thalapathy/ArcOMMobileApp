package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Feb 2023*/

public class Model_AddressHistory {
    private String id_address;
    private String line2;
    private String country;
    private String city;
    private String stateorprovince;
    private String postalcode;
    private String addresstypecode;

    public Model_AddressHistory(String id_address, String line2, String country, String city,String stateorprovince,String postalcode,String addresstypecode) {
        this.id_address = id_address;
        this.line2 = line2;
        this.country = country;
        this.city = city;
        this.stateorprovince = stateorprovince;
        this.postalcode = postalcode;
        this.addresstypecode = addresstypecode;

    }

    public String getId_address() {
        return id_address;
    }

    public void setId_address(String id_address) {
        this.id_address = id_address;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateorprovince() {
        return stateorprovince;
    }

    public void setStateorprovince(String stateorprovince) {
        this.stateorprovince = stateorprovince;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getAddresstypecode() {
        return addresstypecode;
    }

    public void setAddresstypecode(String addresstypecode) {
        this.addresstypecode = addresstypecode;
    }


}