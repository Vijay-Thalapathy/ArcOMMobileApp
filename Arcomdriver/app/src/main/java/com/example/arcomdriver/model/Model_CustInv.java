package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Jan 2024*/

public class Model_CustInv {

    private String CustInvname;
    private String CustInvstartDate;
    private String CustInvendDate;
    private String CustInvgroupid;
    private String CustInvzoneid;

    public Model_CustInv(String CustInvname, String CustInvstartDate, String CustInvendDate, String CustInvgroupid, String CustInvzoneid) {
        this.CustInvname = CustInvname;
        this.CustInvstartDate = CustInvstartDate;
        this.CustInvendDate = CustInvendDate;
        this.CustInvgroupid = CustInvgroupid;
        this.CustInvzoneid = CustInvzoneid;

    }

    public String getCustInvname() {
        return CustInvname;
    }

    public void setCustInvname(String custInvname) {
        CustInvname = custInvname;
    }

    public String getCustInvstartDate() {
        return CustInvstartDate;
    }

    public void setCustInvstartDate(String custInvstartDate) {
        CustInvstartDate = custInvstartDate;
    }

    public String getCustInvendDate() {
        return CustInvendDate;
    }

    public void setCustInvendDate(String custInvendDate) {
        CustInvendDate = custInvendDate;
    }

    public String getCustInvgroupid() {
        return CustInvgroupid;
    }

    public void setCustInvgroupid(String custInvgroupid) {
        CustInvgroupid = custInvgroupid;
    }

    public String getCustInvzoneid() {
        return CustInvzoneid;
    }

    public void setCustInvzoneid(String custInvzoneid) {
        CustInvzoneid = custInvzoneid;
    }


}