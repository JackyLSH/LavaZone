package com.example.user.lavazone;

import java.util.Date;

public class Item {
    public int item_id = 0;
    public String name = "";
    public String item_description = "";
    public float price = 0;
    public int store_id = 0;
    public Date post_date = new Date();

    public Item(int item_id, String name, String item_description, float price, int store_id, Date post_date) {
        this.item_id = item_id;
        this.name = name;
        this.item_description = item_description;
        this.price = price;
        this.store_id = store_id;
        this.post_date = post_date;
    }

}
