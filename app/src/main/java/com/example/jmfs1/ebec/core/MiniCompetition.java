package com.example.jmfs1.ebec.core;

/**
 * Created by root on 3/5/17.
 */

public class MiniCompetition {

    // Attributes
    String name;
    int credits;

    public MiniCompetition() {

    }

    public MiniCompetition(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
