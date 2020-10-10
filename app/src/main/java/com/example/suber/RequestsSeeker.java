package com.example.suber;

public class RequestsSeeker {
    public String recruitername;
    public String imgUrl;
    public String recruiterId;
    public String txtSpec;
    public  String vidUrl;
    public String subCat;
    public String statusCode;
    public String orderId;
    public String paymentStatus;
    public String amount;


    public RequestsSeeker(){}

    public RequestsSeeker(String recruitername,String recruiterId, String subCat, String txtSpec, String statusCode, String orderId,String imgUrl,String vidUrl,String paymentStatus,String amount) {
        this.recruitername = recruitername;
        this.imgUrl = imgUrl;
        this.recruiterId = recruiterId;
        this.txtSpec = txtSpec;
        this.vidUrl = vidUrl;
        this.subCat = subCat;
        this.statusCode = statusCode;
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

    public String getRecruitername() {
        return recruitername;
    }

    public void setRecruitername(String recruitername) {
        this.recruitername = recruitername;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

