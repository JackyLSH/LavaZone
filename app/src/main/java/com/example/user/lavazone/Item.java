package com.example.user.lavazone;

import com.google.gson.Gson;

import java.util.Date;

public class Item {
    public int item_id = 0;
    public String name = "";
    public String item_description = "";
    public float price = 0;
    public int store_id = 0;
    public Date post_date = new Date();

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String className() {
        return "Item";
    }
}
