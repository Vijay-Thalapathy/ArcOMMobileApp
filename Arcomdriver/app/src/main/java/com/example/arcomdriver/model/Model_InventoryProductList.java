package com.example.arcomdriver.model;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/

public class Model_InventoryProductList {

    private String InProductName;
    private String InProductImage;
    private String InProductUnitMeasure;
    private String InProductPrice;
    private String InProductMargin;
    private String InProductProfit;
    private String InProductCusPrice;
    private String vendorcost;
    private String Smargin;
    private String Sprofit;


    public Model_InventoryProductList(String InProductName, String InProductImage, String InProductUnitMeasure, String InProductPrice, String InProductMargin, String InProductProfit, String InProductCusPrice, String vendorcost, String Smargin, String Sprofit) {
        this.InProductName = InProductName;
        this.InProductImage = InProductImage;
        this.InProductUnitMeasure = InProductUnitMeasure;
        this.InProductPrice = InProductPrice;
        this.InProductMargin = InProductMargin;
        this.InProductProfit = InProductProfit;
        this.InProductCusPrice = InProductCusPrice;
        this.vendorcost = vendorcost;
        this.Smargin = Smargin;
        this.Sprofit = Sprofit;
    }


    public String getInProductName() {
        return InProductName;
    }

    public void setInProductName(String inProductName) {
        InProductName = inProductName;
    }

    public String getInProductImage() {
        return InProductImage;
    }

    public void setInProductImage(String inProductImage) {
        InProductImage = inProductImage;
    }

    public String getInProductUnitMeasure() {
        return InProductUnitMeasure;
    }

    public void setInProductUnitMeasure(String inProductUnitMeasure) {
        InProductUnitMeasure = inProductUnitMeasure;
    }

    public String getInProductPrice() {
        return InProductPrice;
    }

    public void setInProductPrice(String inProductPrice) {
        InProductPrice = inProductPrice;
    }

    public String getInProductMargin() {
        return InProductMargin;
    }

    public void setInProductMargin(String inProductMargin) {
        InProductMargin = inProductMargin;
    }

    public String getInProductProfit() {
        return InProductProfit;
    }

    public void setInProductProfit(String inProductProfit) {
        InProductProfit = inProductProfit;
    }

    public String getInProductCusPrice() {
        return InProductCusPrice;
    }

    public void setInProductCusPrice(String inProductCusPrice) {
        InProductCusPrice = inProductCusPrice;
    }

    public String getVendorcost() {
        return vendorcost;
    }

    public void setVendorcost(String vendorcost) {
        this.vendorcost = vendorcost;
    }

    public String getSmargin() {
        return Smargin;
    }

    public void setSmargin(String smargin) {
        Smargin = smargin;
    }

    public String getSprofit() {
        return Sprofit;
    }

    public void setSprofit(String sprofit) {
        Sprofit = sprofit;
    }
}