package com.example.suber;

public class RecruiterRequests
{
    public String name;
    public String imgUrl;
    public String seekerId;
    public String txtSpec;
    public  String vidUrl;
    public String subCat;
    public String statusCode;
    public String orderId;
    public String paymentStatus;
    public String amount;

    public RecruiterRequests(String name, String seekerId,String subCat,String txtSpec,String statusCode,String orderId, String imgUrl, String vidUrl,String paymentStatus,String amount) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.seekerId = seekerId;
        this.txtSpec = txtSpec;
        this.vidUrl = vidUrl;
        this.subCat = subCat;
        this.statusCode =statusCode;
        this.orderId = orderId;
        this.paymentStatus =paymentStatus;
        this.amount =amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public String getTxtSpec() {
        return txtSpec;
    }

    public void setTxtSpec(String txtSpec) {
        this.txtSpec = txtSpec;
    }

    public String getVidUrl() {
        return vidUrl;
    }

    public void setVidUrl(String vidUrl) {
        this.vidUrl = vidUrl;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public RecruiterRequests(){

    }

}
