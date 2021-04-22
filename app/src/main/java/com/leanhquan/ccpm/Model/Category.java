package com.leanhquan.ccpm.Model;

import androidx.annotation.NonNull;

public class Category {
    private String name, date;

    public Category() {
    }

    public Category(String name, String date) {
        this.name = name;
        this.date = date;
    }
    @NonNull
    @Override
    public String toString() {
        return name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
