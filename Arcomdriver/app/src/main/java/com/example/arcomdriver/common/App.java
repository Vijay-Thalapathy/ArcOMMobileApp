package com.example.arcomdriver.common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Dec 2022*/

public class App {
    private static App instance;

    public static App getInstance() {
        if (instance == null)
            instance = new App();
        return instance;
    }

  /*  public void GetAgentView(Callback b) throws IOException {
        getJWT(Const.GetPresaleView, b);
    }
*/
    public void GetOrderLogin(String jwt_token,Callback b) throws IOException {
        get(Const.GetOrderLogin, b,jwt_token);
    }

    public void GetCreateOrderLogin(String orderDate,String productsDate,String shipmentDate,String addressDate,String InvoiceDate,String InvoiceProductsDate,String CustomerDate,String ProductsDate,String ProductsAssertDate,String jwt_token,Callback b) throws IOException {
        get(Const.GetCreateOrderLogin+orderDate+"&orderproductlastsyncon="+productsDate+"&shipmentlastsyncon="+shipmentDate+"&addresslastsyncon="+addressDate+"&invoicelastsyncon="+InvoiceDate+"&invoiceproductlastsyncon="+InvoiceProductsDate+"&customerlastsyncon="+CustomerDate+"&productlastsyncon="+ProductsDate+"&productassetlastsyncon="+ProductsAssertDate, b,jwt_token);
    }
    public void GetCurrency(String jwt_token,Callback b) throws IOException {
        get(Const.GetCurrency, b,jwt_token);
    }

    public void GetSalesRep(String jwt_token,Callback b) throws IOException {
        get(Const.GetSalesRep, b,jwt_token);
    }

    public void GetDeliveryRep(String jwt_token,Callback b) throws IOException {
        get(Const.GetDeliveryRep, b,jwt_token);
    }

    public void GetFulfilment(String jwt_token,Callback b) throws IOException {
        get(Const.GetFulfilment, b,jwt_token);
    }

    public void GetCustomerStatus(String jwt_token,Callback b) throws IOException {
        get(Const.GetCustomerStatus, b,jwt_token);
    }

    public void GetPriceMethod(String jwt_token,Callback b) throws IOException {
        get(Const.GetPriceMethod, b,jwt_token);
    }
    public void GetPDFFormat(String jwt_token,Callback b) throws IOException {
        get(Const.GetPDFFormat, b,jwt_token);
    }
    public void GetProductMaster(String jwt_token,Callback b) throws IOException {
        get(Const.GetProductMaster, b,jwt_token);
    }
    public void GetProductGroup(String jwt_token,Callback b) throws IOException {
        get(Const.GetProductGroup, b,jwt_token);
    }
    public void GetWareHouseType(String jwt_token,Callback b) throws IOException {
        get(Const.GetWareHouseType, b,jwt_token);
    }

    public void GetReason(String jwt_token,Callback b) throws IOException {
        get(Const.GetReason, b,jwt_token);
    }
    public void GetWarehouse(String jwt_token,Callback b) throws IOException {
        get(Const.GetWarehouse, b,jwt_token);
    }
    public void GetCustomerState(String jwt_token,Callback b) throws IOException {
        getNEw(Const.GetCustomerState, b,jwt_token);
    }
    public void GetCustomerTaxall(String jwt_token,Callback b) throws IOException {
        get(Const.GetCustomerTaxall, b,jwt_token);
    }
    public void GetTermsDetails(String jwt_token,Callback b) throws IOException {
        get(Const.GetTermsDetails, b,jwt_token);
    }

    public void GetCompanylogo(String jwt_token,Callback b) throws IOException {
        get(Const.GetCompanylogo, b,jwt_token);
    }
    public void GetTaxCode(String jwt_token,Callback b) throws IOException {
        get(Const.GetTaxCode, b,jwt_token);
    }

    public void GetCancelReason(String OrderID,String jwt_token,Callback b) throws IOException {
        get(Const.GetCancelReason+OrderID, b,jwt_token);
    }
    public void GetUserDetails(String OrderID,String jwt_token,Callback b) throws IOException {
        get(Const.GetUserDetails+OrderID, b,jwt_token);
    }

    public void GetNotesList(String OrderID,String jwt_token,Callback b) throws IOException {
        get(Const.GetNotesList+OrderID+"&MessageType=-1", b,jwt_token);
    }

