package com.example.android.caferecharge;

public class User {
    String roll;
    String name;
    String card;
    String balance;

    public User(String a, String b, String c, String balance){
        this.roll=a;
        this.name=b;
        this.card=c;
        this.balance=balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public User(){}
    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public String getCard() {
        return card;
    }
}
