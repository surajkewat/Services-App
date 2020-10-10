package com.example.suber;

public class ShareRequests {
    public String senderName;
    public String senderId;
    public String orderId;
    public String imgUrl;
    public String seekerId;
    public String txtSpec;
    public  String vidUrl;
    public String subCat;
    public String statusCode;
    public String paymentStatus;
    public String amount;

    public ShareRequests() {
    }

    public ShareRequests(String senderName, String senderId, String orderId, String imgUrl, String seekerId, String txtSpec, String vidUrl, String subCat, String statusCode, String paymentStatus, String amount) {
        this.senderName = senderName;
        this.senderId = senderId;
        this.orderId = orderId;
        this.imgUrl = imgUrl;
        this.seekerId = seekerId;
        this.txtSpec = txtSpec;
        this.vidUrl = vidUrl;
        this.subCat = subCat;
        this.statusCode = statusCode;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
