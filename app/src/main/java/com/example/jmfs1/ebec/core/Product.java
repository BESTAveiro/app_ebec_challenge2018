package com.example.jmfs1.ebec.core;

/**
 * Created by jeronimo on 22/02/2017.
 */

public class Product {

    // Attributes
    private String name;
    private double price;
    private double quantity;
    private String units;
    private String category;

    public Product() {

    }

    public Product(String name, double price, double quantity, String units, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
