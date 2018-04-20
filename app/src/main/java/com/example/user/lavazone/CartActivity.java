package com.example.user.lavazone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class CartActivity extends AppCompatActivity {
    private final String imgURLPrefix = "http://www2.comp.polyu.edu.hk/~15093307d/imagedb/";
    private final String imgURLPostfix = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        //receieve data
        int item_id= getIntent().getIntExtra("item_id",0);
        int store_id=getIntent().getIntExtra("store_id",0);
        int quantity=getIntent().getIntExtra("qq",0);

        Log.d("AppMsg","item_id: "+item_id);
        Log.d("AppMsg","store_id: "+store_id);
        Log.d("AppMsg","quantity: "+quantity);
        //connect  to database
        Database db = new Database();
        Item item = null;
        Store store = null;

        float total = 0;

        if (item_id != 0 && store_id != 0 && quantity != 0) {
            try {
                item = db.getItemDetailByID(item_id);
            } catch (Exception e) {
                Log.d("AppMsg", "DB error 1: " + e.getMessage());
            }
            try {
                store = db.getStoreDetailByID(store_id);
            } catch (Exception e) {
                Log.d("AppMsg", "DB error 2: " + e.getMessage());
            }
            // Set cart item details
            float price = 0;
            TextView textView29 = (TextView) findViewById(R.id.textView29);
            TextView textView26 = (TextView) findViewById(R.id.textView26);
            TextView textView27 = (TextView) findViewById(R.id.textView27);
            TextView textView25 = (TextView) findViewById(R.id.textView25);
            TextView textView24 = (TextView) findViewById(R.id.textView24);
            textView29.setText(item.name);
            textView26.setText(String.valueOf(item.price));
            textView27.setText(String.valueOf(quantity));
            textView25.setText(store.name);
            price=item.price*quantity;
            total+=price;
            textView24.setText(String.valueOf(price));

            // Set cart item image
            ImageView iv_cartItemImg = (ImageView) findViewById(R.id.cartItemImg1);
            try {
                URL imgURL = new URL(imgURLPrefix + Integer.toString(db.getItemImg(item_id).get(0)) + imgURLPostfix);
                InputStream content = null;
                content = (InputStream)imgURL.getContent();
                Drawable d = Drawable.createFromStream(content, "src");
                iv_cartItemImg.setImageDrawable(d);
            } catch (Exception e) {
                Log.d("AppMsg", e.getMessage());
            }
        } else {
            LinearLayout ll_left = (LinearLayout) findViewById(R.id.cartItemLeft);
            LinearLayout ll_mid = (LinearLayout) findViewById(R.id.cartItemMid);
            LinearLayout ll_right = (LinearLayout) findViewById(R.id.cartItemRight);
            ll_left.setVisibility(View.GONE);
            ll_mid.setVisibility(View.GONE);
            ll_right.setVisibility(View.GONE);

        }

        //calculate total price
        TextView textView15 = (TextView) findViewById(R.id.textView15);
        textView15.setText(String.valueOf(total));
        Log.d("AppMsg", "Value of total");


        //on select navigation bar highlight
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(CartActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_advasearch_search:
                        Intent intent1 = new Intent(CartActivity.this, AdvancedSearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_cart:
                        Intent intent2 = new Intent(CartActivity.this, CartActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
    }
}
