package com.example.jmfs1.ebec.core;

import java.util.List;

/**
 * Created by root on 3/5/17.
 */

public class OrderEntry {

    //Attributes
    private String product_name;
    private int price;
    private double quantity;
    private String units;

    public OrderEntry() {

    }

    public OrderEntry(String product_name, int price, double quantity, String units) {
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.units = units;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