    public void GetOrderByID(String entityType,String OrderID,String jwt_token,Callback b) throws IOException {
        get(Const.GetOrderByID+entityType+"&id="+OrderID, b,jwt_token);
    }

    public void GetInvoiceList(String OrderID,String jwt_token,Callback b) throws IOException {
        get(Const.GetInvoiceList+OrderID, b,jwt_token);
    }
    public void GetProductList(String jwt_token,Callback b) throws IOException {
        get(Const.GetProductList, b,jwt_token);
    }
    public void GetDeliverOrders(String RouteID,String CustID,String jwt_token,Callback b) throws IOException {
        get(Const.GetDeliverOrders+CustID+"&routeid="+RouteID, b,jwt_token);
    }
    public void GetCustomer(String jwt_token,Callback b) throws IOException {
        get(Const.GetCustomer, b,jwt_token);
    }
    public void GetState(String D_CountryId,String jwt_token,Callback b) throws IOException {
        get(Const.GetState+D_CountryId, b,jwt_token);
    }
    public void GetCountry(String jwt_token,Callback b) throws IOException {
        get(Const.GetCountry, b,jwt_token);
    }
    public void GetIndustry(String jwt_token,Callback b) throws IOException {
        get(Const.GetIndustry, b,jwt_token);
    }

    public void GetCity(String D_stateID,String jwt_token,Callback b) throws IOException {
        get(Const.GetCity+D_stateID, b,jwt_token);
    }
    public void GetWareHouseLocation(String D_stateID,String jwt_token,Callback b) throws IOException {
        get(Const.GetWareHouseLocation+D_stateID, b,jwt_token);
    }

    public void GetKpi(String Date_st,String jwt_token,Callback b) throws IOException {
        get(Const.GetKpi+Date_st,b,jwt_token);
    }

    public void GetRouteList(String SDate_st,String EDate_st,String jwt_token,Callback b) throws IOException {
        get(Const.GetRouteList+SDate_st+"&enddate="+EDate_st,b,jwt_token);
    }

    public void GetPlannedOrder(String RouteId,String jwt_token,Callback b) throws IOException {
        get(Const.GetPlannedOrder+RouteId,b,jwt_token);
    }
    public void GetPlannedStops(String RouteId,String jwt_token,Callback b) throws IOException {
        get(Const.GetPlannedStops+RouteId,b,jwt_token);
    }

    public void GetInspectionDetails(String jwt_token,Callback b) throws IOException {
        get(Const.GetInspectionDetails,b,jwt_token);
    }
    public void GetRouteDetails(String jwt_token,Callback b) throws IOException {
        get(Const.GetRouteDetails,b,jwt_token);
    }

    public void GetCustomerPricing(String jwt_token,Callback b) throws IOException {
        get(Const.GetCustomerPricing,b,jwt_token);
    }
    public void GetSalesReturns(String jwt_token,Callback b) throws IOException {
        get(Const.GetSalesReturns,b,jwt_token);
    }
    public void GetCustomerSummary(String id,String jwt_token,Callback b) throws IOException {
        get(Const.GetCustomerSummary+id, b,jwt_token);
    }

    public void GetCustomerPricingInfo(String id,String jwt_token,Callback b) throws IOException {
        get(Const.GetCustomerPricingInfo+id, b,jwt_token);
    }
    public void GetSalesInfo(String id,String jwt_token,Callback b) throws IOException {
        get(Const.GetSalesInfo+id, b,jwt_token);
    }

    public void GetCustomerProducts(String Id,String date,String postJson,String jwt_token, Callback b) throws IOException {
        postGet(Const.GetCustomerProducts+Id+"&currentdate="+date,b,postJson,jwt_token);
    }
    public void GetProductsSummary(String id,String jwt_token,Callback b) throws IOException {
        get(Const.GetProductsSummary+id, b,jwt_token);
    }
    public void GetInvoiceSummary(String invoidID,String jwt_token,Callback b) throws IOException {
        get(Const.GetInvoiceSummary+invoidID, b,jwt_token);
    }
    public void GetPaymentsView(String payID,String jwt_token,Callback b) throws IOException {
        get(Const.GetPaymentsView+payID, b,jwt_token);
    }

    public void GetDeliveryView(String payID,String jwt_token,Callback b) throws IOException {
        get(Const.GetDeliveryView+payID, b,jwt_token);
    }

