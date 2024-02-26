package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Mar 2023*/

public class Model_InvoiceList {

    private String Invoice_productname;
    private String Invoice_quantity;
    private String Invoice_baseamount;
    private String Invoice_productimage;
    private String itemistaxable;
    private String Invoice_priceperunit;
    private String Invoice_upccode;
    private String Invoice_description;

    public Model_InvoiceList(String Invoice_productname, String Invoice_quantity, String Invoice_baseamount,String Invoice_productimage,String Invoice_priceperunit,String itemistaxable,String Invoice_upccode,String Invoice_description) {
        this.Invoice_productname = Invoice_productname;
        this.Invoice_quantity = Invoice_quantity;
        this.Invoice_baseamount = Invoice_baseamount;
        this.Invoice_productimage = Invoice_productimage;
        this.Invoice_priceperunit = Invoice_priceperunit;
        this.itemistaxable = itemistaxable;
        this.Invoice_upccode = Invoice_upccode;
        this.Invoice_description = Invoice_description;

    }

    public String getItemistaxable() {
        return itemistaxable;
    }

    public void setItemistaxable(String itemistaxable) {
        this.itemistaxable = itemistaxable;
    }

    public String getInvoice_productname() {
        return Invoice_productname;
    }

    public void setInvoice_productname(String invoice_productname) {
        Invoice_productname = invoice_productname;
    }

    public String getInvoice_quantity() {
        return Invoice_quantity;
    }

    public void setInvoice_quantity(String invoice_quantity) {
        Invoice_quantity = invoice_quantity;
    }

    public String getInvoice_baseamount() {
        return Invoice_baseamount;
    }

    public void setInvoice_baseamount(String invoice_baseamount) {
        Invoice_baseamount = invoice_baseamount;
    }

    public String getInvoice_productimage() {
        return Invoice_productimage;
    }

    public void setInvoice_productimage(String invoice_productimage) {
        Invoice_productimage = invoice_productimage;
    }
    public String getInvoice_priceperunit() {
        return Invoice_priceperunit;
    }

    public void setInvoice_priceperunit(String invoice_priceperunit) {
        Invoice_priceperunit = invoice_priceperunit;
    }
    public String getInvoice_upccode() {
        return Invoice_upccode;
    }

    public void setInvoice_upccode(String invoice_upccode) {
        Invoice_upccode = invoice_upccode;
    }

    public String getInvoice_description() {
        return Invoice_description;
    }

    public void setInvoice_description(String invoice_description) {
        Invoice_description = invoice_description;
    }

}