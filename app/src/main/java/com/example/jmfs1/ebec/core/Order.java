package com.example.jmfs1.ebec.core;

import java.util.List;

/**
 * Created by root on 3/5/17.
 */

public class Order {

    // Attributes
    private String team_id;
    private int cost;
    private String date;
    private String description;
    private List<OrderEntry> entries;

    public Order() {

    }

    public Order(String team_id, int cost, String date, String description, List<OrderEntry> entries) {
        this.team_id = team_id;
        this.cost = cost;
        this.date = date;
        this.description = description;
        this.entries = entries;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OrderEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<OrderEntry> entries) {
        this.entries = entries;
    }
}