    public void PostLogin(String json, Callback b) throws IOException {
        postLogin(Const.PostLogin,b,json);
    }
    public void PostCustomerCreate(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostCustomerCreate,b,json,jwt_token);
    }
    public void PostCustomerUpdate(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostCustomerUpdate,b,json,jwt_token);
    }

    public void PostPayment(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostPayment,b,json,jwt_token);
    }

    public void PostSavePresale(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostSavePresale,b,json,jwt_token);
    }

    public void PostUpdatePresale(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostUpdatePresale,b,json,jwt_token);
    }

    public void PostCancelPresale(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostCancelPresale,b,json,jwt_token);
    }

    public void PostSalesReason(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostSalesReason,b,json,jwt_token);
    }

    public void filterkpi(String json,String jwt_token, Callback b) throws IOException {
        post(Const.filterkpi,b,json,jwt_token);
    }

    public void InvoiceFilterkpi(String json,String jwt_token, Callback b) throws IOException {
        post(Const.InvoiceFilterkpi,b,json,jwt_token);
    }
    public void SalesREturnFilterkpi(String json,String jwt_token, Callback b) throws IOException {
        post(Const.SalesREturnFilterkpi,b,json,jwt_token);
    }
    public void PostNotes(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostNotes,b,json,jwt_token);
    }
    public void PostInvoiceCreate(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostInvoiceCreate,b,json,jwt_token);
    }

    public void PostProduct(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostProduct,b,json,jwt_token);
    }

    public void PostEmail(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostEmail,b,json,jwt_token);
    }

    public void PostInspection(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostInspection,b,json,jwt_token);
    }

    public void PostPaymentRoute(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostPaymentRoute,b,json,jwt_token);
    }

    public void PostAddStops(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostAddStops,b,json,jwt_token);
    }

    public void PostAddStops2(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostAddStops2,b,json,jwt_token);
    }
    public void PostCustomerRef(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostCustomerRef,b,json,jwt_token);
    }
    public void PostSales(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostSales,b,json,jwt_token);
    }
    public void PostAddressMap(String json,String jwt_token, Callback b) throws IOException {
        postMap(Const.PostAddressMap,b,json,jwt_token);
    }

    public void PostProductUpdate(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostProductUpdate,b,json,jwt_token);
    }

    public void PostInvoiceSummaryFul(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostInvoiceSummaryFul,b,json,jwt_token);
    }
    public void PostStatusUpdate(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostStatusUpdate,b,json,jwt_token);
    }
    public void PostConfirmStatusUpdate(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostConfirmStatusUpdate,b,json,jwt_token);
    }
    public void PostWarehouseSummaryFul(String json,String jwt_token, Callback b) throws IOException {
        post(Const.PostWarehouseSummaryFul,b,json,jwt_token);
    }
    public void PostPresaleOrder(String OrderId,String postJson,String jwt_token, Callback b) throws IOException {
        postGet(Const.PostPresaleOrder+OrderId,b,postJson,jwt_token);
    }

    private void post(String url, Callback b, String json, String token) throws IOException {
        System.out.println("URL---"+url);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization","Bearer "+token)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }
    private void get(String url, Callback b,String token) throws IOException {
        System.out.println("URL---"+url);
        System.out.println("token---"+token);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer "+token)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }
    private void getNEw(String url, Callback b,String token) throws IOException {
        System.out.println("URL---"+url);
        System.out.println("token---"+token);
        Request request = new Request.Builder()
                .url(url)
               // .addHeader("Authorization","Bearer "+token)
                .addHeader("tenantidentifier",Const.AppServer)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }

    private void delete(String url, Callback b,String token) throws IOException {
        System.out.println("URL---"+url);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization","Bearer "+token)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }

    // Only PostGet API
    private void postGet(String url, Callback b, String json, String token) throws IOException {
        System.out.println("PURL---"+url);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,"");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization","Bearer "+token)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }

    //Only Login
    private void postLogin(String url, Callback b, String json) throws IOException {
        System.out.println("URL---"+url);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("tenantidentifier",Const.AppServer)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }

    private void postMap(String url, Callback b, String json, String token) throws IOException {
        System.out.println("URL---"+url);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
              //  .addHeader("Authorization","Bearer "+token)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        client.newCall(request).enqueue(b);
    }

}