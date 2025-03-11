package com.example.megacitycab.DTOs;

import java.util.*;

public class DailySalesDTO {
    private Date billDate;
    private double totalSales;

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}
