package com.example.arcomdriver.common;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Dec 2022*/

public class Const {

      //TODO CrispFresh
      //public static final String AppServer ="crispnfresh";
      //public static final String hostName =  "https://crispnfresh.archarina.com/arcordermanagement/API/api/v1/";
      //public static final String ImageProducts =  "https://crispnfresh.archarina.com/arcordermanagement/API/";
      //public static final String ImagePayments =  "https://crispnfresh.archarina.com/arcordermanagement/API/files/Payments/";
      //public static final String ImagePaymentsSignature =  "https://crispnfresh.archarina.com/arcordermanagement/API/files/Payments/Signature/";
      //public static final String ImageDeliverySignature = "https://crispnfresh.archarina.com/arcordermanagement/API/";

      //TODO QA
      public static final String AppServer ="qa";
      public static final String hostName =  "https://arcproducts-qa.archarina.com/ArcOrderManagement/API/api/v1/";

      public static final String ImageProducts =  "https://arcproducts-qa.archarina.com/ArcOrderManagement/API/";
      public static final String ImagePayments =  "https://arcproducts-qa.archarina.com/ArcOrderManagement/API/files/Payments/";
      public static final String ImagePaymentsSignature =  "https://arcproducts-qa.archarina.com/ArcOrderManagement/API/files/Payments/Signature/";
      public static final String ImageDeliverySignature = "https://arcproducts-qa.archarina.com/ArcOrderManagement/API/";


      //TODO UAT
      //public static final String AppServer ="uat";
      //public static final String hostName =  "https://arcproducts-uat.archarina.com/ArcOrderManagement/API/api/v1/";
      //public static final String ImageProducts =  "https://arcproducts-uat.archarina.com/ArcOrderManagement/API/";
      //public static final String ImagePayments =  "https://arcproducts-uat.archarina.com/ArcOrderManagement/API/files/Payments/";
      //public static final String ImagePaymentsSignature =  "https://arcproducts-uat.archarina.com/ArcOrderManagement/API/files/Payments/Signature/";

      //TODO DEV-QA
      //public static final String AppServer ="dev-qa";
      //public static final String hostName = "https://arcproducts-dev.archarina.com/ArcOrderManagement/API/api/v1/";

      //public static final String ImageProducts = "https://arcproducts-dev.archarina.com/ArcOrderManagement/API/";
      //public static final String ImagePayments = "https://arcproducts-dev.archarina.com/ArcOrderManagement/API/files/Payments/";
      //public static final String ImagePaymentsSignature = "https://arcproducts-dev.archarina.com/ArcOrderManagement/API/files/Payments/Signature/";
      //public static final String ImageDeliverySignature = "https://arcproducts-dev.archarina.com/ArcOrderManagement/API/";

      //TODO Local IP
      //public static final String AppServer ="";
      //public static final String hostName =  "http://192.168.29.178/ArcorderAPI/api/v1/";
      //public static final String ImageProducts =  "http://192.168.29.178/ArcorderAPI/files/products/";
      //public static final String ImagePayments =  "http://192.168.29.178/ArcorderAPI/files/Payments/";
      //public static final String ImagePaymentsSignature =  "http://192.168.29.178/ArcorderAPI/files/Payments/Signature/";

      static final String PostLogin = hostName+"identity/tokens";

      public static final String GetOrder = hostName+"order/mobile";

      //login order
      public static final String GetOrderLogin = hostName+"order/mobile/lastsynceddata?orderlastsyncon=1990-01-01T00%3A00%3A00.000Z&orderproductlastsyncon=1990-01-01T00%3A00%3A00.000Z&shipmentlastsyncon=1990-01-01T00%3A00%3A00.000Z&addresslastsyncon=1990-01-01T00%3A00%3A00.000Z&invoicelastsyncon=1990-01-01T00%3A00%3A00.000Z&invoiceproductlastsyncon=1990-01-01T00%3A00%3A00.000Z&customerlastsyncon=1990-01-01T00%3A00%3A00.000Z&productlastsyncon=1990-01-01T00%3A00%3A00.000Z&productassetlastsyncon=1990-01-01T00%3A00%3A00.000Z";

      //create presale order
      public static final String GetCreateOrderLogin = hostName+"order/mobile/lastsynceddata?orderlastsyncon=";

      public static final String GetOrderByID = hostName+"order/mobile/getbyid?entitytype=";
      static final String GetCurrency = hostName+"order/common/transactioncurrency";
      static final String GetCancelReason = hostName+"order/common/optionset?id=";
      static final String GetUserDetails = hostName+"order/common/userdetails?id=";
      static final String GetSalesRep = hostName+"order/common/salesrep";
      static final String GetDeliveryRep = hostName+"order/common/tierbaseduser?tierorder=0";
      static final String GetFulfilment = hostName+"order/common/optionset?logicalname=shipmenttype";
      static final String GetWarehouse = hostName+"order/warehouse";
      static final String GetCustomerState = hostName+"order/orders/customer";
      static final String GetCustomerTaxall = hostName+"order/common/taxall";
      static final String GetTermsDetails = hostName+"order/terms/termsdetail";
      static final String GetCompanylogo = hostName+"order/common/alluploadedfilesdata?foldername=Company&aggregateid=e3d9a0f6-d5af-4654-803c-b163bfdf28ad";
      static final String GetTaxCode = hostName+"order/tax?PageSize=500";
      static final String PostSavePresale = hostName+"order/orders/savepresale";
      static final String PostUpdatePresale = hostName+"order/orders/updatepresale";
      static final String PostCancelPresale = hostName+"order/presale/cancelsave";

