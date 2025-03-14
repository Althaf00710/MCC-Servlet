package com.example.megacitycab.models.booking;
import java.util.Date;

public class Billing {
    private int id;
    private int bookingId;
    private double totalDistanceFare;
    private double totalWaitFare;
    private Date billDate;
    private double tax; //tax percentage
    private double discount; //discount percentage
    private int userId;
    private double cash;
    private double deposit;
    private double card;
    private double totalAmount;

    public double getTotalAmount() {return totalAmount;}
    public void setTotalAmount(double totalAmount) {this.totalAmount = totalAmount;}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getBookingId() {
        return bookingId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    public double getTotalDistanceFare() {
        return totalDistanceFare;
    }
    public void setTotalDistanceFare(double totalDistanceFare) {
        this.totalDistanceFare = totalDistanceFare;
    }
    public double getTotalWaitFare() {
        return totalWaitFare;
    }
    public void setTotalWaitFare(double totalWaitFare) {
        this.totalWaitFare = totalWaitFare;
    }
    public Date getBillDate() {
        return billDate;
    }
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public double getCash() {
        return cash;
    }
    public void setCash(double cash) {
        this.cash = cash;
    }
    public double getDeposit() {
        return deposit;
    }
    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
    public double getCard() {
        return card;
    }
    public void setCard(double card) {
        this.card = card;
    }
}
