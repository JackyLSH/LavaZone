package com.example.user.lavazone;

public class CartItem {
    Item item;
    int quantity = 0;
    String store_name = "";

    public CartItem() {

    }

    public CartItem(Item i, int q) {
        item = i;
        quantity = q;

        Database db = new Database();
    }
}
