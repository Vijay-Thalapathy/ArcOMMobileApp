package com.example.arcomdriver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Oct 2022*/
public class SQLiteController {
    private Context context;
    private DbHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteController(Context cxt) {
        this.context = cxt;
    }

    public SQLiteController open() throws SQLException {
        dbHandler = new DbHandler(context);
        sqLiteDatabase = dbHandler.getWritableDatabase();
        sqLiteDatabase = dbHandler.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHandler.close();
    }

    public void insertOrders(
         String OrderID,
         String order_name,
         String billtoaddressid,
         String customerid,
         String customeridtype,
         String datefulfilled,
         String description,
         String discountamount,
         String timestamp,
         String transactioncurrencyid,
         String exchangerate,
         String discountamountbase,
         String discountpercentage,
         String freightamount,
         String freightamountbase,
         String freighttermscode,
         String ispricelocked,
         String lastbackofficesubmit,
         String opportunityid,
         String ordernumber,
         String paymenttermscode,
         String paymenttermscode_display,
         String pricelevelid,
         String pricingerrorcode,
         String prioritycode,
         String quoteid,
         String requestdeliveryby,
         String shippingmethodcode,
         String shippingmethodcode_display,
         String shiptoaddressid,
         String statecode,
         String submitdate,
         String submitstatus,
         String submitstatusdescription,
         String totalamount,
         String totalamountbase,
         String totalamountlessfreight,
         String totalamountlessfreightbase,
         String totaldiscountamount,
         String totaldiscountamountbase,
         String totallineitemamount,
         String totallineitemamountbase,
         String totallineitemdiscountamount,
         String totallineitemdiscountamountbase,
         String totaltax,
         String totaltaxbase,
         String willcall,
         String isdisabled,
         String disabledby,
         String disabledbehalfof,
         String isdeleted,
         String deletedby,
         String deletedbehalfof,
         String deletedon,
         String createdby,
         String createdbehalfof,
         String creratedon,
         String modifiedby,
         String modifiedbehalfof,
         String modifiedon,
         String agent,
         String salesrepid,
         String pricingdate,
         String datafromtypeid,
         String datafromid,
         String tenantid,
         String isactive,
         String deliverynote,
         String shipnote,
         String termsconditions,
         String memo,
         String cancellationreason,
         String comments,
         String cancellationDate,
         String poreferencenum,
         String confirmedby,
         String confirmeddate,
         String notes,
         String warehouseid,
         String customeristaxable,
         String netStatus,
         String SyncStatus,
         String lastsyncon,
         String draftnumber,
         String shipmenttypeid,
         String taxid,
         String netterms,
         String duedate

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. ORDER_ID, OrderID);
        cv.put(DbHandler.ORDER_NAME, order_name);
        cv.put(DbHandler.ORDER_BILL_ADDRESS, billtoaddressid);
        cv.put(DbHandler.ORDER_CUSTOMER_ID, customerid);
        cv.put(DbHandler.ORDER_CUSTOMER_ID_TYPE, customeridtype);
        cv.put(DbHandler.ORDER_DATEFUL, datefulfilled);
        cv.put(DbHandler.ORDER_DESCRIPTION, description);
        cv.put(DbHandler.ORDER_DISCOUNT_AMOUNT, discountamount);
        cv.put(DbHandler.ORDER_TIMESTAMP, timestamp);
        cv.put(DbHandler.ORDER_TRANSACTION_CURRENCY, transactioncurrencyid);
        cv.put(DbHandler.ORDER_EXACHANGE_RATE, exchangerate);
        cv.put(DbHandler.ORDER_DISCOUNT_BASE, discountamountbase);
        cv.put(DbHandler.ORDER_DISCOUNT_PERCENTAGE, discountpercentage);
        cv.put(DbHandler.ORDER_FREIGHT_AMOUNT, freightamount);
        cv.put(DbHandler.ORDER_FREIGHT_BASE, freightamountbase);
        cv.put(DbHandler.ORDER_FREIGHT_TERMS, freighttermscode);
        cv.put(DbHandler.ORDER_PRICELOCKED, ispricelocked);
        cv.put(DbHandler.ORDER_LAST_BACKOFF, lastbackofficesubmit);
        cv.put(DbHandler.ORDER_OPPORTUNITY_ID, opportunityid);
        cv.put(DbHandler.ORDER_NUMBER, ordernumber);
        cv.put(DbHandler.ORDER_PAYMENT_TERMSCODE, paymenttermscode);
        cv.put(DbHandler.ORDER_PAYMENT_DISPLAY, paymenttermscode_display);
        cv.put(DbHandler.ORDER_PRICE_LEVEL_ID, pricelevelid);
        cv.put(DbHandler.ORDER_PRICE_ERORRCODE, pricingerrorcode);
        cv.put(DbHandler.ORDER_PRIORITY_CODE, prioritycode);
        cv.put(DbHandler.ORDER_QUOTEID, quoteid);
        cv.put(DbHandler.ORDER_REQUEST_DELIVERBY, requestdeliveryby);
        cv.put(DbHandler.ORDER_SHIPPING_CODE, shippingmethodcode);
        cv.put(DbHandler.ORDER_SHIPPING_CODEDISPLAY, shippingmethodcode_display);
        cv.put(DbHandler.ORDER_SHIPPING_ADDRESSID, shiptoaddressid);
        cv.put(DbHandler.ORDER_STATECODE, statecode);
        cv.put(DbHandler.ORDER_SUBMIT_DATE, submitdate);
        cv.put(DbHandler.ORDER_SUBMIT_STATUS, submitstatus);
        cv.put(DbHandler.ORDER_SUBMIT_DESCRIPTION, submitstatusdescription);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT, totalamount);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT_BASE, totalamountbase);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT_LESS_FREIGHT, totalamountlessfreight);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT_LESS_FREIGHT_BASE, totalamountlessfreightbase);
        cv.put(DbHandler.ORDER_TOTAL_DISCOUNT_AMOUNT, totaldiscountamount);
        cv.put(DbHandler.ORDER_TOTAL_DISCOUNT_AMOUNT_BASE, totaldiscountamountbase);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_AMOUNT, totallineitemamount);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_AMOUNT_BASE, totallineitemamountbase);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT, totallineitemdiscountamount);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT_BASE, totallineitemdiscountamountbase);
        cv.put(DbHandler.ORDER_TOTAL_TAX, totaltax);
        cv.put(DbHandler.ORDER_TOTAL_TAX_BASE, totaltaxbase);
        cv.put(DbHandler.ORDER_WILL_CALL, willcall);
        cv.put(DbHandler.ORDER_ISDISABLED, isdisabled);
        cv.put(DbHandler.ORDER_DISABLED_BY, disabledby);
        cv.put(DbHandler.ORDER_DISABLED_BEHALFOF, disabledbehalfof);
        cv.put(DbHandler.ORDER_ISDELETED, isdeleted);
        cv.put(DbHandler.ORDER_DELETED_BY, deletedby);
        cv.put(DbHandler.ORDER_DELETED_BEHALFOF, deletedbehalfof);
        cv.put(DbHandler.ORDER_DELETED_DON, deletedon);
        cv.put(DbHandler.ORDER_CREATED_BY, createdby);
        cv.put(DbHandler.ORDER_CREATED_BEHALFOF, createdbehalfof);
        cv.put(DbHandler.ORDER_CREATED_DON, creratedon);
        cv.put(DbHandler.ORDER_MODIFIED_BY, modifiedby);
        cv.put(DbHandler.ORDER_MODIFIED_BEHALFOF, modifiedbehalfof);
        cv.put(DbHandler.ORDER_MODIFIED_DON, modifiedon);
        cv.put(DbHandler.ORDER_AGENT, agent);
        cv.put(DbHandler.ORDER_SALES_REPID, salesrepid);
        cv.put(DbHandler.ORDER_PRICING_DATE, pricingdate);
        cv.put(DbHandler.ORDER_DATE_FROM_TYPE_ID, datafromtypeid);
        cv.put(DbHandler.ORDER_DATE_FROM_ID, datafromid);
        cv.put(DbHandler.ORDER_TENANT_ID, tenantid);
        cv.put(DbHandler.ORDER_ISACTIVE, isactive);
        cv.put(DbHandler.ORDER_DELIVERY_NOTE, deliverynote);
        cv.put(DbHandler.ORDER_SHIPNOTE, shipnote);
        cv.put(DbHandler.ORDER_TERMS_CONDITIONS, termsconditions);
        cv.put(DbHandler.ORDER_MEMO, memo);
        cv.put(DbHandler.ORDER_CANCELLATION_REASON, cancellationreason);
        cv.put(DbHandler.ORDER_COMMENTS, comments);
        cv.put(DbHandler.ORDER_CANCELLATION_DATE, cancellationDate);
        cv.put(DbHandler.ORDER_PO_REFERENCE_NUM, poreferencenum);
        cv.put(DbHandler.ORDER_CONFIRMED_BY, confirmedby);
        cv.put(DbHandler.ORDER_CONFIRMED_DATE, confirmeddate);
        cv.put(DbHandler.ORDER_NOTES, notes);
        cv.put(DbHandler.ORDER_WAREHOUSE_ID, warehouseid);
        cv.put(DbHandler.ORDER_CUSTOMERTAXSTATUS, customeristaxable);
        cv.put(DbHandler.ORDER_NETSTATUS, netStatus);
        cv.put(DbHandler.ORDER_SYNCSTATUS, SyncStatus);
        cv.put(DbHandler.ORDER_LASTSYNCON, lastsyncon);
        cv.put(DbHandler.ORDER_DRAFTNUM, draftnumber);
        cv.put(DbHandler.ORDER_SHIPMENTTYPE, shipmenttypeid);
        cv.put(DbHandler.ORDER_TAXID, taxid);
        cv.put(DbHandler.ORDER_TERMSID, netterms);
        cv.put(DbHandler.ORDER_DUEDATE, duedate);
        sqLiteDatabase.insert(DbHandler.TABLE_ORDER,null,cv);
    }
    public void insertInvoice(
        String id,
        String orderid,
        String paymenttypeid,
        String paymentid,
        String paymentmethod,
        String name,
        String billtoaddressid,
        String customerid,
        String customeridtype,
        String datefulfilled,
        String description,
        String discountamount,
        String timestamp,
        String transactioncurrencyid,
        String exchangerate,
        String discountamountbase,
        String discountpercentage,
        String freightamount,
        String freightamountbase,
        String freighttermscode,
        String ispricelocked,
        String lastbackofficesubmit,
        String opportunityid,
        String ordernumber,
        String paymenttermscode,
        String paymenttermscode_display,
        String pricelevelid,
        String pricingerrorcode,
        String prioritycode,
        String quoteid,
        String requestdeliveryby,
        String shippingmethodcode,
        String shippingmethodcode_display,
        String shiptoaddressid,
        String statecode,
        String statuscode,
        String invoicedate,
        String submitstatus,
        String submitstatusdescription,
        String totalamount,
        String totalamountbase,
        String totalamountlessfreight,
        String totalamountlessfreightbase,
        String totaldiscountamount,
        String totaldiscountamountbase,
        String totallineitemamount,
        String totallineitemamountbase,
        String totallineitemdiscountamount,
        String totallineitemdiscountamountbase,
        String totaltax,
        String totaltaxbase,
        String willcall,
        String isdisabled,
        String disabledby,
        String disabledbehalfof,
        String isdeleted,
        String deletedby,
        String deletedbehalfof,
        String deletedon,
        String createdby,
        String createdbehalfof,
        String createdon,
        String modifiedby,
        String modifiedbehalfof,
        String modifiedon,
        String agent,
        String salesrepid,
        String pricingdate,
        String datafromtypeid,
        String datafromid,
        String tenantid,
        String isactive,
        String deliverynote,
        String shipnote,
        String termsconditions,
        String memo,
        String cancellationreason,
        String comments,
        String cancellationDate,
        String referenceorder,
        String vendorid,
        String externalid,
        String externalstatus,
        String warehouseid,
        String customeristaxable,
        String draftnumber,
        String lastsyncon,
        String netStatus,
        String SyncStatus,
        String taxid,
        String netterms,
        String duedate

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. INVOICES_DEFAULT_ID ,id);
        cv.put(DbHandler. INVOICES_ORDER_ID ,orderid);
        cv.put(DbHandler. INVOICES_PAYMENTTYPE_ID ,paymenttypeid);
        cv.put(DbHandler. INVOICES_PAYMENT_ID ,paymentid);
        cv.put(DbHandler. INVOICES_PAYMENT_METHOD ,paymentmethod);
        cv.put(DbHandler. INVOICES_NAME ,name);
        cv.put(DbHandler. INVOICES_BILLTOADDRESS_ID ,billtoaddressid);
        cv.put(DbHandler. INVOICES_CUSTOMER_ID ,customerid);
        cv.put(DbHandler. INVOICES_CUSTOMERID_TYPE ,customeridtype);
        cv.put(DbHandler. INVOICES_DATEFULFILLED ,datefulfilled);
        cv.put(DbHandler. INVOICES_DESCRIPTION ,description);
        cv.put(DbHandler. INVOICES_DISCOUNTAMT ,discountamount);
        cv.put(DbHandler. INVOICES_TIMESTAMP ,timestamp);
        cv.put(DbHandler. INVOICES_TRANSACTION_CURRENCY ,transactioncurrencyid);
        cv.put(DbHandler. INVOICES_EXCHANGE_RATE ,exchangerate);
        cv.put(DbHandler. INVOICES_DISCOUNTAMT_BASE ,discountamountbase);
        cv.put(DbHandler. INVOICES_DISCOUNT_PERCENTAGE ,discountpercentage);
        cv.put(DbHandler. INVOICES_FREIGHTAMOUNT ,freightamount);
        cv.put(DbHandler. INVOICES_FRIGHTAMT_BASE ,freightamountbase);
        cv.put(DbHandler. INVOICES_FRIGHTTERMS_CODE ,freighttermscode);
        cv.put(DbHandler. INVOICES_ISPRICE_LOCKED ,ispricelocked);
        cv.put(DbHandler. INVOICES_LASTBACKOFFICE ,lastbackofficesubmit);
        cv.put(DbHandler. INVOICES_OPPORTUNITY_ID ,opportunityid);
        cv.put(DbHandler. INVOICES_ORDERNUMBER ,ordernumber);
        cv.put(DbHandler. INVOICES_PAYMENTTERMS_CODE ,paymenttermscode);
        cv.put(DbHandler. INVOICES_PAYMENTTERMS_DISPLAY ,paymenttermscode_display);
        cv.put(DbHandler. INVOICES_PRICELEVELID ,pricelevelid);
        cv.put(DbHandler. INVOICES_PRICEERROR_CODE ,pricingerrorcode);
        cv.put(DbHandler. INVOICES_PRIORITY_CODE ,prioritycode);
        cv.put(DbHandler. INVOICES_QUOTE_ID ,quoteid);
        cv.put(DbHandler. INVOICES_REQUEST_DELIVERY ,requestdeliveryby);
        cv.put(DbHandler. INVOICES_SHIPPING_CODE ,shippingmethodcode);
        cv.put(DbHandler. INVOICES_SHIPPING_DISPLAY ,shippingmethodcode_display);
        cv.put(DbHandler. INVOICES_SHIPTOADDRESS_ID ,shiptoaddressid);
        cv.put(DbHandler. INVOICES_STATECODE ,statecode);
        cv.put(DbHandler. INVOICES_STATUS_CODE ,statuscode);
        cv.put(DbHandler. INVOICES_DATE ,invoicedate);
        cv.put(DbHandler. INVOICES_SUBMIT_STATUS ,submitstatus);
        cv.put(DbHandler. INVOICES_SUBMIT_DESC ,submitstatusdescription);
        cv.put(DbHandler. INVOICES_TOTAL_AMT ,totalamount);
        cv.put(DbHandler. INVOICES_TOTALAMT_BASE ,totalamountbase);
        cv.put(DbHandler. INVOICES_TOTALAMT_FREIGHT ,totalamountlessfreight);
        cv.put(DbHandler. INVOICES_TOTALAMT_FRIGHTBASE ,totalamountlessfreightbase);
        cv.put(DbHandler. INVOICES_TOTALDISCOUNT_AMT ,totaldiscountamount);
        cv.put(DbHandler. INVOICES_TOTALDISCOUNT_AMTBASE ,totaldiscountamountbase);
        cv.put(DbHandler. INVOICES_TOTALLINE_ITEMAMT ,totallineitemamount);
        cv.put(DbHandler. INVOICES_TOTALLINEITEM_AMTBASE ,totallineitemamountbase);
        cv.put(DbHandler. INVOICES_TOTALLINEITEM_DISCOUNTBASE ,totallineitemdiscountamount);
        cv.put(DbHandler. INVOICES_TOTALLINEITEM_DISCOUNT_AMTBASE ,totallineitemdiscountamountbase);
        cv.put(DbHandler. INVOICES_TOTALTAX ,totaltax);
        cv.put(DbHandler. INVOICES_TOTALTAX_BASE ,totaltaxbase);
        cv.put(DbHandler. INVOICES_WILLCALL ,willcall);
        cv.put(DbHandler. INVOICES_ISDISABLE ,isdisabled);
        cv.put(DbHandler. INVOICES_DISABLEBY ,disabledby);
        cv.put(DbHandler. INVOICES_DISABLE_BEHALF ,disabledbehalfof);
        cv.put(DbHandler. INVOICES_ISDELETED ,isdeleted);
        cv.put(DbHandler. INVOICES_DELETEBY ,deletedby);
        cv.put(DbHandler. INVOICES_DELETE_BEHALF ,deletedbehalfof);
        cv.put(DbHandler. INVOICES_DELETEDON ,deletedon);
        cv.put(DbHandler. INVOICES_CREATEBY ,createdby);
        cv.put(DbHandler. INVOICES_CREATEDBEHALF ,createdbehalfof);
        cv.put(DbHandler. INVOICES_CREATEDON ,createdon);
        cv.put(DbHandler. INVOICES_MODIFIEDBY ,modifiedby);
        cv.put(DbHandler. INVOICES_MODIFIED_BEHALF ,modifiedbehalfof);
        cv.put(DbHandler. INVOICES_MODIFIDON ,modifiedon);
        cv.put(DbHandler. INVOICES_AGENT ,agent);
        cv.put(DbHandler. INVOICES_SALESREPID ,salesrepid);
        cv.put(DbHandler. INVOICES_PRICINGDATE ,pricingdate);
        cv.put(DbHandler. INVOICES_DATAFROMTYPEID ,datafromtypeid);
        cv.put(DbHandler. INVOICES_DATAFROM_ID ,datafromid);
        cv.put(DbHandler. INVOICES_TENANID ,tenantid);
        cv.put(DbHandler. INVOICES_ISACTIVE ,isactive);
        cv.put(DbHandler. INVOICES_DELIVERYNOTE ,deliverynote);
        cv.put(DbHandler. INVOICES_SHIP ,shipnote);
        cv.put(DbHandler. INVOICES_TERMS ,termsconditions);
        cv.put(DbHandler. INVOICES_MEMO ,memo);
        cv.put(DbHandler. INVOICES_CANCELREASON ,cancellationreason);
        cv.put(DbHandler. INVOICES_COMMENTS ,comments);
        cv.put(DbHandler. INVOICES_CANCEL_DATE ,cancellationDate);
        cv.put(DbHandler. INVOICES_REFRENCE_ORDER ,referenceorder);
        cv.put(DbHandler. INVOICES_VENDORID ,vendorid);
        cv.put(DbHandler. INVOICES_EXTERNALID ,externalid);
        cv.put(DbHandler. INVOICES_EXTERNAL_STATUS ,externalstatus);
        cv.put(DbHandler. INVOICES_WAREHOUSE_ID ,warehouseid);
        cv.put(DbHandler. INVOICES_CUSTOMER_ISTAXABLE ,customeristaxable);
        cv.put(DbHandler. INVOICES_DRAFT_NUMBER ,draftnumber);
        cv.put(DbHandler. INVOICES_LASTSYNCON ,lastsyncon);
        cv.put(DbHandler. INVOICES_NETSTATUS,netStatus);
        cv.put(DbHandler. INVOICES_SYNCSTATUS,SyncStatus);
        cv.put(DbHandler. INVOICES_TAXID,taxid);
        cv.put(DbHandler. INVOICES_TERMSID,netterms);
        cv.put(DbHandler. INVOICES_DUEDATE,duedate);
        sqLiteDatabase.insert(DbHandler.TABLE_INVOICE,null,cv);
    }
    public void insertInvoiceProduct(
            String id,
            String invoiceid,
            String transactioncurrencyid,
            String uomid,
            String baseamount,
            String exchangerate,
            String baseamountbase,
            String description,
            String extendedamount,
            String extendedamountbase,
            String iscopied,
            String ispriceoverridden,
            String isproductoverridden,
            String lineitemnumber,
            String manualdiscountamount,
            String manualdiscountamountbase,
            String parentbundleid,
            String productassociationid,
            String producttypecode,
            String priceperunit,
            String priceperunitbase,
            String pricingerrorcode,
            String productdescription,
            String productname,
            String productid,
            String quantity,
            String quantitybackordered,
            String quantitycancelled,
            String quantityshipped,
            String requestdeliveryby,
            String salesorderispricelocked,
            String salesorderstatecode,
            String salesrepid,
            String shiptoaddressid,
            String tax,
            String taxbase,
            String volumediscountamount,
            String volumediscountamountbase,
            String willcall,
            String sequencenumber,
            String quotedetailid,
            String salesorderdetailname,
            String uuid,
            String isdisabled,
            String disabledby,
            String disabledbehalfof,
            String isdeleted,
            String deletedby,
            String deletedbehalfof,
            String deletedon,
            String createdby,
            String createdbehalfof,
            String createdon,
            String modifiedby,
            String modifiedbehalfof,
            String modifiedon,
            String agent,
            String datafromtypeid,
            String datafromid,
            String tenantid,
            String isactive,
            String quantitypicked,
            String quantitypacked,
            String orderpid,
            String externalid,
            String externalstatus,
            String itemistaxable,
            String itemorderid,
            String itemwarehouseid

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler.IN_PRODUCTS_ID ,id);
        cv.put(DbHandler.IN_PRODUCTS_INVOICE_ID ,invoiceid);
        cv.put(DbHandler.IN_PRODUCTS_TXNCURRENCY ,transactioncurrencyid);
        cv.put(DbHandler.IN_PRODUCTS_UOMID ,uomid);
        cv.put(DbHandler.IN_PRODUCTS_BASEAMT ,baseamount);
        cv.put(DbHandler.IN_PRODUCTS_EXCHANGERATE ,exchangerate);
        cv.put(DbHandler.IN_PRODUCTS_BASEAMT_BASE ,baseamountbase);
        cv.put(DbHandler.IN_PRODUCTS_DEC ,description);
        cv.put(DbHandler.IN_PRODUCTS_EXTENDAMT ,extendedamount);
        cv.put(DbHandler.IN_PRODUCTS_EXTENDAMT_BASE ,extendedamountbase);
        cv.put(DbHandler.IN_PRODUCTS_ISCOPIED ,iscopied);
        cv.put(DbHandler.IN_PRODUCTS_ISPRICEOVER ,ispriceoverridden);
        cv.put(DbHandler.IN_PRODUCTS_ISPRODUCT_OVER ,isproductoverridden);
        cv.put(DbHandler.IN_PRODUCTS_LINEITEM_NUM ,lineitemnumber);
        cv.put(DbHandler.IN_PRODUCTS_MANUAL_DISCOUNT ,manualdiscountamount);
        cv.put(DbHandler.IN_PRODUCTS_MANUAL_DISAMT_BASE ,manualdiscountamountbase);
        cv.put(DbHandler.IN_PRODUCTS_PARENT_BUNDLEID ,parentbundleid);
        cv.put(DbHandler.IN_PRODUCTS_PRODUCT_ASSOCIATION ,productassociationid);
        cv.put(DbHandler.IN_PRODUCTS_PRODUCTTYPECODE ,producttypecode);
        cv.put(DbHandler.IN_PRODUCTS_PRICEPERUNIT ,priceperunit);
        cv.put(DbHandler.IN_PRODUCTS_PRICEPER_UNITBASE ,priceperunitbase);
        cv.put(DbHandler.IN_PRODUCTS_PRICING_ERRORCODE ,pricingerrorcode);
        cv.put(DbHandler.IN_PRODUCTS_PRODUCT_DEC ,productdescription);
        cv.put(DbHandler.IN_PRODUCTS_PRODUCT_NAME ,productname);
        cv.put(DbHandler.IN_PRODUCTS_PRODUCTID ,productid);
        cv.put(DbHandler.IN_PRODUCTS_QTY ,quantity);
        cv.put(DbHandler.IN_PRODUCTS_QTY_BACK ,quantitybackordered);
        cv.put(DbHandler.IN_PRODUCTS_QTY_CANCELLED ,quantitycancelled);
        cv.put(DbHandler.IN_PRODUCTS_QTY_SHIPPED ,quantityshipped);
        cv.put(DbHandler.IN_PRODUCTS_REQUEST_DELIVERY ,requestdeliveryby);
        cv.put(DbHandler.IN_PRODUCTS_SALESORDER_LOCK ,salesorderispricelocked);
        cv.put(DbHandler.IN_PRODUCTS_SALES_STATECODE ,salesorderstatecode);
        cv.put(DbHandler.IN_PRODUCTS_SALES_REPID ,salesrepid);
        cv.put(DbHandler.IN_PRODUCTS_SHIPTOADDRESSID ,shiptoaddressid);
        cv.put(DbHandler.IN_PRODUCTS_TAX ,tax);
        cv.put(DbHandler.IN_PRODUCTS_TAX_BASE ,taxbase);
        cv.put(DbHandler.IN_PRODUCTS_VOLUME_DISAMT ,volumediscountamount);
        cv.put(DbHandler.IN_PRODUCTS_VOLUME_DIS_AMTBASE ,volumediscountamountbase);
        cv.put(DbHandler.IN_PRODUCTS_WILLCALL ,willcall);
        cv.put(DbHandler.IN_PRODUCTS_SEQUENCENUM ,sequencenumber);
        cv.put(DbHandler.IN_PRODUCTS_QUOTE_DETAIL ,quotedetailid);
        cv.put(DbHandler.IN_PRODUCTS_SALES_ORDER ,salesorderdetailname);
        cv.put(DbHandler.IN_PRODUCTS_UUID ,uuid);
        cv.put(DbHandler.IN_PRODUCTS_ISDISABLED ,isdisabled);
        cv.put(DbHandler.IN_PRODUCTS_DISABLEBY ,disabledby);
        cv.put(DbHandler.IN_PRODUCTS_DISABLEBEHALF ,disabledbehalfof);
        cv.put(DbHandler.IN_PRODUCTS_ISDELETED ,isdeleted);
        cv.put(DbHandler.IN_PRODUCTS_DELETEBY ,deletedby);
        cv.put(DbHandler.IN_PRODUCTS_DELETE_BEHALF ,deletedbehalfof);
        cv.put(DbHandler.IN_PRODUCTS_DELETEDON ,deletedon);
        cv.put(DbHandler.IN_PRODUCTS_CREATEDBY ,createdby);
        cv.put(DbHandler.IN_PRODUCTS_CREATEDBEHALF ,createdbehalfof);
        cv.put(DbHandler.IN_PRODUCTS_CREATEDON ,createdon);
        cv.put(DbHandler.IN_PRODUCTS_MODIFIYBY ,modifiedby);
        cv.put(DbHandler.IN_PRODUCTS_MODIFIEDBEHALF ,modifiedbehalfof);
        cv.put(DbHandler.IN_PRODUCTS_MODIFIYDON ,modifiedon);
        cv.put(DbHandler.IN_PRODUCTS_AGENT ,agent);
        cv.put(DbHandler.IN_PRODUCTS_DATAFROM_TYPEID ,datafromtypeid);
        cv.put(DbHandler.IN_PRODUCTS_DATAFROM_ID ,datafromid);
        cv.put(DbHandler.IN_PRODUCTS_TENANTID ,tenantid);
        cv.put(DbHandler.IN_PRODUCTS_ISACTIVE ,isactive);
        cv.put(DbHandler.IN_PRODUCTS_QTY_PICKED ,quantitypicked);
        cv.put(DbHandler.IN_PRODUCTS_QTY_PACKED ,quantitypacked);
        cv.put(DbHandler.IN_PRODUCTS_ORDERPID ,orderpid);
        cv.put(DbHandler.IN_PRODUCTS_EXTERNAL_ID ,externalid);
        cv.put(DbHandler.IN_PRODUCTS_EXTERNAL_STATUS ,externalstatus);
        cv.put(DbHandler.IN_PRODUCTS_ITEMS_TAX ,itemistaxable);
        cv.put(DbHandler.IN_PRODUCTS_ORDERID ,itemorderid);
        cv.put(DbHandler.IN_PRODUCTS_WAREHOUSE ,itemwarehouseid);
        sqLiteDatabase.insert(DbHandler.TABLE_INVOICE_PRODUCTS,null,cv);
    }

    public void UpdateOrders(
         String OrderID,
         String order_name,
         String billtoaddressid,
         String customerid,
         String customeridtype,
         String datefulfilled,
         String description,
         String discountamount,
         String timestamp,
         String transactioncurrencyid,
         String exchangerate,
         String discountamountbase,
         String discountpercentage,
         String freightamount,
         String freightamountbase,
         String freighttermscode,
         String ispricelocked,
         String lastbackofficesubmit,
         String opportunityid,
         String ordernumber,
         String paymenttermscode,
         String paymenttermscode_display,
         String pricelevelid,
         String pricingerrorcode,
         String prioritycode,
         String quoteid,
         String requestdeliveryby,
         String shippingmethodcode,
         String shippingmethodcode_display,
         String shiptoaddressid,
         String statecode,
         String submitdate,
         String submitstatus,
         String submitstatusdescription,
         String totalamount,
         String totalamountbase,
         String totalamountlessfreight,
         String totalamountlessfreightbase,
         String totaldiscountamount,
         String totaldiscountamountbase,
         String totallineitemamount,
         String totallineitemamountbase,
         String totallineitemdiscountamount,
         String totallineitemdiscountamountbase,
         String totaltax,
         String totaltaxbase,
         String willcall,
         String isdisabled,
         String disabledby,
         String disabledbehalfof,
         String isdeleted,
         String deletedby,
         String deletedbehalfof,
         String deletedon,
         String createdby,
         String createdbehalfof,
         String creratedon,
         String modifiedby,
         String modifiedbehalfof,
         String modifiedon,
         String agent,
         String salesrepid,
         String pricingdate,
         String datafromtypeid,
         String datafromid,
         String tenantid,
         String isactive,
         String deliverynote,
         String shipnote,
         String termsconditions,
         String memo,
         String cancellationreason,
         String comments,
         String cancellationDate,
         String poreferencenum,
         String confirmedby,
         String confirmeddate,
         String notes,
         String warehouseid,
         String customeristaxable,
         String netStatus,
         String SyncStatus,
          String lastsyncon,
         String draftnumber,
         String shipmenttypeid,
         String taxid,
         String netterms,
         String duedate

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. ORDER_ID, OrderID);
        cv.put(DbHandler.ORDER_NAME, order_name);
        cv.put(DbHandler.ORDER_BILL_ADDRESS, billtoaddressid);
        cv.put(DbHandler.ORDER_CUSTOMER_ID, customerid);
        cv.put(DbHandler.ORDER_CUSTOMER_ID_TYPE, customeridtype);
        cv.put(DbHandler.ORDER_DATEFUL, datefulfilled);
        cv.put(DbHandler.ORDER_DESCRIPTION, description);
        cv.put(DbHandler.ORDER_DISCOUNT_AMOUNT, discountamount);
        cv.put(DbHandler.ORDER_TIMESTAMP, timestamp);
        cv.put(DbHandler.ORDER_TRANSACTION_CURRENCY, transactioncurrencyid);
        cv.put(DbHandler.ORDER_EXACHANGE_RATE, exchangerate);
        cv.put(DbHandler.ORDER_DISCOUNT_BASE, discountamountbase);
        cv.put(DbHandler.ORDER_DISCOUNT_PERCENTAGE, discountpercentage);
        cv.put(DbHandler.ORDER_FREIGHT_AMOUNT, freightamount);
        cv.put(DbHandler.ORDER_FREIGHT_BASE, freightamountbase);
        cv.put(DbHandler.ORDER_FREIGHT_TERMS, freighttermscode);
        cv.put(DbHandler.ORDER_PRICELOCKED, ispricelocked);
        cv.put(DbHandler.ORDER_LAST_BACKOFF, lastbackofficesubmit);
        cv.put(DbHandler.ORDER_OPPORTUNITY_ID, opportunityid);
        cv.put(DbHandler.ORDER_NUMBER, ordernumber);
        cv.put(DbHandler.ORDER_PAYMENT_TERMSCODE, paymenttermscode);
        cv.put(DbHandler.ORDER_PAYMENT_DISPLAY, paymenttermscode_display);
        cv.put(DbHandler.ORDER_PRICE_LEVEL_ID, pricelevelid);
        cv.put(DbHandler.ORDER_PRICE_ERORRCODE, pricingerrorcode);
        cv.put(DbHandler.ORDER_PRIORITY_CODE, prioritycode);
        cv.put(DbHandler.ORDER_QUOTEID, quoteid);
        cv.put(DbHandler.ORDER_REQUEST_DELIVERBY, requestdeliveryby);
        cv.put(DbHandler.ORDER_SHIPPING_CODE, shippingmethodcode);
        cv.put(DbHandler.ORDER_SHIPPING_CODEDISPLAY, shippingmethodcode_display);
        cv.put(DbHandler.ORDER_SHIPPING_ADDRESSID, shiptoaddressid);
        cv.put(DbHandler.ORDER_STATECODE, statecode);
        cv.put(DbHandler.ORDER_SUBMIT_DATE, submitdate);
        cv.put(DbHandler.ORDER_SUBMIT_STATUS, submitstatus);
        cv.put(DbHandler.ORDER_SUBMIT_DESCRIPTION, submitstatusdescription);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT, totalamount);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT_BASE, totalamountbase);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT_LESS_FREIGHT, totalamountlessfreight);
        cv.put(DbHandler.ORDER_TOTAL_AMOUNT_LESS_FREIGHT_BASE, totalamountlessfreightbase);
        cv.put(DbHandler.ORDER_TOTAL_DISCOUNT_AMOUNT, totaldiscountamount);
        cv.put(DbHandler.ORDER_TOTAL_DISCOUNT_AMOUNT_BASE, totaldiscountamountbase);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_AMOUNT, totallineitemamount);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_AMOUNT_BASE, totallineitemamountbase);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT, totallineitemdiscountamount);
        cv.put(DbHandler.ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT_BASE, totallineitemdiscountamountbase);
        cv.put(DbHandler.ORDER_TOTAL_TAX, totaltax);
        cv.put(DbHandler.ORDER_TOTAL_TAX_BASE, totaltaxbase);
        cv.put(DbHandler.ORDER_WILL_CALL, willcall);
        cv.put(DbHandler.ORDER_ISDISABLED, isdisabled);
        cv.put(DbHandler.ORDER_DISABLED_BY, disabledby);
        cv.put(DbHandler.ORDER_DISABLED_BEHALFOF, disabledbehalfof);
        cv.put(DbHandler.ORDER_ISDELETED, isdeleted);
        cv.put(DbHandler.ORDER_DELETED_BY, deletedby);
        cv.put(DbHandler.ORDER_DELETED_BEHALFOF, deletedbehalfof);
        cv.put(DbHandler.ORDER_DELETED_DON, deletedon);
        cv.put(DbHandler.ORDER_CREATED_BY, createdby);
        cv.put(DbHandler.ORDER_CREATED_BEHALFOF, createdbehalfof);
        cv.put(DbHandler.ORDER_CREATED_DON, creratedon);
        cv.put(DbHandler.ORDER_MODIFIED_BY, modifiedby);
        cv.put(DbHandler.ORDER_MODIFIED_BEHALFOF, modifiedbehalfof);
        cv.put(DbHandler.ORDER_MODIFIED_DON, modifiedon);
        cv.put(DbHandler.ORDER_AGENT, agent);
        cv.put(DbHandler.ORDER_SALES_REPID, salesrepid);
        cv.put(DbHandler.ORDER_PRICING_DATE, pricingdate);
        cv.put(DbHandler.ORDER_DATE_FROM_TYPE_ID, datafromtypeid);
        cv.put(DbHandler.ORDER_DATE_FROM_ID, datafromid);
        cv.put(DbHandler.ORDER_TENANT_ID, tenantid);
        cv.put(DbHandler.ORDER_ISACTIVE, isactive);
        cv.put(DbHandler.ORDER_DELIVERY_NOTE, deliverynote);
        cv.put(DbHandler.ORDER_SHIPNOTE, shipnote);
        cv.put(DbHandler.ORDER_TERMS_CONDITIONS, termsconditions);
        cv.put(DbHandler.ORDER_MEMO, memo);
        cv.put(DbHandler.ORDER_CANCELLATION_REASON, cancellationreason);
        cv.put(DbHandler.ORDER_COMMENTS, comments);
        cv.put(DbHandler.ORDER_CANCELLATION_DATE, cancellationDate);
        cv.put(DbHandler.ORDER_PO_REFERENCE_NUM, poreferencenum);
        cv.put(DbHandler.ORDER_CONFIRMED_BY, confirmedby);
        cv.put(DbHandler.ORDER_CONFIRMED_DATE, confirmeddate);
        cv.put(DbHandler.ORDER_NOTES, notes);
        cv.put(DbHandler.ORDER_WAREHOUSE_ID, warehouseid);
        cv.put(DbHandler.ORDER_CUSTOMERTAXSTATUS, customeristaxable);
        cv.put(DbHandler.ORDER_NETSTATUS, netStatus);
        cv.put(DbHandler.ORDER_SYNCSTATUS, SyncStatus);
        cv.put(DbHandler.ORDER_LASTSYNCON, lastsyncon);
        cv.put(DbHandler.ORDER_DRAFTNUM, draftnumber);
        cv.put(DbHandler.ORDER_SHIPMENTTYPE, shipmenttypeid);
        cv.put(DbHandler.ORDER_TAXID, taxid);
        cv.put(DbHandler.ORDER_TERMSID, netterms);
        cv.put(DbHandler.ORDER_DUEDATE, duedate);
        sqLiteDatabase.update(DbHandler.TABLE_ORDER,cv,"order_id="+"\""+OrderID+"\"",null);
    }
    public void insertUser(String UserID,String UserName,String token,String Email) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. USER_ID, UserID);
        cv.put(DbHandler. USER_NAME, UserName);
        cv.put(DbHandler. USER_TOKEN, token);
        cv.put(DbHandler. USER_EMAIL, Email);
        sqLiteDatabase.insert(DbHandler.TABLE_USER,null,cv);
    }

    public void insertMasterSync(String syncID,String syncName,String syncTime) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. MASTER_SYNC_ID, syncID);
        cv.put(DbHandler. MASTER_SYNC_NAME, syncName);
        cv.put(DbHandler. MASTER_SYNC_TIME, syncTime);
        sqLiteDatabase.insert(DbHandler.TABLE_MASTER_SYNC,null,cv);
    }
    public void insertSalesRep(String salesrep_id,String salesrep_name) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. SALESREP_ID, salesrep_id);
        cv.put(DbHandler. SALESREP_NAME, salesrep_name);
        sqLiteDatabase.insert(DbHandler.TABLE_SALESREP,null,cv);
    }
    public void insertDeliveryRep(String delivery_rep_id,String delivery_rep_name,String delivery_rep_email) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. DELIVERY_ID, delivery_rep_id);
        cv.put(DbHandler. DELIVERY_NAME, delivery_rep_name);
        cv.put(DbHandler. DELIVERY_EMAIL, delivery_rep_email);
        sqLiteDatabase.insert(DbHandler.TABLE_DELIVERYREP,null,cv);
    }
    public void insertFulfillment(String fulfillment_id,String fulfillment_name,String Full_isdefault) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. FULFILLMENT_ID, fulfillment_id);
        cv.put(DbHandler. FULFILLMENT_NAME, fulfillment_name);
        cv.put(DbHandler. FULFILLMENT_DEFAULT, Full_isdefault);
        sqLiteDatabase.insert(DbHandler.TABLE_FULFILLMENT,null,cv);
    }
    public void insertWarehouse(String warehouse_id,String warehouse_name) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. WAREHOUSE_ID, warehouse_id);
        cv.put(DbHandler. WAREHOUSE_NAME, warehouse_name);
        sqLiteDatabase.insert(DbHandler.TABLE_WAREHOUSE,null,cv);
    }
    public void insertALlProduct(String all_product_id,String all_product_number,String all_product_name,String all_product_price,String all_product_status,String all_product_img,String all_upsc,String all_istax,String all_description) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. All_PRODUCT_ID, all_product_id);
        cv.put(DbHandler. All_PRODUCT_NUMBER, all_product_number);
        cv.put(DbHandler. All_PRODUCT_NAME, all_product_name);
        cv.put(DbHandler. All_PRODUCT_PRICE, all_product_price);
        cv.put(DbHandler. All_PRODUCT_STATUS, all_product_status);
        cv.put(DbHandler. All_PRODUCT_IMG, all_product_img);
        cv.put(DbHandler. All_PRODUCT_UPSC, all_upsc);
        cv.put(DbHandler. All_PRODUCT_ISTAX, all_istax);
        cv.put(DbHandler. All_PRODUCT_DES, all_description);
        sqLiteDatabase.insert(DbHandler.TABLE_All_PRODUCT,null,cv);
    }

    public void insertProductsAssert(String assertID,String assertName,String assertPID,String assertImage,String assertFile,String assertType,String assertDefault,String assertActive) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. PRODUCT_ASSERT_ID, assertID);
        cv.put(DbHandler. PRODUCT_ASSERT_NAME, assertName);
        cv.put(DbHandler. PRODUCT_ASSERT_PRODUCTSID, assertPID);
        cv.put(DbHandler. PRODUCT_ASSERT_IMAGE, assertImage);
        cv.put(DbHandler. PRODUCT_ASSERT_FILE, assertFile);
        cv.put(DbHandler. PRODUCT_ASSERT_TYPE, assertType);
        cv.put(DbHandler. PRODUCT_ASSERT_DEFAULT, assertDefault);
        cv.put(DbHandler. PRODUCT_ASSERT_ACTIVE, assertActive);
        sqLiteDatabase.insert(DbHandler.TABLE_PRODUCT_ASSERT,null,cv);
    }

    public void insertCancelledReason(String cancelreason_id,String cancelreason_name) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. CANCELREASON_ID, cancelreason_id);
        cv.put(DbHandler. CANCELREASON_NAME, cancelreason_name);
        sqLiteDatabase.insert(DbHandler.TABLE_CANCELREASON,null,cv);
    }
    public void insertInvoiceFormat(String id,String settingsname,String settingsvalue) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. INVOICE_ID, id);
        cv.put(DbHandler. INVOICE_NAME, settingsname);
        cv.put(DbHandler. INVOICE_VALUE, settingsvalue);
        sqLiteDatabase.insert(DbHandler.TABLE_INVOICE_FORMAT,null,cv);
    }
    public void insertCurrency(String id,String name,String currencysymbol,String isocurrencycode) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. CURRENCY_ID, id);
        cv.put(DbHandler. CURRENCY_NAME, name);
        cv.put(DbHandler. CURRENCY_SYMBOL, currencysymbol);
        cv.put(DbHandler. CURRENCY_Code, isocurrencycode);
        sqLiteDatabase.insert(DbHandler.TABLE_CURRENCY,null,cv);
    }
    public void insertCusState(String cs_id,String cs_stateid,String cs_istaxable,String cs_taxid,String cs_nettermsid) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. CS_ID, cs_id);
        cv.put(DbHandler. CS_STATE_ID, cs_stateid);
        cv.put(DbHandler. CS_TAXBLE, cs_istaxable);
        cv.put(DbHandler. CS_TAXID, cs_taxid);
        cv.put(DbHandler. CS_TERMSID, cs_nettermsid);
        sqLiteDatabase.insert(DbHandler.TABLE_CS_STATE,null,cv);
    }
    public void insertCusTax(String tax_id,String tax_taxpercentage,String tax_stateid) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. CS_TAX_ID, tax_id);
        cv.put(DbHandler. CS_TAX_PERCENT, tax_taxpercentage);
        cv.put(DbHandler. CS_TAX_STATEID, tax_stateid);
        sqLiteDatabase.insert(DbHandler.TABLE_CS_TAX,null,cv);
    }

    public void insertTerms(String terms_id,String terms_name,String terms_days) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. TERMS_ID, terms_id);
        cv.put(DbHandler. TERMS_NAME, terms_name);
        cv.put(DbHandler. TERMS_DAYS, terms_days);
        sqLiteDatabase.insert(DbHandler.TABLE_TERMS,null,cv);
    }
    public void insertAddress(String id,String line1,String line2,String line3,String stateorprovince,String addresstypecode,String country,String city,String postalcode,String postofficebox,String name,String isprimaryaddress,String isactive,String domainEvents) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. ADDRESS_ID, id);
        cv.put(DbHandler. ADDRESS_LINE1, line1);
        cv.put(DbHandler. ADDRESS_LINE2, line2);
        cv.put(DbHandler. ADDRESS_LINE3, line3);
        cv.put(DbHandler. ADDRESS_SATE_PROVINCE, stateorprovince);
        cv.put(DbHandler. ADDRESS_TYPECODE, addresstypecode);
        cv.put(DbHandler. ADDRESS_COUNTRY, country);
        cv.put(DbHandler. ADDRESS_CITY, city);
        cv.put(DbHandler. ADDRESS_POSTAL, postalcode);
        cv.put(DbHandler. ADDRESS_POSTOFFICE, postofficebox);
        cv.put(DbHandler. ADDRESS_NAME, name);
        cv.put(DbHandler. ADDRESS_PRIMARY_ADD, isprimaryaddress);
        cv.put(DbHandler. ADDRESS_ISACTIVE, isactive);
        cv.put(DbHandler. ADDRESS_DOMAINEVENTS, domainEvents);
        sqLiteDatabase.insert(DbHandler.TABLE_ADDRESS,null,cv);
    }
    public void insertProducts(String pid,String orderid,String productid,String priceperunit,String baseamount,String quantity,String productname,String itemistaxable,String warehouseid) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. ORDER_PRO_ID, pid);
        cv.put(DbHandler. ORDER_PRODUCT_ORDERID, orderid);
        cv.put(DbHandler. ORDER_PRODUCT_ID, productid);
        cv.put(DbHandler. ORDER_PRODUCT_PRICEPER_UNIT, priceperunit);
        cv.put(DbHandler. ORDER_PRODUCT_BASEAMT, baseamount);
        cv.put(DbHandler. ORDER_PRODUCT_QTY, quantity);
        cv.put(DbHandler. ORDER_PRODUCT_NAME, productname);
        cv.put(DbHandler. ORDER_PRODUCT_TAX, itemistaxable);
        cv.put(DbHandler. ORDER_PRODUCT_WAREHOUSE, warehouseid);
        sqLiteDatabase.insert(DbHandler.TABLE_ORDER_PRODUCT,null,cv);
    }
    public void UpdateProducts(String pid,String orderid,String productid,String priceperunit,String baseamount,String quantity,String productname,String itemistaxable,String warehouseid) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler.ORDER_PRO_ID, pid);
        cv.put(DbHandler.ORDER_PRODUCT_ORDERID, orderid);
        cv.put(DbHandler.ORDER_PRODUCT_ID, productid);
        cv.put(DbHandler.ORDER_PRODUCT_PRICEPER_UNIT, priceperunit);
        cv.put(DbHandler.ORDER_PRODUCT_BASEAMT, baseamount);
        cv.put(DbHandler.ORDER_PRODUCT_QTY, quantity);
        cv.put(DbHandler.ORDER_PRODUCT_NAME, productname);
        cv.put(DbHandler.ORDER_PRODUCT_TAX, itemistaxable);
        cv.put(DbHandler.ORDER_PRODUCT_WAREHOUSE, warehouseid);
        sqLiteDatabase.update(DbHandler.TABLE_ORDER_PRODUCT, cv, "pid=" + "\"" + pid + "\"", null);
    }
    public void insertShipments(String shipment_id,
        String orderid,
        String shipmenttypeid,
        String warehouseid,
        String routenum,
        String routedate,
        String truck,
        String driverid,
        String carrierid,
        String shippeddate,
        String trackingnumber,
        String deliverydate,
        String deliverytime,
        String isactive,
        String uuid,
        String isdisabled,
        String disabledby,
        String disabledbehalfof,
        String isdeleted,
        String deletedby,
        String deletedbehalfof,
        String deletedon,
        String createdby,
        String createdbehalfof,
        String creratedon,
        String modifiedby,
        String modifiedbehalfof,
        String modifiedon,
        String datafromtypeid,
        String datafromid,
        String tenantid,
        String fulfillmentstatus,
        String pickerid,
        String packerid,
        String shipperid,
        String invoicerid,
        String paymenterid,
        String deliveryagentid,
        String deliveryshipmenttypeid,
        String routedayid,
        String routeid,
        String truckid,
        String invoiceid,
        String workorderid,
        String purchaseorderid,
        String undeliveredreasonid,
        String undeliveredreason
    )
    {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. SHIP_ID, shipment_id);
        cv.put(DbHandler. SHIP_ORDER_ID , orderid);
        cv.put(DbHandler. SHIP_TYPE_ID , shipmenttypeid);
        cv.put(DbHandler. SHIP_WAREHOUSE_ID , warehouseid);
        cv.put(DbHandler. SHIP_ROUTE_NUM , routenum);
        cv.put(DbHandler. SHIP_ROUTE_DATE , routedate);
        cv.put(DbHandler. SHIP_TRUCK , truck);
        cv.put(DbHandler. SHIP_DRIVER_ID , driverid);
        cv.put(DbHandler. SHIP_CARRIER_ID , carrierid);
        cv.put(DbHandler. SHIPPED_DATE , shippeddate);
        cv.put(DbHandler. SHIP_TRACK_NUM , trackingnumber);
        cv.put(DbHandler. SHIP_DELIVERY_DATE , deliverydate);
        cv.put(DbHandler. SHIP_DELIVERY_TIME , deliverytime);
        cv.put(DbHandler. SHIP_IS_ACTIVE , isactive);
        cv.put(DbHandler. SHIP_UUID , uuid);
        cv.put(DbHandler. SHIP_IS_DISABLED , isdisabled);
        cv.put(DbHandler. SHIP_IS_DISABLED_BY , disabledby);
        cv.put(DbHandler. SHIP_DISBLED_BEHALFOF , disabledbehalfof);
        cv.put(DbHandler. SHIP_IS_DELETED , isdeleted);
        cv.put(DbHandler. SHIP_DELTED_BY , deletedby);
        cv.put(DbHandler. SHIP_DELETED_BEHALFOF , deletedbehalfof);
        cv.put(DbHandler. SHIP_DELETE_DON , deletedon);
        cv.put(DbHandler. SHIP_CREATE_BY , createdby);
        cv.put(DbHandler. SHIP_CREATED_BEHALFOF , createdbehalfof);
        cv.put(DbHandler. SHIP_CREATION_DON , creratedon);
        cv.put(DbHandler. SHIP_MODIFY_BY , modifiedby);
        cv.put(DbHandler. SHIP_MODIFIED_BEHALF , modifiedbehalfof);
        cv.put(DbHandler. SHIP_MODIFIED_DON , modifiedon);
        cv.put(DbHandler. SHIP_DATA_TYPE_ID , datafromtypeid);
        cv.put(DbHandler. SHIP_DATAFROM_ID , datafromid);
        cv.put(DbHandler. SHIP_TENAN_ID , tenantid);
        cv.put(DbHandler. SHIP_FULFILLMENTS_STATUS , fulfillmentstatus);
        cv.put(DbHandler. SHIP_PICKER_ID , pickerid);
        cv.put(DbHandler. SHIP_PACKER_ID , packerid);
        cv.put(DbHandler. SHIPPER_ID , shipperid);
        cv.put(DbHandler. SHIP_INVOICER_ID , invoicerid);
        cv.put(DbHandler. SHIP_PAYMENT_ID , paymenterid);
        cv.put(DbHandler. SHIP_DELIVERY_AGENT_ID , deliveryagentid);
        cv.put(DbHandler. SHIP_DELIVER_TYPE_ID , deliveryshipmenttypeid);
        cv.put(DbHandler. SHIP_ROUTE_DAY_ID , routedayid);
        cv.put(DbHandler. SHIP_ROUTE_ID , routeid);
        cv.put(DbHandler. SHIP_TRUCK_ID , truckid);
        cv.put(DbHandler. SHIP_INVOICE_ID , invoiceid);
        cv.put(DbHandler. SHIP_WORKORDER_ID , workorderid);
        cv.put(DbHandler. SHIP_PURCHASE_ORDER_ID , purchaseorderid);
        cv.put(DbHandler. SHIP_UNDELIVER_REASON_ID , undeliveredreasonid);
        cv.put(DbHandler. SHIP_UNDELIVER_REASON , undeliveredreason);
        sqLiteDatabase.insert(DbHandler.TABLE_SHIPMENT,null,cv);
    }
    public void insertCustomer(
           String Id,
           String customername,
           String customertypeid,
           String status,
           String businessname,
           String vatnumber,
           String gstnumber,
           String pannumber,
           String customerurl,
           String vendorid,
           String taxisapplicable,
           String billingaddressid,
           String shippingaddressid,
           String deliveryaddressid,
           String isactive,
           String uuid,
           String isdisabled,
           String disabledby,
           String disabledbehalfof,
           String isdeleted,
           String deletedby,
           String deletedbehalfof,
           String deletedon,
           String createdby,
           String createdbehalfof,
           String createdon,
           String modifiedby,
           String modifiedbehalfof,
           String modifiedon,
           String agent,
           String datafromtypeid,
           String datafromid,
           String tenantid,
           String website,
           String industry,
           String isprimaryaddress,
           String sid,
           String customernumber,
           String externalid,
           String externalupdated,
           String istaxable,
           String tax,
           String netterms

    ) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler. CUSTOMER_ID, Id);
        cv.put(DbHandler. CUSTOMER_NAME, customername);
        cv.put(DbHandler. CUSTOMER_TYPE_ID, customertypeid);
        cv.put(DbHandler. CUSTOMER_STATUS, status);
        cv.put(DbHandler. CUSTOMER_BUSINESS_NAME, businessname);
        cv.put(DbHandler. CUSTOMER_VAT_NUMBER, vatnumber);
        cv.put(DbHandler. CUSTOMER_GST_NUMBER, gstnumber);
        cv.put(DbHandler. CUSTOMER_PAN_NUMBER, pannumber);
        cv.put(DbHandler. CUSTOMER_CUSTOMER_URL, customerurl);
        cv.put(DbHandler. CUSTOMER_VENDOR_ID, vendorid);
        cv.put(DbHandler. CUSTOMER_TAXI_APPLICABLE, taxisapplicable);
        cv.put(DbHandler. CUSTOMER_BILLING_ADDRESS_ID, billingaddressid);
        cv.put(DbHandler. CUSTOMER_SHIPPING_ADDRESS_ID, shippingaddressid);
        cv.put(DbHandler. CUSTOMER_DELIVERY_ADDRESS_ID, deliveryaddressid);
        cv.put(DbHandler. CUSTOMER_IS_ACTIVE, isactive);
        cv.put(DbHandler. CUSTOMER_UUID, uuid);
        cv.put(DbHandler. CUSTOMER_IS_DISABLED, isdisabled);
        cv.put(DbHandler. CUSTOMER_DISABLED_BY, disabledby);
        cv.put(DbHandler. CUSTOMER_DISABLED_BEHALFOF, disabledbehalfof);
        cv.put(DbHandler. CUSTOMER_IS_DELETED, isdeleted);
        cv.put(DbHandler. CUSTOMER_DELETED_BY, deletedby);
        cv.put(DbHandler. CUSTOMER_DELETED_BEHALFOF, deletedbehalfof);
        cv.put(DbHandler. CUSTOMER_DELETE_DON, deletedon);
        cv.put(DbHandler. CUSTOMER_CREATE_BY, createdby);
        cv.put(DbHandler. CUSTOMER_CREATE_BEHALFOF, createdbehalfof);
        cv.put(DbHandler. CUSTOMER_CREATE_DON, createdon);
        cv.put(DbHandler. CUSTOMER_MODIFIED_BY, modifiedby);
        cv.put(DbHandler. CUSTOMER_MODIFIED_BEHALFOF, modifiedbehalfof);
        cv.put(DbHandler. CUSTOMER_MODIFIED_ON, modifiedon);
        cv.put(DbHandler. CUSTOMER_AGENT, agent);
        cv.put(DbHandler. CUSTOMER_DATAFROM_TYPE_ID, datafromtypeid);
        cv.put(DbHandler. CUSTOMER_DATAFROM_ID, datafromid);
        cv.put(DbHandler. CUSTOMER_TENANT_ID, tenantid);
        cv.put(DbHandler. CUSTOMER_WEBSITE_ID, website);
        cv.put(DbHandler. CUSTOMER_INDUSTRY_ID, industry);
        cv.put(DbHandler. CUSTOMER_IS_PRIMARY_ADDRESS, isprimaryaddress);
        cv.put(DbHandler. CUSTOMER_ISID, sid);
        cv.put(DbHandler. CUSTOMER_NUMBER, customernumber);
        cv.put(DbHandler. CUSTOMER_EXTERNAL_ID, externalid);
        cv.put(DbHandler. CUSTOMER_EXTERNAL_UPDATE, externalupdated);
        cv.put(DbHandler. CUSTOMER_ISTAXABLE, istaxable);
        cv.put(DbHandler. CUSTOMER_TAXID, tax);
        cv.put(DbHandler. CUSTOMER_NETTERMS, netterms);
        sqLiteDatabase.insert(DbHandler.TABLE_CUSTOMER,null,cv);

    }

    public Cursor readTableOrder() {
        String readAll = "select * from " + DbHandler.TABLE_ORDER +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableOrderProducts() {
        String readAll = "select * from " + DbHandler.TABLE_ORDER_PRODUCT +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readProductJoinTables(String OrderID) {
        String readAll = "SELECT op.* FROM Orderproducts op INNER JOIN orders o ON op.orderid=o.order_id where op.orderid= '"+ OrderID+"'" ;
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
       // Log.d("Product LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readInvoiceProductJoinTables(String InID) {
        String readAll = "SELECT op.* FROM invoiceproduct op INNER JOIN invoices o ON op.invoiceid=o.id where op.invoiceid= '"+ InID+"'" ;
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
       // Log.d("Product LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableItem(String tableName,String fieldName,String value) {
        String readAll = "select * from " + tableName + " where "+fieldName+"= '"+ value+"'" ;
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readJointTableOrder() {

         //String presaleList_query = "SELECT ord.*,cust.id,cust.customername FROM orders ord INNER JOIN customer cust ON ord.customerid=cust.Id order by ord.creratedon desc, ord.modifiedon desc";
         //String presaleList_query = "SELECT ord.*,cust.id,cust.customername FROM orders ord INNER JOIN customer cust ON ord.customerid=cust.Id order by ord.modifiedon desc, ord.creratedon desc, ord.ordernumber desc";
         //String presaleList_query = "SELECT ord.*,cust.id,cust.customername FROM orders ord INNER JOIN customer cust ON ord.customerid=cust.Id order by ord.modifiedon desc ,ord.creratedon desc, ord.ordernumber desc ";
         //String presaleList_query = "SELECT ord.*,cust.id,cust.customername FROM orders ord INNER JOIN customer cust ON ord.customerid=cust.Id ORDER BY CASE  WHEN ord.[modifiedon] IS NOT NULL THEN ord.[modifiedon] ELSE ord.[creratedon] END DESC";

        String presaleList_query = "SELECT CASE WHEN  ord.modifiedon = 'null' THEN ord.creratedon ELSE ord.modifiedon END  orderbydatecolumn, ord.*,cust.id,cust.customername FROM orders ord INNER JOIN customer cust ON ord.customerid=cust.Id ORDER BY orderbydatecolumn DESC ";

        Cursor c = sqLiteDatabase.rawQuery(presaleList_query,null);
        //Log.d("Order LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readJointTableOrderShipment() {
       String presaleList_query = "SELECT DISTINCT ord.order_id,ord.ordernumber,ord.submitstatus,ship.fulfillmentstatus FROM orders ord INNER JOIN shipment ship ON ord.order_id=ship.orderid order by ord.creratedon desc";
        Cursor c = sqLiteDatabase.rawQuery(presaleList_query,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readJointTableOrderInvoice() {

        String presaleList_query = "SELECT DISTINCT ord.order_id,ord.ordernumber,ord.submitstatus FROM orders ord where ord.order_id NOT IN(SELECT orderid FROM invoices WHERE Insubmitstatus='Paid') and ord.order_id NOT IN(SELECT orderid FROM invoices WHERE Insubmitstatus='Payment pending') and ord.submitstatus <> 'Fulfilled'and ord.submitstatus <> 'Cancelled' and ord.ordernumber not like 'DRFOM%' order by ord.creratedon desc";


       //String presaleList_query = "SELECT DISTINCT ord.order_id,ord.ordernumber,ord.submitstatus,invp.Insubmitstatus FROM orders ord INNER JOIN invoices invp ON ord.order_id=invp.orderid order by ord.creratedon desc";
        Cursor c = sqLiteDatabase.rawQuery(presaleList_query,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readJointTableInvoice() {
        
        //String InvoiceList_query = "SELECT inv.*,cust.id,cust.customername FROM invoices inv INNER JOIN customer cust ON inv.customerid=cust.Id order by  inv.createdon desc, inv.modifiedon desc";

        String InvoiceList_query = "SELECT CASE WHEN  inv.modifiedon = 'null' THEN inv.createdon ELSE inv.modifiedon END  orderbydatecolumn, inv.*,cust.id,cust.customername FROM invoices inv INNER JOIN customer cust ON inv.customerid=cust.Id ORDER BY orderbydatecolumn DESC ";

        Cursor c = sqLiteDatabase.rawQuery(InvoiceList_query,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableShipment() {
        String readAll = "select * from " + DbHandler.TABLE_SHIPMENT +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
      //  Log.d("SHIPMENT LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableInvoice() {
        String readAll = "select * from " + DbHandler.TABLE_INVOICE +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readShipTableItem(String tableName,String fieldName,String value) {
        String readAll = "select * from " + tableName + " where "+fieldName+"= '"+ value+"'" ;
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readOrderItem(String tableName,String fieldName,String value) {
        String readAll = "select * from " + tableName + " where "+fieldName+"= '"+ value+"'" ;
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("ORDER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableCustomer() {
        String readAll = "select * from " + DbHandler.TABLE_CUSTOMER +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }
    public Cursor readTableSalesRep() {
        String readAll = "select * from " + DbHandler.TABLE_SALESREP +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableDeliveryRep() {
        String readAll = "select * from " + DbHandler.TABLE_DELIVERYREP +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableFulfill() {
        String readAll = "select * from " + DbHandler.TABLE_FULFILLMENT +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
       // Log.d("FullFillment LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableWarehouse() {
        String readAll = "select * from " + DbHandler.TABLE_WAREHOUSE +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readAllTableProduct() {
        String readAll = "select * from " + DbHandler.TABLE_All_PRODUCT +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("Product LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableUser() {
        String readAll = "select * from " + DbHandler.TABLE_USER +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableMasterSync() {
        String readAll = "select * from " + DbHandler.TABLE_MASTER_SYNC +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableCancelledReason() {
        String readAll = "select * from " + DbHandler.TABLE_CANCELREASON +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableOrderStatus() {
        String readAll = "select * from " + DbHandler.TABLE_ORDERSTATUS +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
       // Log.d("OrderStatus", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableAddress() {
        String readAll = "select * from " + DbHandler.TABLE_ADDRESS +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableCurrency() {
        String readAll = "select * from " + DbHandler.TABLE_CURRENCY +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableCs_State() {
        String readAll = "select * from " + DbHandler.TABLE_CS_STATE +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableCs_Tax() {
        String readAll = "select * from " + DbHandler.TABLE_CS_TAX +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    public Cursor readTableTerms() {
        String readAll = "select * from " + DbHandler.TABLE_TERMS +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
        //Log.d("USER LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }


    public Cursor readTableInvoiceFormat() {
        String readAll = "select * from " + DbHandler.TABLE_INVOICE_FORMAT +" ;";
        Cursor c = sqLiteDatabase.rawQuery(readAll,null);
       // Log.d("TAX LIST", DatabaseUtils.dumpCursorToString(c));
        return c;
    }


    public long fetchSycCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_MASTER_SYNC;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_USER;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchSyncCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_MASTER_SYNC;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }
    public long fetch_All_PRODUCT_Count() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_All_PRODUCT;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }
    public long fetchInvoiceListCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_INVOICE;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchOrderCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_ORDER;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchInProductsCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_INVOICE_PRODUCTS;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchInvoiceLIstCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_INVOICE;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchShipmentCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_SHIPMENT;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchOrderProductsCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_ORDER_PRODUCT;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchCustomerCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_CUSTOMER;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchCancelReasonCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_CANCELREASON;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchCsStateCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_CS_STATE;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchCsTaxCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_CS_TAX;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchAddressCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_ADDRESS;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public long fetchInvoiceCount() {
        String sql = "SELECT COUNT(*) FROM "+ DbHandler.TABLE_INVOICE_FORMAT;
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public void UpdateSyncStatus(String UserId, String Status) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler.ORDER_SYNCSTATUS,Status);
        sqLiteDatabase.update(DbHandler.TABLE_ORDER,cv,"order_id="+"\""+UserId+"\"",null);
    }

    public void UpdateMasterSync(String name, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler.MASTER_SYNC_NAME,name);
        cv.put(DbHandler.MASTER_SYNC_TIME,date);
        sqLiteDatabase.update(DbHandler.TABLE_MASTER_SYNC,cv,"master_sync_name="+"\""+name+"\"",null);
    }

     public void updatePresaleOrder(String order_id, String Status,String cancellationreason,String comments) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler.ORDER_SUBMIT_STATUS,Status);
        cv.put(DbHandler.ORDER_CANCELLATION_REASON,cancellationreason);
        cv.put(DbHandler.ORDER_COMMENTS,comments);
        sqLiteDatabase.update(DbHandler.TABLE_ORDER,cv,"order_id="+"\""+order_id+"\"",null);
    }

    public void updatePresaleConfirmOrder(String order_id, String Status) {
        ContentValues cv = new ContentValues();
        cv.put(DbHandler.ORDER_SUBMIT_STATUS,Status);
        sqLiteDatabase.update(DbHandler.TABLE_ORDER,cv,"order_id="+"\""+order_id+"\"",null);
    }

    public void deleteTableUser() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_USER);
    }

    public void deleteTableCustomer() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_CUSTOMER);
    }

    public void deleteTableOrders() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ORDER);
    }

    public void deleteTableShipment() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_SHIPMENT);
    }

    public void deleteTableCancelReason() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_CANCELREASON);
    }

    public void deleteTableSalesRep() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_SALESREP);
    }
    public void deleteTableDeliverysRep() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_DELIVERYREP);
    }

    public void deleteTableOrdersProducts() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ORDER_PRODUCT);
    }

    public void deleteTableAddress() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ADDRESS);
    }

    public void deleteTableWarehouse() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_WAREHOUSE);
    }

    public void deleteTableAllProducts() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_All_PRODUCT);
    }


    public void deleteTableFulfilment() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_FULFILLMENT);
    }

    public void deleteTableInvoiceFormat() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE_FORMAT);
    }

    public void deleteTableInvoice() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE);
    }

    public void deleteTableInvoiceProducts() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE_PRODUCTS);
    }
    public void deleteTableTerms() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_TERMS);
    }

    public void deleteCS_STATE() {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_CS_STATE);
    }

    public void deleteProductItems(String Order_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ORDER_PRODUCT+ " where orderid= '"+ Order_id+"'");
    }

    public void deleteInvoiceProductsItems(String inID) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE_PRODUCTS+ " where invoiceid= '"+ inID+"'");
    }

    public void deleteProductIDItems(String pdID) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ORDER_PRODUCT+ " where pid= '"+ pdID+"'");
    }

    public void deleteAllProductIDItems(String pdID) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_All_PRODUCT+ " where all_product_id= '"+ pdID+"'");
    }

    public void deleteProductAssertIDItems(String AID) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_PRODUCT_ASSERT+ " where Products_assets_id= '"+ AID+"'");
    }

    public void deleteShipItems(String ship_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_SHIPMENT+ " where shipment_id= '"+ ship_id+"'");
    }

    public void deleteCustomerItems(String id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_CUSTOMER+ " where Id= '"+ id+"'");
    }

    public void deleteAddressItems(String add_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ADDRESS+ " where address_id= '"+ add_id+"'");
    }

    public void deleteInvoiceProductItems(String In_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE_PRODUCTS+ " where invoiceid= '"+ In_id+"'");
    }

    public void deleteOrder(String Or_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_ORDER+ " where order_id= '"+ Or_id+"'");
    }

    public void deleteInvoice(String In_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE+ " where id= '"+ In_id+"'");
    }

    public void deleteInvoiceProductsALL(String In_Pid) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_INVOICE_PRODUCTS+ " where id= '"+ In_Pid+"'");
    }

    public void deleteShipment(String Ors_id) {
        sqLiteDatabase.execSQL("delete from "+DbHandler.TABLE_SHIPMENT+ " where orderid= '"+ Ors_id+"'");
    }

}