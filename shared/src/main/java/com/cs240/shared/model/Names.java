package com.cs240.shared.model;

import java.util.ArrayList;

public class Names {
    private ArrayList<String> data;

    public Names(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}

