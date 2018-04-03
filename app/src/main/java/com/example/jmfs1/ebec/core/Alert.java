package com.example.jmfs1.ebec.core;

import com.orm.SugarRecord;

/**
 * Created by jeronimo on 3/10/17.
 */

public class Alert extends SugarRecord {

    private String text;
    private String date;

    public Alert() {

    }

    public Alert(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
