package com.example.arcomdriver.model;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Feb 2023*/

public class Model_ContactList {
    private String Id;
    private String IsPrimary;
    private String FirstNm;
    private String LastNm;
    private String JobTittle;
    private String PEmail;
    private String PContact;
    private String WrkNum;
    private String HNum;
    private String DCAddress;
    private String DCCountry;
    private String DCState;
    private String DCCity;
    private String DCPinCode;
    private String BCAddress;
    private String BCCountry;
    private String BCState;
    private String BCCity;
    private String BCPinCode;


    public Model_ContactList(String Id,String IsPrimary, String FirstNm, String  LastNm, String  JobTittle,
                             String PEmail, String PContact, String WrkNum, String HNum,
                             String DCAddress, String DCCountry, String DCState, String DCCity,
                             String DCPinCode, String BCAddress, String BCCountry, String BCState, String BCCity, String BCPinCode) {
        this.Id = Id;
        this.IsPrimary = IsPrimary;
        this.FirstNm = FirstNm;
        this.LastNm = LastNm;
        this.JobTittle = JobTittle;
        this.PEmail = PEmail;
        this.PContact = PContact;
        this.WrkNum = WrkNum;
        this.HNum = HNum;
        this.DCAddress = DCAddress;
        this.DCCountry = DCCountry;
        this.DCState = DCState;
        this.DCCity = DCCity;
        this.DCPinCode = DCPinCode;
        this.BCAddress = BCAddress;
        this.BCCountry = BCCountry;
        this.BCState = BCState;
        this.BCCity = BCCity;
        this.BCPinCode = BCPinCode;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getIsPrimary() {
        return IsPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        IsPrimary = isPrimary;
    }

    public String getFirstNm() {
        return FirstNm;
    }

    public void setFirstNm(String firstNm) {
        FirstNm = firstNm;
    }

    public String getLastNm() {
        return LastNm;
    }

    public void setLastNm(String lastNm) {
        LastNm = lastNm;
    }

    public String getJobTittle() {
        return JobTittle;
    }

    public void setJobTittle(String jobTittle) {
        JobTittle = jobTittle;
    }

    public String getPEmail() {
        return PEmail;
    }

    public void setPEmail(String PEmail) {
        this.PEmail = PEmail;
    }

    public String getPContact() {
        return PContact;
    }

    public void setPContact(String PContact) {
        this.PContact = PContact;
    }

    public String getWrkNum() {
        return WrkNum;
    }

    public void setWrkNum(String wrkNum) {
        WrkNum = wrkNum;
    }

    public String getHNum() {
        return HNum;
    }

    public void setHNum(String HNum) {
        this.HNum = HNum;
    }

    public String getDCAddress() {
        return DCAddress;
    }

    public void setDCAddress(String DCAddress) {
        this.DCAddress = DCAddress;
    }

    public String getDCCountry() {
        return DCCountry;
    }

    public void setDCCountry(String DCCountry) {
        this.DCCountry = DCCountry;
    }

    public String getDCState() {
        return DCState;
    }

    public void setDCState(String DCState) {
        this.DCState = DCState;
    }

    public String getDCCity() {
        return DCCity;
    }

    public void setDCCity(String DCCity) {
        this.DCCity = DCCity;
    }

    public String getDCPinCode() {
        return DCPinCode;
    }

    public void setDCPinCode(String DCPinCode) {
        this.DCPinCode = DCPinCode;
    }

    public String getBCAddress() {
        return BCAddress;
    }

    public void setBCAddress(String BCAddress) {
        this.BCAddress = BCAddress;
    }

    public String getBCCountry() {
        return BCCountry;
    }

    public void setBCCountry(String BCCountry) {
        this.BCCountry = BCCountry;
    }

    public String getBCState() {
        return BCState;
    }

    public void setBCState(String BCState) {
        this.BCState = BCState;
    }

    public String getBCCity() {
        return BCCity;
    }

    public void setBCCity(String BCCity) {
        this.BCCity = BCCity;
    }

    public String getBCPinCode() {
        return BCPinCode;
    }

    public void setBCPinCode(String BCPinCode) {
        this.BCPinCode = BCPinCode;
    }



}