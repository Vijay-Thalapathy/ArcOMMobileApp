package com.example.arcomdriver.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Oct 2022*/
public class DbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "DSD";
    private static final int DB_VERSION = 47;

    //Master
    public static final String TABLE_MASTER_SYNC = "master_sync";
    public static final String MASTER_SYNC_PRIMARY_ID = "master_sync_primary_id";
    public static final String MASTER_SYNC_ID = "master_sync_id";
    public static final String MASTER_SYNC_NAME = "master_sync_name";
    public static final String MASTER_SYNC_TIME = "master_sync_time";

    //User Details
    public static final String TABLE_USER = "user";
    public static final String USER_PRIMARY_ID = "user_primary_id";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_TOKEN = "token";
    public static final String USER_EMAIL = "Email";

    //CancelReason
    public static final String TABLE_CANCELREASON = "cancelreason";
    public static final String CANCELREASON_PRIMARY_ID = "cancelreason_primary_id";
    public static final String CANCELREASON_ID = "cancelreason_id";
    public static final String CANCELREASON_NAME = "cancelreason_name";

    //Product Assert
    public static final String TABLE_PRODUCT_ASSERT = "Productsassets";
    public static final String PRODUCT_ASSERT_PRIMARY_ID = "Products_assets_primary_id";
    public static final String PRODUCT_ASSERT_ID = "Products_assets_id";
    public static final String PRODUCT_ASSERT_NAME = "Products_assets_name";
    public static final String PRODUCT_ASSERT_PRODUCTSID = "Products_assets_productid";
    public static final String PRODUCT_ASSERT_IMAGE = "Products_assets_image";
    public static final String PRODUCT_ASSERT_FILE= "Products_assets_file";
    public static final String PRODUCT_ASSERT_TYPE= "Products_assets_type";
    public static final String PRODUCT_ASSERT_DEFAULT= "Products_assets_default";
    public static final String PRODUCT_ASSERT_ACTIVE= "Products_assets_active";

    //OrderStatus
    public static final String TABLE_ORDERSTATUS = "orderstatus";
    public static final String ORDERSTATUS_PRIMARY_ID = "orderstatus_primary_id";
    public static final String ORDERSTATUS_ID = "orderstatus_id";
    public static final String ORDERSTATUS_NAME = "orderstatus_name";

    //InvoiceFormat
    public static final String TABLE_INVOICE_FORMAT = "invoiceformat";
    public static final String INVOICE_PRIMARY_ID = "invoice_primary_id";
    public static final String INVOICE_ID = "invoice_id";
    public static final String INVOICE_NAME = "invoice_name";
    public static final String INVOICE_VALUE = "invoice_value";

    //CurrencyFormat
    public static final String TABLE_CURRENCY = "currency";
    public static final String CURRENCY_PRIMARY_ID = "currency_primary_id";
    public static final String CURRENCY_ID = "currency_id";
    public static final String CURRENCY_NAME = "currency_name";
    public static final String CURRENCY_SYMBOL = "currency_symbol";
    public static final String CURRENCY_Code = "currency_code";

    //Customer State
    public static final String TABLE_CS_STATE = "cs_state";
    public static final String CS_STATE_PRIMARY_ID = "cs_state_primary_id";
    public static final String CS_ID = "cs_id";
    public static final String CS_STATE_ID = "cs_state_id";
    public static final String CS_TAXBLE = "cs_taxable";
    public static final String CS_TAXID = "cs_taxid";
    public static final String CS_TERMSID = "cs_nettermsid";

    //Customer Tax
    public static final String TABLE_CS_TAX = "cs_tax";
    public static final String CS_TAX_PRIMARY_ID = "cs_tax_primary_id";
    public static final String CS_TAX_ID = "cs_tax_id";
    public static final String CS_TAX_PERCENT = "cs_tax_percent";
    public static final String CS_TAX_STATEID = "cs_tax_stateis";

    //Terms
    public static final String TABLE_TERMS = "terms";
    public static final String TERMS_PRIMARY_ID = "terms_primary_id";
    public static final String TERMS_ID = "terms_id";
    public static final String TERMS_NAME= "terms_name";
    public static final String TERMS_DAYS = "terms_days";

    //OrderProduct
    public static final String TABLE_ORDER_PRODUCT = "Orderproducts";
    public static final String ORDER_PRODUCT_PRIMARY_ID = "Orderproducts_primary_id";
    public static final String ORDER_PRO_ID = "pid";
    public static final String ORDER_PRODUCT_ORDERID = "orderid";
    public static final String ORDER_PRODUCT_ID = "productid";
    public static final String ORDER_PRODUCT_PRICEPER_UNIT = "priceperunit";
    public static final String ORDER_PRODUCT_BASEAMT = "baseamount";
    public static final String ORDER_PRODUCT_QTY = "quantity";
    public static final String ORDER_PRODUCT_NAME = "productname";
    public static final String ORDER_PRODUCT_TAX = "itemistaxable";
    public static final String ORDER_PRODUCT_WAREHOUSE = "warehouseid";

    //OrderStatus
    public static final String TABLE_ADDRESS = "address";
    public static final String ADDRESS_PRIMARY_ID = "address_primary_id";
    public static final String ADDRESS_ID = "address_id";
    public static final String ADDRESS_LINE1 = "line1";
    public static final String ADDRESS_LINE2 = "line2";
    public static final String ADDRESS_LINE3 = "line3";
    public static final String ADDRESS_SATE_PROVINCE = "stateorprovince";
    public static final String ADDRESS_TYPECODE = "addresstypecode";
    public static final String ADDRESS_COUNTRY = "country";
    public static final String ADDRESS_CITY = "city";
    public static final String ADDRESS_POSTAL = "postalcode";
    public static final String ADDRESS_POSTOFFICE = "postofficebox";
    public static final String ADDRESS_NAME = "name";
    public static final String ADDRESS_PRIMARY_ADD = "isprimaryaddress";
    public static final String ADDRESS_ISACTIVE = "isactive";
    public static final String ADDRESS_DOMAINEVENTS = "domainEvents";


    //SalesRep
    public static final String TABLE_SALESREP = "salesrep";
    public static final String SALESREP_PRIMARY_ID = "salesrep_primary_id";
    public static final String SALESREP_ID = "salesrep_id";
    public static final String SALESREP_NAME = "salesrep_name";

    //DeliveryRep
    public static final String TABLE_DELIVERYREP = "deliveryrep";
    public static final String DELIVERY_PRIMARY_ID = "delivery_rep_primary_id";
    public static final String DELIVERY_ID = "delivery_rep_id";
    public static final String DELIVERY_NAME = "delivery_rep_name";
    public static final String DELIVERY_EMAIL= "delivery_rep_email";

    //Fulfillment
    public static final String TABLE_FULFILLMENT = "fulfillment";
    public static final String FULFILLMENT_PRIMARY_ID = "fulfillment_primary_id";
    public static final String FULFILLMENT_ID = "fulfillment_id";
    public static final String FULFILLMENT_NAME = "fulfillment_name";
    public static final String FULFILLMENT_DEFAULT = "Full_isdefault";

    //Warehouse
    public static final String TABLE_WAREHOUSE = "warehouse";
    public static final String WAREHOUSE_PRIMARY_ID = "warehouse_primary_id";
    public static final String WAREHOUSE_ID = "warehouse_id";
    public static final String WAREHOUSE_NAME = "warehouse_name";

    //Invoice Product
    public static final String TABLE_INVOICE_PRODUCTS = "invoiceproduct";
    public static final String IN_PRODUCTS_PRIMARY_ID = "inproduct_primary_id";
    public static final String IN_PRODUCTS_ID = "id";
    public static final String IN_PRODUCTS_INVOICE_ID = "invoiceid";
    public static final String IN_PRODUCTS_TXNCURRENCY = "transactioncurrencyid";
    public static final String IN_PRODUCTS_UOMID = "uomid";
    public static final String IN_PRODUCTS_BASEAMT = "baseamount";
    public static final String IN_PRODUCTS_EXCHANGERATE = "exchangerate";
    public static final String IN_PRODUCTS_BASEAMT_BASE = "baseamountbase";
    public static final String IN_PRODUCTS_DEC = "description";
    public static final String IN_PRODUCTS_EXTENDAMT = "extendedamount";
    public static final String IN_PRODUCTS_EXTENDAMT_BASE = "extendedamountbase";
    public static final String IN_PRODUCTS_ISCOPIED = "iscopied";
    public static final String IN_PRODUCTS_ISPRICEOVER = "ispriceoverridden";
    public static final String IN_PRODUCTS_ISPRODUCT_OVER = "isproductoverridden";
    public static final String IN_PRODUCTS_LINEITEM_NUM = "lineitemnumber";
    public static final String IN_PRODUCTS_MANUAL_DISCOUNT = "manualdiscountamount";
    public static final String IN_PRODUCTS_MANUAL_DISAMT_BASE = "manualdiscountamountbase";
    public static final String IN_PRODUCTS_PARENT_BUNDLEID = "parentbundleid";
    public static final String IN_PRODUCTS_PRODUCT_ASSOCIATION = "productassociationid";
    public static final String IN_PRODUCTS_PRODUCTTYPECODE = "producttypecode";
    public static final String IN_PRODUCTS_PRICEPERUNIT = "priceperunit";
    public static final String IN_PRODUCTS_PRICEPER_UNITBASE = "priceperunitbase";
    public static final String IN_PRODUCTS_PRICING_ERRORCODE = "pricingerrorcode";
    public static final String IN_PRODUCTS_PRODUCT_DEC = "productdescription";
    public static final String IN_PRODUCTS_PRODUCT_NAME = "productname";
    public static final String IN_PRODUCTS_PRODUCTID = "productid";
    public static final String IN_PRODUCTS_QTY = "quantity";
    public static final String IN_PRODUCTS_QTY_BACK = "quantitybackordered";
    public static final String IN_PRODUCTS_QTY_CANCELLED = "quantitycancelled";
    public static final String IN_PRODUCTS_QTY_SHIPPED = "quantityshipped";
    public static final String IN_PRODUCTS_REQUEST_DELIVERY = "requestdeliveryby";
    public static final String IN_PRODUCTS_SALESORDER_LOCK = "salesorderispricelocked";
    public static final String IN_PRODUCTS_SALES_STATECODE = "salesorderstatecode";
    public static final String IN_PRODUCTS_SALES_REPID = "salesrepid";
    public static final String IN_PRODUCTS_SHIPTOADDRESSID = "shiptoaddressid";
    public static final String IN_PRODUCTS_TAX = "tax";
    public static final String IN_PRODUCTS_TAX_BASE = "taxbase";
    public static final String IN_PRODUCTS_VOLUME_DISAMT = "volumediscountamount";
    public static final String IN_PRODUCTS_VOLUME_DIS_AMTBASE = "volumediscountamountbase";
    public static final String IN_PRODUCTS_WILLCALL = "willcall";
    public static final String IN_PRODUCTS_SEQUENCENUM = "sequencenumber";
    public static final String IN_PRODUCTS_QUOTE_DETAIL = "quotedetailid";
    public static final String IN_PRODUCTS_SALES_ORDER = "salesorderdetailname";
    public static final String IN_PRODUCTS_UUID = "uuid";
    public static final String IN_PRODUCTS_ISDISABLED = "isdisabled";
    public static final String IN_PRODUCTS_DISABLEBY = "disabledby";
    public static final String IN_PRODUCTS_DISABLEBEHALF = "disabledbehalfof";
    public static final String IN_PRODUCTS_ISDELETED = "isdeleted";
    public static final String IN_PRODUCTS_DELETEBY = "deletedby";
    public static final String IN_PRODUCTS_DELETE_BEHALF = "deletedbehalfof";
    public static final String IN_PRODUCTS_DELETEDON = "deletedon";
    public static final String IN_PRODUCTS_CREATEDBY = "createdby";
    public static final String IN_PRODUCTS_CREATEDBEHALF = "createdbehalfof";
    public static final String IN_PRODUCTS_CREATEDON = "createdon";
    public static final String IN_PRODUCTS_MODIFIYBY = "modifiedby";
    public static final String IN_PRODUCTS_MODIFIEDBEHALF = "modifiedbehalfof";
    public static final String IN_PRODUCTS_MODIFIYDON = "modifiedon";
    public static final String IN_PRODUCTS_AGENT = "agent";
    public static final String IN_PRODUCTS_DATAFROM_TYPEID = "datafromtypeid";
    public static final String IN_PRODUCTS_DATAFROM_ID = "datafromid";
    public static final String IN_PRODUCTS_TENANTID = "tenantid";
    public static final String IN_PRODUCTS_ISACTIVE = "isactive";
    public static final String IN_PRODUCTS_QTY_PICKED = "quantitypicked";
    public static final String IN_PRODUCTS_QTY_PACKED = "quantitypacked";
    public static final String IN_PRODUCTS_ORDERPID = "orderpid";
    public static final String IN_PRODUCTS_EXTERNAL_ID = "externalid";
    public static final String IN_PRODUCTS_EXTERNAL_STATUS = "externalstatus";
    public static final String IN_PRODUCTS_ITEMS_TAX = "itemistaxable";
    public static final String IN_PRODUCTS_ORDERID = "itemorderid";
    public static final String IN_PRODUCTS_WAREHOUSE = "itemwarehouseid";

    //Invoice
    public static final String TABLE_INVOICE = "invoices";
    public static final String INVOICES_PRIMARY_ID = "invoices_primary_id";
    public static final String INVOICES_DEFAULT_ID = "id";
    public static final String INVOICES_ORDER_ID = "orderid";
    public static final String INVOICES_PAYMENTTYPE_ID = "paymenttypeid";
    public static final String INVOICES_PAYMENT_ID = "paymentid";
    public static final String INVOICES_PAYMENT_METHOD = "paymentmethod";
    public static final String INVOICES_NAME = "name";
    public static final String INVOICES_BILLTOADDRESS_ID = "billtoaddressid";
    public static final String INVOICES_CUSTOMER_ID = "customerid";
    public static final String INVOICES_CUSTOMERID_TYPE = "customeridtype";
    public static final String INVOICES_DATEFULFILLED = "datefulfilled";
    public static final String INVOICES_DESCRIPTION = "description";
    public static final String INVOICES_DISCOUNTAMT = "discountamount";
    public static final String INVOICES_TIMESTAMP = "timestamp";
    public static final String INVOICES_TRANSACTION_CURRENCY = "transactioncurrencyid";
    public static final String INVOICES_EXCHANGE_RATE = "exchangerate";
    public static final String INVOICES_DISCOUNTAMT_BASE = "discountamountbase";
    public static final String INVOICES_DISCOUNT_PERCENTAGE = "discountpercentage";
    public static final String INVOICES_FREIGHTAMOUNT = "freightamount";
    public static final String INVOICES_FRIGHTAMT_BASE = "freightamountbase";
    public static final String INVOICES_FRIGHTTERMS_CODE = "freighttermscode";
    public static final String INVOICES_ISPRICE_LOCKED = "ispricelocked";
    public static final String INVOICES_LASTBACKOFFICE = "lastbackofficesubmit";
    public static final String INVOICES_OPPORTUNITY_ID = "opportunityid";
    public static final String INVOICES_ORDERNUMBER = "ordernumber";
    public static final String INVOICES_PAYMENTTERMS_CODE = "paymenttermscode";
    public static final String INVOICES_PAYMENTTERMS_DISPLAY = "paymenttermscode_display";
    public static final String INVOICES_PRICELEVELID = "pricelevelid";
    public static final String INVOICES_PRICEERROR_CODE = "pricingerrorcode";
    public static final String INVOICES_PRIORITY_CODE = "prioritycode";
    public static final String INVOICES_QUOTE_ID = "quoteid";
    public static final String INVOICES_REQUEST_DELIVERY = "requestdeliveryby";
    public static final String INVOICES_SHIPPING_CODE = "shippingmethodcode";
    public static final String INVOICES_SHIPPING_DISPLAY = "shippingmethodcode_display";
    public static final String INVOICES_SHIPTOADDRESS_ID = "shiptoaddressid";
    public static final String INVOICES_STATECODE = "statecode";
    public static final String INVOICES_STATUS_CODE = "statuscode";
    public static final String INVOICES_DATE = "invoicedate";
    public static final String INVOICES_SUBMIT_STATUS = "Insubmitstatus";
    public static final String INVOICES_SUBMIT_DESC = "submitstatusdescription";
    public static final String INVOICES_TOTAL_AMT = "totalamount";
    public static final String INVOICES_TOTALAMT_BASE = "totalamountbase";
    public static final String INVOICES_TOTALAMT_FREIGHT = "totalamountlessfreight";
    public static final String INVOICES_TOTALAMT_FRIGHTBASE = "totalamountlessfreightbase";
    public static final String INVOICES_TOTALDISCOUNT_AMT = "totaldiscountamount";
    public static final String INVOICES_TOTALDISCOUNT_AMTBASE = "totaldiscountamountbase";
    public static final String INVOICES_TOTALLINE_ITEMAMT = "totallineitemamount";
    public static final String INVOICES_TOTALLINEITEM_AMTBASE = "totallineitemamountbase";
    public static final String INVOICES_TOTALLINEITEM_DISCOUNTBASE = "totallineitemdiscountamount";
    public static final String INVOICES_TOTALLINEITEM_DISCOUNT_AMTBASE = "totallineitemdiscountamountbase";
    public static final String INVOICES_TOTALTAX = "totaltax";
    public static final String INVOICES_TOTALTAX_BASE = "totaltaxbase";
    public static final String INVOICES_WILLCALL = "willcall";
    public static final String INVOICES_ISDISABLE = "isdisabled";
    public static final String INVOICES_DISABLEBY = "disabledby";
    public static final String INVOICES_DISABLE_BEHALF = "disabledbehalfof";
    public static final String INVOICES_ISDELETED = "isdeleted";
    public static final String INVOICES_DELETEBY = "deletedby";
    public static final String INVOICES_DELETE_BEHALF = "deletedbehalfof";
    public static final String INVOICES_DELETEDON = "deletedon";
    public static final String INVOICES_CREATEBY = "createdby";
    public static final String INVOICES_CREATEDBEHALF = "createdbehalfof";
    public static final String INVOICES_CREATEDON = "createdon";
    public static final String INVOICES_MODIFIEDBY = "modifiedby";
    public static final String INVOICES_MODIFIED_BEHALF = "modifiedbehalfof";
    public static final String INVOICES_MODIFIDON = "modifiedon";
    public static final String INVOICES_AGENT = "agent";
    public static final String INVOICES_SALESREPID = "salesrepid";
    public static final String INVOICES_PRICINGDATE = "pricingdate";
    public static final String INVOICES_DATAFROMTYPEID = "datafromtypeid";
    public static final String INVOICES_DATAFROM_ID = "datafromid";
    public static final String INVOICES_TENANID = "tenantid";
    public static final String INVOICES_ISACTIVE = "isactive";
    public static final String INVOICES_DELIVERYNOTE = "deliverynote";
    public static final String INVOICES_SHIP = "shipnote";
    public static final String INVOICES_TERMS = "termsconditions";
    public static final String INVOICES_MEMO = "memo";
    public static final String INVOICES_CANCELREASON = "cancellationreason";
    public static final String INVOICES_COMMENTS = "comments";
    public static final String INVOICES_CANCEL_DATE = "cancellationDate";
    public static final String INVOICES_REFRENCE_ORDER = "referenceorder";
    public static final String INVOICES_VENDORID = "vendorid";
    public static final String INVOICES_EXTERNALID = "externalid";
    public static final String INVOICES_EXTERNAL_STATUS = "externalstatus";
    public static final String INVOICES_WAREHOUSE_ID = "warehouseid";
    public static final String INVOICES_CUSTOMER_ISTAXABLE = "customeristaxable";
    public static final String INVOICES_DRAFT_NUMBER = "draftnumber";
    public static final String INVOICES_LASTSYNCON = "lastsyncon";
    public static final String INVOICES_NETSTATUS= "netStatus";
    public static final String INVOICES_SYNCSTATUS= "SyncStatus";
    public static final String INVOICES_TAXID= "in_taxid";
    public static final String INVOICES_TERMSID= "in_terms";
    public static final String INVOICES_DUEDATE= "in_due";

    //All product
    public static final String TABLE_All_PRODUCT = "all_product";
    public static final String All_PRODUCT_PRIMARY_ID = "all_product_primary_id";
    public static final String All_PRODUCT_ID = "all_product_id";
    public static final String All_PRODUCT_NUMBER = "all_product_number";
    public static final String All_PRODUCT_NAME = "all_product_name";
    public static final String All_PRODUCT_PRICE= "all_product_price";
    public static final String All_PRODUCT_STATUS= "all_product_status";
    public static final String All_PRODUCT_IMG= "all_product_img";
    public static final String All_PRODUCT_UPSC= "all_upsc";
    public static final String All_PRODUCT_ISTAX= "all_istax";
    public static final String All_PRODUCT_DES= "all_description";

    //Orders Details
    public static final String TABLE_ORDER = "orders";
    public static final String ORDER_PRIMARY_ID = "order_primary_id";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_NAME= "order_name";
    public static final String ORDER_BILL_ADDRESS= "billtoaddressid";
    public static final String ORDER_CUSTOMER_ID= "customerid";
    public static final String ORDER_CUSTOMER_ID_TYPE= "customeridtype";
    public static final String ORDER_DATEFUL= "datefulfilled";
    public static final String ORDER_DESCRIPTION= "description";
    public static final String ORDER_DISCOUNT_AMOUNT= "discountamount";
    public static final String ORDER_TIMESTAMP= "timestamp";
    public static final String ORDER_TRANSACTION_CURRENCY= "transactioncurrencyid";
    public static final String ORDER_EXACHANGE_RATE= "exchangerate";
    public static final String ORDER_DISCOUNT_BASE= "discountamountbase";
    public static final String ORDER_DISCOUNT_PERCENTAGE= "discountpercentage";
    public static final String ORDER_FREIGHT_AMOUNT= "freightamount";
    public static final String ORDER_FREIGHT_BASE= "freightamountbase";
    public static final String ORDER_FREIGHT_TERMS= "freighttermscode";
    public static final String ORDER_PRICELOCKED= "ispricelocked";
    public static final String ORDER_LAST_BACKOFF= "lastbackofficesubmit";
    public static final String ORDER_OPPORTUNITY_ID= "opportunityid";
    public static final String ORDER_NUMBER= "ordernumber";
    public static final String ORDER_PAYMENT_TERMSCODE= "paymenttermscode";
    public static final String ORDER_PAYMENT_DISPLAY= "paymenttermscode_display";
    public static final String ORDER_PRICE_LEVEL_ID= "pricelevelid";
    public static final String ORDER_PRICE_ERORRCODE= "pricingerrorcode";
    public static final String ORDER_PRIORITY_CODE= "prioritycode";
    public static final String ORDER_QUOTEID= "quoteid";
    public static final String ORDER_REQUEST_DELIVERBY= "requestdeliveryby";
    public static final String ORDER_SHIPPING_CODE= "shippingmethodcode";
    public static final String ORDER_SHIPPING_CODEDISPLAY= "shippingmethodcode_display";
    public static final String ORDER_SHIPPING_ADDRESSID= "shiptoaddressid";
    public static final String ORDER_STATECODE= "statecode";
    public static final String ORDER_SUBMIT_DATE= "submitdate";
    public static final String ORDER_SUBMIT_STATUS= "submitstatus";
    public static final String ORDER_SUBMIT_DESCRIPTION= "submitstatusdescription";
    public static final String ORDER_TOTAL_AMOUNT= "totalamount";
    public static final String ORDER_TOTAL_AMOUNT_BASE= "totalamountbase";
    public static final String ORDER_TOTAL_AMOUNT_LESS_FREIGHT= "totalamountlessfreight";
    public static final String ORDER_TOTAL_AMOUNT_LESS_FREIGHT_BASE= "totalamountlessfreightbase";
    public static final String ORDER_TOTAL_DISCOUNT_AMOUNT= "totaldiscountamount";
    public static final String ORDER_TOTAL_DISCOUNT_AMOUNT_BASE= "totaldiscountamountbase";
    public static final String ORDER_TOTAL_LINE_ITEM_AMOUNT= "totallineitemamount";
    public static final String ORDER_TOTAL_LINE_ITEM_AMOUNT_BASE= "totallineitemamountbase";
    public static final String ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT= "totallineitemdiscountamount";
    public static final String ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT_BASE= "totallineitemdiscountamountbase";
    public static final String ORDER_TOTAL_TAX= "totaltax";
    public static final String ORDER_TOTAL_TAX_BASE= "totaltaxbase";
    public static final String ORDER_WILL_CALL= "willcall";
    public static final String ORDER_ISDISABLED= "isdisabled";
    public static final String ORDER_DISABLED_BY= "disabledby";
    public static final String ORDER_DISABLED_BEHALFOF= "disabledbehalfof";
    public static final String ORDER_ISDELETED= "isdeleted";
    public static final String ORDER_DELETED_BY= "deletedby";
    public static final String ORDER_DELETED_BEHALFOF= "deletedbehalfof";
    public static final String ORDER_DELETED_DON= "deletedon";
    public static final String ORDER_CREATED_BY= "createdby";
    public static final String ORDER_CREATED_BEHALFOF= "createdbehalfof";
    public static final String ORDER_CREATED_DON= "creratedon";
    public static final String ORDER_MODIFIED_BY= "modifiedby";
    public static final String ORDER_MODIFIED_BEHALFOF= "modifiedbehalfof";
    public static final String ORDER_MODIFIED_DON= "modifiedon";
    public static final String ORDER_AGENT= "agent";
    public static final String ORDER_SALES_REPID= "salesrepid";
    public static final String ORDER_PRICING_DATE= "pricingdate";
    public static final String ORDER_DATE_FROM_TYPE_ID= "datafromtypeid";
    public static final String ORDER_DATE_FROM_ID= "datafromid";
    public static final String ORDER_TENANT_ID= "tenantid";
    public static final String ORDER_ISACTIVE= "isactive";
    public static final String ORDER_DELIVERY_NOTE= "deliverynote";
    public static final String ORDER_SHIPNOTE= "shipnote";
    public static final String ORDER_TERMS_CONDITIONS= "termsconditions";
    public static final String ORDER_MEMO= "memo";
    public static final String ORDER_CANCELLATION_REASON= "cancellationreason";
    public static final String ORDER_COMMENTS= "comments";
    public static final String ORDER_CANCELLATION_DATE= "cancellationDate";
    public static final String ORDER_PO_REFERENCE_NUM= "poreferencenum";
    public static final String ORDER_CONFIRMED_BY= "confirmedby";
    public static final String ORDER_CONFIRMED_DATE= "confirmeddate";
    public static final String ORDER_NOTES= "notes";
    public static final String ORDER_WAREHOUSE_ID= "warehouseid";
    public static final String ORDER_CUSTOMERTAXSTATUS= "customeristaxable";
    public static final String ORDER_NETSTATUS= "netStatus";
    public static final String ORDER_SYNCSTATUS= "SyncStatus";
    public static final String ORDER_LASTSYNCON= "lastsyncon";
    public static final String ORDER_DRAFTNUM= "draftnumber";
    public static final String ORDER_SHIPMENTTYPE= "shipmenttypeid";
    public static final String ORDER_TAXID= "or_taxId";
    public static final String ORDER_TERMSID= "or_termsId";
    public static final String ORDER_DUEDATE= "or_dueDate";

    //Shipment Details
    public static final String TABLE_SHIPMENT = "shipment";
    public static final String SHIP_PRIMARY_ID = "ship_primary_id";
    public static final String SHIP_ID = "shipment_id";
    public static final String SHIP_ORDER_ID = "orderid";
    public static final String SHIP_TYPE_ID = "shipmenttypeid";
    public static final String SHIP_WAREHOUSE_ID = "warehouseid";
    public static final String SHIP_ROUTE_NUM = "routenum";
    public static final String SHIP_ROUTE_DATE = "routedate";
    public static final String SHIP_TRUCK = "truck";
    public static final String SHIP_DRIVER_ID = "driverid";
    public static final String SHIP_CARRIER_ID = "carrierid";
    public static final String SHIPPED_DATE = "shippeddate";
    public static final String SHIP_TRACK_NUM = "trackingnumber";
    public static final String SHIP_DELIVERY_DATE = "deliverydate";
    public static final String SHIP_DELIVERY_TIME = "deliverytime";
    public static final String SHIP_IS_ACTIVE = "isactive";
    public static final String SHIP_UUID = "uuid";
    public static final String SHIP_IS_DISABLED = "isdisabled";
    public static final String SHIP_IS_DISABLED_BY = "disabledby";
    public static final String SHIP_DISBLED_BEHALFOF = "disabledbehalfof";
    public static final String SHIP_IS_DELETED = "isdeleted";
    public static final String SHIP_DELTED_BY = "deletedby";
    public static final String SHIP_DELETED_BEHALFOF = "deletedbehalfof";
    public static final String SHIP_DELETE_DON = "deletedon";
    public static final String SHIP_CREATE_BY = "createdby";
    public static final String SHIP_CREATED_BEHALFOF = "createdbehalfof";
    public static final String SHIP_CREATION_DON = "creratedon";
    public static final String SHIP_MODIFY_BY = "modifiedby";
    public static final String SHIP_MODIFIED_BEHALF = "modifiedbehalfof";
    public static final String SHIP_MODIFIED_DON = "modifiedon";
    public static final String SHIP_DATA_TYPE_ID = "datafromtypeid";
    public static final String SHIP_DATAFROM_ID = "datafromid";
    public static final String SHIP_TENAN_ID = "tenantid";
    public static final String SHIP_FULFILLMENTS_STATUS = "fulfillmentstatus";
    public static final String SHIP_PICKER_ID = "pickerid";
    public static final String SHIP_PACKER_ID = "packerid";
    public static final String SHIPPER_ID = "shipperid";
    public static final String SHIP_INVOICER_ID = "invoicerid";
    public static final String SHIP_PAYMENT_ID = "paymenterid";
    public static final String SHIP_DELIVERY_AGENT_ID = "deliveryagentid";
    public static final String SHIP_DELIVER_TYPE_ID = "deliveryshipmenttypeid";
    public static final String SHIP_ROUTE_DAY_ID = "routedayid";
    public static final String SHIP_ROUTE_ID = "routeid";
    public static final String SHIP_TRUCK_ID = "truckid";
    public static final String SHIP_INVOICE_ID = "invoiceid";
    public static final String SHIP_WORKORDER_ID = "workorderid";
    public static final String SHIP_PURCHASE_ORDER_ID = "purchaseorderid";
    public static final String SHIP_UNDELIVER_REASON_ID = "undeliveredreasonid";
    public static final String SHIP_UNDELIVER_REASON = "undeliveredreason";

    //Customer
    public static final String TABLE_CUSTOMER = "customer";
    public static final String CUSTOMER_PRIMARY_ID = "customer_primary_id";
    public static final String CUSTOMER_ID = "Id";
    public static final String CUSTOMER_NAME = "customername";
    public static final String CUSTOMER_TYPE_ID = "customertypeid";
    public static final String CUSTOMER_STATUS = "status";
    public static final String CUSTOMER_BUSINESS_NAME = "businessname";
    public static final String CUSTOMER_VAT_NUMBER = "vatnumber";
    public static final String CUSTOMER_GST_NUMBER = "gstnumber";
    public static final String CUSTOMER_PAN_NUMBER = "pannumber";
    public static final String CUSTOMER_CUSTOMER_URL = "customerurl";
    public static final String CUSTOMER_VENDOR_ID = "vendorid";
    public static final String CUSTOMER_TAXI_APPLICABLE = "taxisapplicable";
    public static final String CUSTOMER_BILLING_ADDRESS_ID = "billingaddressid";
    public static final String CUSTOMER_SHIPPING_ADDRESS_ID = "shippingaddressid";
    public static final String CUSTOMER_DELIVERY_ADDRESS_ID = "deliveryaddressid";
    public static final String CUSTOMER_IS_ACTIVE = "isactive";
    public static final String CUSTOMER_UUID = "uuid";
    public static final String CUSTOMER_IS_DISABLED = "isdisabled";
    public static final String CUSTOMER_DISABLED_BY = "disabledby";
    public static final String CUSTOMER_DISABLED_BEHALFOF = "disabledbehalfof";
    public static final String CUSTOMER_IS_DELETED = "isdeleted";
    public static final String CUSTOMER_DELETED_BY = "deletedby";
    public static final String CUSTOMER_DELETED_BEHALFOF = "deletedbehalfof";
    public static final String CUSTOMER_DELETE_DON = "deletedon";
    public static final String CUSTOMER_CREATE_BY = "createdby";
    public static final String CUSTOMER_CREATE_BEHALFOF = "createdbehalfof";
    public static final String CUSTOMER_CREATE_DON = "createdon";
    public static final String CUSTOMER_MODIFIED_BY = "modifiedby";
    public static final String CUSTOMER_MODIFIED_BEHALFOF = "modifiedbehalfof";
    public static final String CUSTOMER_MODIFIED_ON = "modifiedon";
    public static final String CUSTOMER_AGENT = "agent";
    public static final String CUSTOMER_DATAFROM_TYPE_ID = "datafromtypeid";
    public static final String CUSTOMER_DATAFROM_ID = "datafromid";
    public static final String CUSTOMER_TENANT_ID = "tenantid";
    public static final String CUSTOMER_WEBSITE_ID = "website";
    public static final String CUSTOMER_INDUSTRY_ID = "industry";
    public static final String CUSTOMER_IS_PRIMARY_ADDRESS = "isprimaryaddress";
    public static final String CUSTOMER_ISID = "sid";
    public static final String CUSTOMER_NUMBER = "customernumber";
    public static final String CUSTOMER_EXTERNAL_ID = "externalid";
    public static final String CUSTOMER_EXTERNAL_UPDATE = "externalupdated";
    public static final String CUSTOMER_ISTAXABLE = "istaxable";
    public static final String CUSTOMER_TAXID = "taxID";
    public static final String CUSTOMER_NETTERMS = "netTermsID";

    public static String CREATE_TABLE_CUSTOMER = "create table " +
            TABLE_CUSTOMER+ "(" +CUSTOMER_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CUSTOMER_ID+ " TEXT NOT NULL," +
            CUSTOMER_NAME+ " TEXT NOT NULL," +
            CUSTOMER_TYPE_ID+ " TEXT NOT NULL," +
            CUSTOMER_STATUS+ " TEXT NOT NULL," +
            CUSTOMER_BUSINESS_NAME+ " TEXT NOT NULL," +
            CUSTOMER_VAT_NUMBER+ " TEXT NOT NULL," +
            CUSTOMER_GST_NUMBER+ " TEXT NOT NULL," +
            CUSTOMER_PAN_NUMBER+ " TEXT NOT NULL," +
            CUSTOMER_CUSTOMER_URL+ " TEXT NOT NULL," +
            CUSTOMER_VENDOR_ID+ " TEXT NOT NULL," +
            CUSTOMER_TAXI_APPLICABLE+ " TEXT NOT NULL," +
            CUSTOMER_BILLING_ADDRESS_ID+ " TEXT NOT NULL," +
            CUSTOMER_SHIPPING_ADDRESS_ID+ " TEXT NOT NULL," +
            CUSTOMER_DELIVERY_ADDRESS_ID+ " TEXT NOT NULL," +
            CUSTOMER_IS_ACTIVE+ " TEXT NOT NULL," +
            CUSTOMER_UUID+ " TEXT NOT NULL," +
            CUSTOMER_IS_DISABLED+ " TEXT NOT NULL," +
            CUSTOMER_DISABLED_BY+ " TEXT NOT NULL," +
            CUSTOMER_DISABLED_BEHALFOF+ " TEXT NOT NULL," +
            CUSTOMER_IS_DELETED+ " TEXT NOT NULL," +
            CUSTOMER_DELETED_BY+ " TEXT NOT NULL," +
            CUSTOMER_DELETED_BEHALFOF+ " TEXT NOT NULL," +
            CUSTOMER_DELETE_DON+ " TEXT NOT NULL," +
            CUSTOMER_CREATE_BY+ " TEXT NOT NULL," +
            CUSTOMER_CREATE_BEHALFOF+ " TEXT NOT NULL," +
            CUSTOMER_CREATE_DON+ " TEXT NOT NULL," +
            CUSTOMER_MODIFIED_BY+ " TEXT NOT NULL," +
            CUSTOMER_MODIFIED_BEHALFOF+ " TEXT NOT NULL," +
            CUSTOMER_MODIFIED_ON+ " TEXT NOT NULL," +
            CUSTOMER_AGENT+ " TEXT NOT NULL," +
            CUSTOMER_DATAFROM_TYPE_ID+ " TEXT NOT NULL," +
            CUSTOMER_DATAFROM_ID+ " TEXT NOT NULL," +
            CUSTOMER_TENANT_ID+ " TEXT NOT NULL," +
            CUSTOMER_WEBSITE_ID+ " TEXT NOT NULL," +
            CUSTOMER_INDUSTRY_ID+ " TEXT NOT NULL," +
            CUSTOMER_IS_PRIMARY_ADDRESS+ " TEXT NOT NULL," +
            CUSTOMER_ISID+ " TEXT NOT NULL," +
            CUSTOMER_NUMBER+ " TEXT NOT NULL," +
            CUSTOMER_EXTERNAL_ID+ " TEXT NOT NULL," +
            CUSTOMER_EXTERNAL_UPDATE+ " TEXT NOT NULL," +
            CUSTOMER_ISTAXABLE+ " TEXT NOT NULL," +
            CUSTOMER_TAXID+ " TEXT NOT NULL," +
            CUSTOMER_NETTERMS+ " TEXT NOT NULL);";


    public static String CREATE_TABLE_ORDERS = "create table " +TABLE_ORDER+ "(" +ORDER_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ORDER_ID+ " TEXT NOT NULL," +
            ORDER_NAME+ " TEXT NOT NULL," +
            ORDER_BILL_ADDRESS+ " TEXT NOT NULL," +
            ORDER_CUSTOMER_ID+ " TEXT NOT NULL," +
            ORDER_CUSTOMER_ID_TYPE+ " TEXT NOT NULL," +
            ORDER_DATEFUL+ " TEXT NOT NULL," +
            ORDER_DESCRIPTION+ " TEXT NOT NULL," +
            ORDER_DISCOUNT_AMOUNT+ " TEXT NOT NULL," +
            ORDER_TIMESTAMP+ " TEXT NOT NULL," +
            ORDER_TRANSACTION_CURRENCY+ " TEXT NOT NULL," +
            ORDER_EXACHANGE_RATE+ " TEXT NOT NULL," +
            ORDER_DISCOUNT_BASE+ " TEXT NOT NULL," +
            ORDER_DISCOUNT_PERCENTAGE+ " TEXT NOT NULL," +
            ORDER_FREIGHT_AMOUNT+ " TEXT NOT NULL," +
            ORDER_FREIGHT_BASE+ " TEXT NOT NULL," +
            ORDER_FREIGHT_TERMS+ " TEXT NOT NULL," +
            ORDER_PRICELOCKED+ " TEXT NOT NULL," +
            ORDER_LAST_BACKOFF+ " TEXT NOT NULL," +
            ORDER_OPPORTUNITY_ID+ " TEXT NOT NULL," +
            ORDER_NUMBER+ " TEXT NOT NULL," +
            ORDER_PAYMENT_TERMSCODE+ " TEXT NOT NULL," +
            ORDER_PAYMENT_DISPLAY+ " TEXT NOT NULL," +
            ORDER_PRICE_LEVEL_ID+ " TEXT NOT NULL," +
            ORDER_PRICE_ERORRCODE+ " TEXT NOT NULL," +
            ORDER_PRIORITY_CODE+ " TEXT NOT NULL," +
            ORDER_QUOTEID+ " TEXT NOT NULL," +
            ORDER_REQUEST_DELIVERBY+ " TEXT NOT NULL," +
            ORDER_SHIPPING_CODE+ " TEXT NOT NULL," +
            ORDER_SHIPPING_CODEDISPLAY+ " TEXT NOT NULL," +
            ORDER_SHIPPING_ADDRESSID+ " TEXT NOT NULL," +
            ORDER_STATECODE+ " TEXT NOT NULL," +
            ORDER_SUBMIT_DATE+ " TEXT NOT NULL," +
            ORDER_SUBMIT_STATUS+ " TEXT NOT NULL," +
            ORDER_SUBMIT_DESCRIPTION+ " TEXT NOT NULL," +
            ORDER_TOTAL_AMOUNT+ " TEXT NOT NULL," +
            ORDER_TOTAL_AMOUNT_BASE+ " TEXT NOT NULL," +
            ORDER_TOTAL_AMOUNT_LESS_FREIGHT+ " TEXT NOT NULL," +
            ORDER_TOTAL_AMOUNT_LESS_FREIGHT_BASE+ " TEXT NOT NULL," +
            ORDER_TOTAL_DISCOUNT_AMOUNT+ " TEXT NOT NULL," +
            ORDER_TOTAL_DISCOUNT_AMOUNT_BASE+ " TEXT NOT NULL," +
            ORDER_TOTAL_LINE_ITEM_AMOUNT+ " TEXT NOT NULL," +
            ORDER_TOTAL_LINE_ITEM_AMOUNT_BASE+ " TEXT NOT NULL," +
            ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT+ " TEXT NOT NULL," +
            ORDER_TOTAL_LINE_ITEM_DISCOUNT_AMOUNT_BASE+ " TEXT NOT NULL," +
            ORDER_TOTAL_TAX+ " TEXT NOT NULL," +
            ORDER_TOTAL_TAX_BASE+ " TEXT NOT NULL," +
            ORDER_WILL_CALL+ " TEXT NOT NULL," +
            ORDER_ISDISABLED+ " TEXT NOT NULL," +
            ORDER_DISABLED_BY+ " TEXT NOT NULL," +
            ORDER_DISABLED_BEHALFOF+ " TEXT NOT NULL," +
            ORDER_ISDELETED+ " TEXT NOT NULL," +
            ORDER_DELETED_BY+ " TEXT NOT NULL," +
            ORDER_DELETED_BEHALFOF+ " TEXT NOT NULL," +
            ORDER_DELETED_DON+ " TEXT NOT NULL," +
            ORDER_CREATED_BY+ " TEXT NOT NULL," +
            ORDER_CREATED_BEHALFOF+ " TEXT NOT NULL," +
            ORDER_CREATED_DON+ " TEXT NOT NULL," +
            ORDER_MODIFIED_BY+ " TEXT NOT NULL," +
            ORDER_MODIFIED_BEHALFOF+ " TEXT NOT NULL," +
            ORDER_MODIFIED_DON+ " TEXT NOT NULL," +
            ORDER_AGENT+ " TEXT NOT NULL," +
            ORDER_SALES_REPID+ " TEXT NOT NULL," +
            ORDER_PRICING_DATE+ " TEXT NOT NULL," +
            ORDER_DATE_FROM_TYPE_ID+ " TEXT NOT NULL," +
            ORDER_DATE_FROM_ID+ " TEXT NOT NULL," +
            ORDER_TENANT_ID+ " TEXT NOT NULL," +
            ORDER_ISACTIVE+ " TEXT NOT NULL," +
            ORDER_DELIVERY_NOTE+ " TEXT NOT NULL," +
            ORDER_SHIPNOTE+ " TEXT NOT NULL," +
            ORDER_TERMS_CONDITIONS+ " TEXT NOT NULL," +
            ORDER_MEMO+ " TEXT NOT NULL," +
            ORDER_CANCELLATION_REASON+ " TEXT NOT NULL," +
            ORDER_COMMENTS+ " TEXT NOT NULL," +
            ORDER_CANCELLATION_DATE+ " TEXT NOT NULL," +
            ORDER_PO_REFERENCE_NUM+ " TEXT NOT NULL," +
            ORDER_CONFIRMED_BY+ " TEXT NOT NULL," +
            ORDER_CONFIRMED_DATE+ " TEXT NOT NULL," +
            ORDER_NOTES+ " TEXT NOT NULL," +
            ORDER_WAREHOUSE_ID+ " TEXT NOT NULL," +
            ORDER_CUSTOMERTAXSTATUS+ " TEXT NOT NULL," +
            ORDER_NETSTATUS+ " TEXT NOT NULL," +
            ORDER_SYNCSTATUS+ " TEXT NOT NULL," +
            ORDER_LASTSYNCON+ " TEXT NOT NULL," +
            ORDER_DRAFTNUM+ " TEXT NOT NULL," +
            ORDER_SHIPMENTTYPE+ " TEXT NOT NULL," +
            ORDER_TAXID+ " TEXT NOT NULL," +
            ORDER_TERMSID+ " TEXT NOT NULL," +
            ORDER_DUEDATE+ " TEXT NOT NULL);";


    public static String CREATE_TABLE_SHIPMENT = "create table " +TABLE_SHIPMENT+ "(" +SHIP_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            SHIP_ID+ " TEXT NOT NULL," +
            SHIP_ORDER_ID+ " TEXT NOT NULL," +
            SHIP_TYPE_ID+ " TEXT NOT NULL," +
            SHIP_WAREHOUSE_ID+ " TEXT NOT NULL," +
            SHIP_ROUTE_NUM+ " TEXT NOT NULL," +
            SHIP_ROUTE_DATE+ " TEXT NOT NULL," +
            SHIP_TRUCK + " TEXT NOT NULL," +
            SHIP_DRIVER_ID + " TEXT NOT NULL," +
            SHIP_CARRIER_ID + " TEXT NOT NULL," +
            SHIPPED_DATE + " TEXT NOT NULL," +
            SHIP_TRACK_NUM + " TEXT NOT NULL," +
            SHIP_DELIVERY_DATE + " TEXT NOT NULL," +
            SHIP_DELIVERY_TIME+ " TEXT NOT NULL," +
            SHIP_IS_ACTIVE + " TEXT NOT NULL," +
            SHIP_UUID + " TEXT NOT NULL," +
            SHIP_IS_DISABLED + " TEXT NOT NULL," +
            SHIP_IS_DISABLED_BY + " TEXT NOT NULL," +
            SHIP_DISBLED_BEHALFOF + " TEXT NOT NULL," +
            SHIP_IS_DELETED + " TEXT NOT NULL," +
            SHIP_DELTED_BY + " TEXT NOT NULL," +
            SHIP_DELETED_BEHALFOF + " TEXT NOT NULL," +
            SHIP_DELETE_DON+ " TEXT NOT NULL," +
            SHIP_CREATE_BY + " TEXT NOT NULL," +
            SHIP_CREATED_BEHALFOF + " TEXT NOT NULL," +
            SHIP_CREATION_DON+ " TEXT NOT NULL," +
            SHIP_MODIFY_BY + " TEXT NOT NULL," +
            SHIP_MODIFIED_BEHALF + " TEXT NOT NULL," +
            SHIP_MODIFIED_DON+ " TEXT NOT NULL," +
            SHIP_DATA_TYPE_ID + " TEXT NOT NULL," +
            SHIP_DATAFROM_ID+ " TEXT NOT NULL," +
            SHIP_TENAN_ID + " TEXT NOT NULL," +
            SHIP_FULFILLMENTS_STATUS+ " TEXT NOT NULL," +
            SHIP_PICKER_ID + " TEXT NOT NULL," +
            SHIP_PACKER_ID+ " TEXT NOT NULL," +
            SHIPPER_ID+ " TEXT NOT NULL," +
            SHIP_INVOICER_ID + " TEXT NOT NULL," +
            SHIP_PAYMENT_ID + " TEXT NOT NULL," +
            SHIP_DELIVERY_AGENT_ID + " TEXT NOT NULL," +
            SHIP_DELIVER_TYPE_ID+ " TEXT NOT NULL," +
            SHIP_ROUTE_DAY_ID + " TEXT NOT NULL," +
            SHIP_ROUTE_ID+ " TEXT NOT NULL," +
            SHIP_TRUCK_ID + " TEXT NOT NULL," +
            SHIP_INVOICE_ID + " TEXT NOT NULL," +
            SHIP_WORKORDER_ID + " TEXT NOT NULL," +
            SHIP_PURCHASE_ORDER_ID+ " TEXT NOT NULL," +
            SHIP_UNDELIVER_REASON_ID + " TEXT NOT NULL," +
            SHIP_UNDELIVER_REASON+ " TEXT NOT NULL);";


    public static String CREATE_TABLE_INVOICES = "create table " +TABLE_INVOICE+ "(" +INVOICES_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
    INVOICES_DEFAULT_ID+ " TEXT NOT NULL," +
    INVOICES_ORDER_ID+ " TEXT NOT NULL," +
    INVOICES_PAYMENTTYPE_ID+ " TEXT NOT NULL," +
    INVOICES_PAYMENT_ID+ " TEXT NOT NULL," +
    INVOICES_PAYMENT_METHOD+ " TEXT NOT NULL," +
    INVOICES_NAME+ " TEXT NOT NULL," +
    INVOICES_BILLTOADDRESS_ID+ " TEXT NOT NULL," +
    INVOICES_CUSTOMER_ID+ " TEXT NOT NULL," +
    INVOICES_CUSTOMERID_TYPE + " TEXT NOT NULL," +
    INVOICES_DATEFULFILLED+ " TEXT NOT NULL," +
    INVOICES_DESCRIPTION+ " TEXT NOT NULL," +
    INVOICES_DISCOUNTAMT+ " TEXT NOT NULL," +
    INVOICES_TIMESTAMP+ " TEXT NOT NULL," +
    INVOICES_TRANSACTION_CURRENCY+ " TEXT NOT NULL," +
    INVOICES_EXCHANGE_RATE+ " TEXT NOT NULL," +
    INVOICES_DISCOUNTAMT_BASE+ " TEXT NOT NULL," +
    INVOICES_DISCOUNT_PERCENTAGE+ " TEXT NOT NULL," +
    INVOICES_FREIGHTAMOUNT+ " TEXT NOT NULL," +
    INVOICES_FRIGHTAMT_BASE+ " TEXT NOT NULL," +
    INVOICES_FRIGHTTERMS_CODE+ " TEXT NOT NULL," +
    INVOICES_ISPRICE_LOCKED + " TEXT NOT NULL," +
    INVOICES_LASTBACKOFFICE + " TEXT NOT NULL," +
    INVOICES_OPPORTUNITY_ID+ " TEXT NOT NULL," +
    INVOICES_ORDERNUMBER+ " TEXT NOT NULL," +
    INVOICES_PAYMENTTERMS_CODE+ " TEXT NOT NULL," +
    INVOICES_PAYMENTTERMS_DISPLAY+ " TEXT NOT NULL," +
    INVOICES_PRICELEVELID+ " TEXT NOT NULL," +
    INVOICES_PRICEERROR_CODE+ " TEXT NOT NULL," +
    INVOICES_PRIORITY_CODE+ " TEXT NOT NULL," +
    INVOICES_QUOTE_ID+ " TEXT NOT NULL," +
    INVOICES_REQUEST_DELIVERY+ " TEXT NOT NULL," +
    INVOICES_SHIPPING_CODE + " TEXT NOT NULL," +
    INVOICES_SHIPPING_DISPLAY+ " TEXT NOT NULL," +
    INVOICES_SHIPTOADDRESS_ID+ " TEXT NOT NULL," +
    INVOICES_STATECODE+ " TEXT NOT NULL," +
    INVOICES_STATUS_CODE+ " TEXT NOT NULL," +
    INVOICES_DATE+ " TEXT NOT NULL," +
    INVOICES_SUBMIT_STATUS+ " TEXT NOT NULL," +
    INVOICES_SUBMIT_DESC+ " TEXT NOT NULL," +
    INVOICES_TOTAL_AMT+ " TEXT NOT NULL," +
    INVOICES_TOTALAMT_BASE + " TEXT NOT NULL," +
    INVOICES_TOTALAMT_FREIGHT+ " TEXT NOT NULL," +
    INVOICES_TOTALAMT_FRIGHTBASE+ " TEXT NOT NULL," +
    INVOICES_TOTALDISCOUNT_AMT + " TEXT NOT NULL," +
    INVOICES_TOTALDISCOUNT_AMTBASE + " TEXT NOT NULL," +
    INVOICES_TOTALLINE_ITEMAMT + " TEXT NOT NULL," +
    INVOICES_TOTALLINEITEM_AMTBASE + " TEXT NOT NULL," +
    INVOICES_TOTALLINEITEM_DISCOUNTBASE + " TEXT NOT NULL," +
    INVOICES_TOTALLINEITEM_DISCOUNT_AMTBASE+ " TEXT NOT NULL," +
    INVOICES_TOTALTAX + " TEXT NOT NULL," +
    INVOICES_TOTALTAX_BASE + " TEXT NOT NULL," +
    INVOICES_WILLCALL + " TEXT NOT NULL," +
    INVOICES_ISDISABLE + " TEXT NOT NULL," +
    INVOICES_DISABLEBY + " TEXT NOT NULL," +
    INVOICES_DISABLE_BEHALF + " TEXT NOT NULL," +
    INVOICES_ISDELETED + " TEXT NOT NULL," +
    INVOICES_DELETEBY + " TEXT NOT NULL," +
    INVOICES_DELETE_BEHALF + " TEXT NOT NULL," +
    INVOICES_DELETEDON + " TEXT NOT NULL," +
    INVOICES_CREATEBY + " TEXT NOT NULL," +
    INVOICES_CREATEDBEHALF+ " TEXT NOT NULL," +
    INVOICES_CREATEDON+ " TEXT NOT NULL," +
    INVOICES_MODIFIEDBY + " TEXT NOT NULL," +
    INVOICES_MODIFIED_BEHALF + " TEXT NOT NULL," +
    INVOICES_MODIFIDON + " TEXT NOT NULL," +
    INVOICES_AGENT + " TEXT NOT NULL," +
    INVOICES_SALESREPID + " TEXT NOT NULL," +
    INVOICES_PRICINGDATE + " TEXT NOT NULL," +
    INVOICES_DATAFROMTYPEID + " TEXT NOT NULL," +
    INVOICES_DATAFROM_ID + " TEXT NOT NULL," +
    INVOICES_TENANID + " TEXT NOT NULL," +
    INVOICES_ISACTIVE + " TEXT NOT NULL," +
    INVOICES_DELIVERYNOTE + " TEXT NOT NULL," +
    INVOICES_SHIP + " TEXT NOT NULL," +
    INVOICES_TERMS + " TEXT NOT NULL," +
    INVOICES_MEMO + " TEXT NOT NULL," +
    INVOICES_CANCELREASON + " TEXT NOT NULL," +
    INVOICES_COMMENTS + " TEXT NOT NULL," +
    INVOICES_CANCEL_DATE + " TEXT NOT NULL," +
    INVOICES_REFRENCE_ORDER + " TEXT NOT NULL," +
    INVOICES_VENDORID + " TEXT NOT NULL," +
    INVOICES_EXTERNALID + " TEXT NOT NULL," +
    INVOICES_EXTERNAL_STATUS + " TEXT NOT NULL," +
    INVOICES_WAREHOUSE_ID + " TEXT NOT NULL," +
    INVOICES_CUSTOMER_ISTAXABLE + " TEXT NOT NULL," +
    INVOICES_DRAFT_NUMBER + " TEXT NOT NULL," +
    INVOICES_LASTSYNCON + " TEXT NOT NULL," +
    INVOICES_NETSTATUS+ " TEXT NOT NULL," +
    INVOICES_SYNCSTATUS+ " TEXT NOT NULL," +
    INVOICES_TAXID+ " TEXT NOT NULL," +
    INVOICES_TERMSID+ " TEXT NOT NULL," +
    INVOICES_DUEDATE+ " TEXT NOT NULL);";


    public static String CREATE_INVOICE_PRODUCTS = "create table " +TABLE_INVOICE_PRODUCTS+ "(" +IN_PRODUCTS_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
    IN_PRODUCTS_ID+ " TEXT NOT NULL," +
    IN_PRODUCTS_INVOICE_ID+ " TEXT NOT NULL," +
    IN_PRODUCTS_TXNCURRENCY+ " TEXT NOT NULL," +
    IN_PRODUCTS_UOMID+ " TEXT NOT NULL," +
    IN_PRODUCTS_BASEAMT+ " TEXT NOT NULL," +
    IN_PRODUCTS_EXCHANGERATE+ " TEXT NOT NULL," +
    IN_PRODUCTS_BASEAMT_BASE + " TEXT NOT NULL," +
    IN_PRODUCTS_DEC+ " TEXT NOT NULL," +
    IN_PRODUCTS_EXTENDAMT+ " TEXT NOT NULL," +
    IN_PRODUCTS_EXTENDAMT_BASE+ " TEXT NOT NULL," +
    IN_PRODUCTS_ISCOPIED + " TEXT NOT NULL," +
    IN_PRODUCTS_ISPRICEOVER + " TEXT NOT NULL," +
    IN_PRODUCTS_ISPRODUCT_OVER+ " TEXT NOT NULL," +
    IN_PRODUCTS_LINEITEM_NUM + " TEXT NOT NULL," +
    IN_PRODUCTS_MANUAL_DISCOUNT + " TEXT NOT NULL," +
    IN_PRODUCTS_MANUAL_DISAMT_BASE + " TEXT NOT NULL," +
    IN_PRODUCTS_PARENT_BUNDLEID+ " TEXT NOT NULL," +
    IN_PRODUCTS_PRODUCT_ASSOCIATION + " TEXT NOT NULL," +
    IN_PRODUCTS_PRODUCTTYPECODE + " TEXT NOT NULL," +
    IN_PRODUCTS_PRICEPERUNIT + " TEXT NOT NULL," +
    IN_PRODUCTS_PRICEPER_UNITBASE + " TEXT NOT NULL," +
    IN_PRODUCTS_PRICING_ERRORCODE + " TEXT NOT NULL," +
    IN_PRODUCTS_PRODUCT_DEC+ " TEXT NOT NULL," +
    IN_PRODUCTS_PRODUCT_NAME + " TEXT NOT NULL," +
    IN_PRODUCTS_PRODUCTID + " TEXT NOT NULL," +
    IN_PRODUCTS_QTY + " TEXT NOT NULL," +
    IN_PRODUCTS_QTY_BACK + " TEXT NOT NULL," +
    IN_PRODUCTS_QTY_CANCELLED+ " TEXT NOT NULL," +
    IN_PRODUCTS_QTY_SHIPPED + " TEXT NOT NULL," +
    IN_PRODUCTS_REQUEST_DELIVERY + " TEXT NOT NULL," +
    IN_PRODUCTS_SALESORDER_LOCK + " TEXT NOT NULL," +
    IN_PRODUCTS_SALES_STATECODE + " TEXT NOT NULL," +
    IN_PRODUCTS_SALES_REPID + " TEXT NOT NULL," +
    IN_PRODUCTS_SHIPTOADDRESSID + " TEXT NOT NULL," +
    IN_PRODUCTS_TAX + " TEXT NOT NULL," +
    IN_PRODUCTS_TAX_BASE + " TEXT NOT NULL," +
    IN_PRODUCTS_VOLUME_DISAMT + " TEXT NOT NULL," +
    IN_PRODUCTS_VOLUME_DIS_AMTBASE + " TEXT NOT NULL," +
    IN_PRODUCTS_WILLCALL + " TEXT NOT NULL," +
    IN_PRODUCTS_SEQUENCENUM+ " TEXT NOT NULL," +
    IN_PRODUCTS_QUOTE_DETAIL + " TEXT NOT NULL," +
    IN_PRODUCTS_SALES_ORDER + " TEXT NOT NULL," +
    IN_PRODUCTS_UUID + " TEXT NOT NULL," +
    IN_PRODUCTS_ISDISABLED+ " TEXT NOT NULL," +
    IN_PRODUCTS_DISABLEBY + " TEXT NOT NULL," +
    IN_PRODUCTS_DISABLEBEHALF + " TEXT NOT NULL," +
    IN_PRODUCTS_ISDELETED + " TEXT NOT NULL," +
    IN_PRODUCTS_DELETEBY + " TEXT NOT NULL," +
    IN_PRODUCTS_DELETE_BEHALF + " TEXT NOT NULL," +
    IN_PRODUCTS_DELETEDON + " TEXT NOT NULL," +
    IN_PRODUCTS_CREATEDBY + " TEXT NOT NULL," +
    IN_PRODUCTS_CREATEDBEHALF + " TEXT NOT NULL," +
    IN_PRODUCTS_CREATEDON + " TEXT NOT NULL," +
    IN_PRODUCTS_MODIFIYBY + " TEXT NOT NULL," +
    IN_PRODUCTS_MODIFIEDBEHALF + " TEXT NOT NULL," +
    IN_PRODUCTS_MODIFIYDON + " TEXT NOT NULL," +
    IN_PRODUCTS_AGENT + " TEXT NOT NULL," +
    IN_PRODUCTS_DATAFROM_TYPEID + " TEXT NOT NULL," +
    IN_PRODUCTS_DATAFROM_ID+ " TEXT NOT NULL," +
    IN_PRODUCTS_TENANTID + " TEXT NOT NULL," +
    IN_PRODUCTS_ISACTIVE + " TEXT NOT NULL," +
    IN_PRODUCTS_QTY_PICKED + " TEXT NOT NULL," +
    IN_PRODUCTS_QTY_PACKED + " TEXT NOT NULL," +
    IN_PRODUCTS_ORDERPID + " TEXT NOT NULL," +
    IN_PRODUCTS_EXTERNAL_ID + " TEXT NOT NULL," +
    IN_PRODUCTS_EXTERNAL_STATUS + " TEXT NOT NULL," +
    IN_PRODUCTS_ITEMS_TAX + " TEXT NOT NULL," +
    IN_PRODUCTS_ORDERID + " TEXT NOT NULL," +
    IN_PRODUCTS_WAREHOUSE+ " TEXT NOT NULL);";


    public static String CREATE_TABLE_MASTER_SYNC = "create table " +TABLE_MASTER_SYNC+ "(" +MASTER_SYNC_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            MASTER_SYNC_ID+ " TEXT NOT NULL," +MASTER_SYNC_NAME+ " TEXT NOT NULL," +MASTER_SYNC_TIME+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_USER = "create table " +TABLE_USER+ "(" +USER_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            USER_ID+ " TEXT NOT NULL," +USER_NAME+ " TEXT NOT NULL," +USER_TOKEN+ " TEXT NOT NULL," +USER_EMAIL+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_CANCELREASON = "create table " +TABLE_CANCELREASON+ "(" +CANCELREASON_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CANCELREASON_ID+ " TEXT NOT NULL," +CANCELREASON_NAME+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_PRODUCT_ASSERT = "create table " +TABLE_PRODUCT_ASSERT+ "(" +PRODUCT_ASSERT_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            PRODUCT_ASSERT_ID+ " TEXT NOT NULL," +PRODUCT_ASSERT_NAME+ " TEXT NOT NULL," +PRODUCT_ASSERT_PRODUCTSID+ " TEXT NOT NULL," +PRODUCT_ASSERT_IMAGE+ " TEXT NOT NULL," +PRODUCT_ASSERT_FILE+ " TEXT NOT NULL," +PRODUCT_ASSERT_TYPE+ " TEXT NOT NULL," +PRODUCT_ASSERT_DEFAULT+ " TEXT NOT NULL," +PRODUCT_ASSERT_ACTIVE+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_ORDERSTATUS = "create table " +TABLE_ORDERSTATUS+ "(" +ORDERSTATUS_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ORDERSTATUS_ID+ " TEXT NOT NULL," +ORDERSTATUS_NAME+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_INVOIVEFORMAT = "create table " +TABLE_INVOICE_FORMAT+ "(" +INVOICE_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            INVOICE_ID+ " TEXT NOT NULL," +INVOICE_NAME+ " TEXT NOT NULL," +INVOICE_VALUE+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_CURRENCY = "create table " +TABLE_CURRENCY+ "(" +CURRENCY_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CURRENCY_ID+ " TEXT NOT NULL," +CURRENCY_NAME+ " TEXT NOT NULL," +CURRENCY_SYMBOL+ " TEXT NOT NULL," +CURRENCY_Code+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_CS_TAX = "create table " +TABLE_CS_TAX+ "(" +CS_TAX_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CS_TAX_ID+ " TEXT NOT NULL," +CS_TAX_PERCENT+ " TEXT NOT NULL," +CS_TAX_STATEID+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_TERMS = "create table " +TABLE_TERMS+ "(" +TERMS_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TERMS_ID+ " TEXT NOT NULL," +TERMS_NAME+ " TEXT NOT NULL," +TERMS_DAYS+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_CS_TABLE= "create table " +TABLE_CS_STATE+ "(" +CS_STATE_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CS_ID+ " TEXT NOT NULL," +CS_STATE_ID+ " TEXT NOT NULL," +CS_TAXBLE+ " TEXT NOT NULL," +CS_TAXID+ " TEXT NOT NULL," +CS_TERMSID+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_SALESREP = "create table " +TABLE_SALESREP+ "(" +SALESREP_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            SALESREP_ID+ " TEXT NOT NULL," +SALESREP_NAME+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_DELIVERYREP = "create table " +TABLE_DELIVERYREP+ "(" +DELIVERY_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            DELIVERY_ID+ " TEXT NOT NULL," +DELIVERY_NAME+ " TEXT NOT NULL," +DELIVERY_EMAIL+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_FULFILLMENT = "create table " +TABLE_FULFILLMENT+ "(" +FULFILLMENT_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            FULFILLMENT_ID+ " TEXT NOT NULL," +FULFILLMENT_NAME+ " TEXT NOT NULL," +FULFILLMENT_DEFAULT+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_WAREHOUSE = "create table " +TABLE_WAREHOUSE+ "(" +WAREHOUSE_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            WAREHOUSE_ID+ " TEXT NOT NULL," +WAREHOUSE_NAME+ " TEXT NOT NULL);";

    public static String CREATE_All_TABLE_PRODUCT = "create table " +TABLE_All_PRODUCT+ "(" +All_PRODUCT_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            All_PRODUCT_ID+ " TEXT NOT NULL," +All_PRODUCT_NUMBER+ " TEXT NOT NULL," +All_PRODUCT_NAME+ " TEXT NOT NULL," +All_PRODUCT_PRICE+ " TEXT NOT NULL," +All_PRODUCT_STATUS+ " TEXT NOT NULL," +All_PRODUCT_IMG+ " TEXT NOT NULL," +All_PRODUCT_UPSC+ " TEXT NOT NULL," +All_PRODUCT_ISTAX+ " TEXT NOT NULL," +All_PRODUCT_DES+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_ADDRESS = "create table " +TABLE_ADDRESS+ "(" +ADDRESS_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ADDRESS_ID+ " TEXT NOT NULL," +ADDRESS_LINE1+ " TEXT NOT NULL," +ADDRESS_LINE2+ " TEXT NOT NULL," +ADDRESS_LINE3+ " TEXT NOT NULL," +ADDRESS_SATE_PROVINCE+ " TEXT NOT NULL," +ADDRESS_TYPECODE+ " TEXT NOT NULL," +ADDRESS_COUNTRY+ " TEXT NOT NULL," +ADDRESS_CITY+ " TEXT NOT NULL," +ADDRESS_POSTAL+ " TEXT NOT NULL," +ADDRESS_POSTOFFICE+ " TEXT NOT NULL," +ADDRESS_NAME+ " TEXT NOT NULL," +ADDRESS_PRIMARY_ADD+ " TEXT NOT NULL," +ADDRESS_ISACTIVE+ " TEXT NOT NULL," +ADDRESS_DOMAINEVENTS+ " TEXT NOT NULL);";

    public static String CREATE_TABLE_ORDER_PRODUCT = "create table " +TABLE_ORDER_PRODUCT+ "(" +ORDER_PRODUCT_PRIMARY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ORDER_PRO_ID+ " TEXT NOT NULL," +ORDER_PRODUCT_ORDERID+ " TEXT NOT NULL," +ORDER_PRODUCT_ID+ " TEXT NOT NULL," +ORDER_PRODUCT_PRICEPER_UNIT+ " TEXT NOT NULL," +ORDER_PRODUCT_BASEAMT+ " TEXT NOT NULL," +ORDER_PRODUCT_QTY+ " TEXT NOT NULL," +ORDER_PRODUCT_NAME+ " TEXT NOT NULL," +ORDER_PRODUCT_TAX+ " TEXT NOT NULL," +ORDER_PRODUCT_WAREHOUSE+ " TEXT NOT NULL);";

    public DbHandler(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_SHIPMENT);
        db.execSQL(CREATE_TABLE_ORDERSTATUS);
        db.execSQL(CREATE_TABLE_CANCELREASON);
        db.execSQL(CREATE_TABLE_PRODUCT_ASSERT);
        db.execSQL(CREATE_TABLE_SALESREP);
        db.execSQL(CREATE_TABLE_DELIVERYREP);
        db.execSQL(CREATE_TABLE_FULFILLMENT);
        db.execSQL(CREATE_TABLE_WAREHOUSE);
        db.execSQL(CREATE_All_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_ADDRESS);
        db.execSQL(CREATE_TABLE_ORDER_PRODUCT);
        db.execSQL(CREATE_TABLE_INVOIVEFORMAT);
        db.execSQL(CREATE_TABLE_CURRENCY);
        db.execSQL(CREATE_TABLE_CS_TAX);
        db.execSQL(CREATE_TABLE_TERMS);
        db.execSQL(CREATE_TABLE_CS_TABLE);
        db.execSQL(CREATE_TABLE_INVOICES);
        db.execSQL(CREATE_INVOICE_PRODUCTS);
        db.execSQL(CREATE_TABLE_MASTER_SYNC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANCELREASON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_ASSERT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERSTATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALESREP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERYREP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FULFILLMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAREHOUSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_All_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_FORMAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CS_STATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CS_TAX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_SYNC);
        onCreate(db);
    }
}