      static final String PostSalesReason = hostName+"order/salesreturn/status";
      static final String filterkpi = hostName+"order/presale/filterkpi";

      static final String InvoiceFilterkpi = hostName+"order/invoice/filterkpi";
      static final String SalesREturnFilterkpi = hostName+"order/salesreturn/filterkpi";
      static final String PostNotes = hostName+"identity/eventlogs";
      static final String PostPresaleOrder = hostName+"order/presale/";
      static final String PostInvoiceCreate = hostName+"order/invoice";
      static final String PostProduct = hostName+"order/product";
      static final String PostEmail = hostName+"order/email";
      static final String PostProductUpdate = hostName+"order/product/update";
      static final String PostCustomerCreate = hostName+"order/customer";
      static final String PostCustomerUpdate = hostName+"order/customer/update";
      static final String PostPayment = hostName+"order/payment";
      static final String PostInvoiceSummaryFul = hostName+"order/presale/summaryfulfillment";
      static final String PostStatusUpdate = hostName+"order/common/presalestatusupdate";
      static final String PostConfirmStatusUpdate = hostName+"order/common/presalestatusupdate";
      static final String PostWarehouseSummaryFul = hostName+"order/warehouse/fulfillmentupdate";
      static final String GetNotesList = hostName+"identity/eventlogs?UserId=";
      static final String GetInvoiceList = hostName+"order/invoice?orderId=";
      public static final String GetProductList = hostName+"order/product?PageSize=500";

      public static final String GetDeliverOrders = hostName+"order/route/getliveroutecustomerordersall?customerid=";
      static final String GetInvoiceSummary = hostName+"order/invoice?invoiceid=";
      static final String GetPaymentsView = hostName+"order/payment?id=";

      static final String GetDeliveryView = hostName+"order/common/alluploadedfilesdata?foldername=Delivery&aggregateid=";
      static final String GetCustomer = hostName+"order/customer?PageSize=500";
      static final String GetCustomerSummary = hostName+"order/customer/";
      static final String GetProductsSummary = hostName+"order/product/";
      static final String GetIndustry = hostName+"order/common/optionset?id=FB354A80-8257-4502-9138-6775975F0C12";
      static final String GetCountry = hostName+"order/common/optionset?logicalname=Country";
      static final String GetState = hostName+"order/common/optionset?logicalname=State&parentoptionid=";
      static final String GetCity = hostName+"order/common/optionset?logicalname=City&parentoptionid=";
      static final String GetCustomerStatus = hostName+"order/common/optionset?logicalname=status";

      static final String GetPriceMethod = hostName+"order/common/optionset?logicalname=pricingmethod";
      static final String GetPDFFormat = hostName+"order/common/systemsettingsall";
      static final String GetProductMaster = hostName+"order/product/masters";
      static final String GetProductGroup = hostName+"order/common/optionset?logicalname=productgroup";
      static final String GetWareHouseType = hostName+"order/common/optionset?logicalname=warehousetype";
      static final String GetReason = hostName+"order/CancellationReason/getreasons/?optionsetid=ccf4807d-a92a-4652-8297-bfb1ae79feb8&pagenumber=1&pagesize=1000";
      static final String GetWareHouseLocation = hostName+"order/warehouse?warehousetype=";
      static final String GetKpi = hostName+"order/route/getroutekpi?RouteOn=";
      static final String GetRouteList = hostName+"order/optimizeroute/allrecordsroutedashboard?startdate=";
      static final String GetPlannedOrder = hostName+"order/route/getdraftedrouteallorderssod?routedayid=";
      static final String GetPlannedStops = hostName+"order/route/getdraftedroutesodallstops?routedayid=";
      static final String GetInspectionDetails = hostName+"order/route/getinspectiondetails";
      static final String PostInspection = hostName+"order/route/saveinspectiondetails";
      static final String PostPaymentRoute = hostName+"order/route/initiateDraftRouteDetailsSOD";
      static final String PostAddStops = hostName+"order/route/savenewstop";
      static final String PostAddStops2 = hostName+"order/route/savestopsequence";
      static final String PostAddressMap = "https://addressvalidation.googleapis.com/v1:validateAddress?key=AIzaSyAxscm3U3mRV8xNuujweHqFtl_3joJDkW0";
      static final String GetRouteDetails = hostName+"order/route/getallplannedroute?Isliveroute=true";

      static final String GetCustomerPricing = hostName+"order/customerpricing?PageSize=500&PageNumber=1";
      static final String GetSalesReturns = hostName+"order/salesreturn?PageSize=500&PageNumber=1";

      static final String GetCustomerPricingInfo = hostName+"order/customerpricing/";
      static final String GetSalesInfo = hostName+"order/salesreturn/";
      static final String GetCustomerProducts = hostName+"order/customerpricing/getcpbyidasync?CId=";

      static final String PostCustomerRef = hostName+"order/salesreturn/getsalesreturnreference";
      static final String PostSales = hostName+"order/salesreturn";
      
}