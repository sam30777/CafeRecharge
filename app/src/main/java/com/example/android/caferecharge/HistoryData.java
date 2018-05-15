package com.example.android.caferecharge;

public class HistoryData {
    private String orderDate;
    private String orderTime;
    private String uid;
    private String key;
    private String orderNumber;
    private String orderTotal ;

    HistoryData(String orderDate, String orderTime, String orderTotal, String key, String orderNumber, String uid){
        this.orderDate=orderDate;
        this.orderTime=orderTime;
        this.orderTotal=orderTotal;
        this.uid=uid;
        this.orderNumber=orderNumber;
        this.key=key;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    HistoryData(){}
    public String getKey() {
        return key;
    }

    public String getUid() {
        return uid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }



    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }


    public String getOrderTotal() {
        return orderTotal;
    }
}
