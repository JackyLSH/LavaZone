package com.example.user.lavazone;

public class CartItem {
    Item item;
    int quantity = 0;

    public CartItem(Item i, int q) {
        item = i;
        quantity = q;
    }
}
