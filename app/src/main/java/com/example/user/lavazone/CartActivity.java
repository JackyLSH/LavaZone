package com.example.user.lavazone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private final String imgURLPrefix = "http://www2.comp.polyu.edu.hk/~15093307d/imagedb/";
    private final String imgURLPostfix = ".jpg";
    public static Activity fa;
    CartListAdapter adapter;
    List<CartItem> cartItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fa = this;

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

//        if (item_id != 0 && store_id != 0 && quantity != 0) {
//            try {
//                item = db.getItemDetailByID(item_id);
//            } catch (Exception e) {
//                Log.d("AppMsg", "DB error 1: " + e.getMessage());
//            }
//            try {
//                store = db.getStoreDetailByID(store_id);
//            } catch (Exception e) {
//                Log.d("AppMsg", "DB error 2: " + e.getMessage());
//            }
//            // Set cart item details
//            float price = 0;
//            TextView textView29 = (TextView) findViewById(R.id.textView29);
//            TextView textView26 = (TextView) findViewById(R.id.textView26);
//            TextView textView27 = (TextView) findViewById(R.id.textView27);
//            TextView textView25 = (TextView) findViewById(R.id.textView25);
//            TextView textView24 = (TextView) findViewById(R.id.textView24);
//            textView29.setText(item.name);
//            textView26.setText(String.valueOf(item.price));
//            textView27.setText(String.valueOf(quantity));
//            textView25.setText(store.name);
//            price=item.price*quantity;
//            total+=price;
//            textView24.setText(String.valueOf(price));

            // Set cart item image
//            ImageView iv_cartItemImg = (ImageView) findViewById(R.id.cartItemImg1);
//            try {
//                URL imgURL = new URL(imgURLPrefix + Integer.toString(db.getItemImg(item_id).get(0)) + imgURLPostfix);
//                InputStream content = null;
//                content = (InputStream)imgURL.getContent();
//                Drawable d = Drawable.createFromStream(content, "src");
//                iv_cartItemImg.setImageDrawable(d);
//            } catch (Exception e) {
//                Log.d("AppMsg", e.getMessage());
//            }
//        } else {
////            LinearLayout ll_left = (LinearLayout) findViewById(R.id.cartItemLeft);
////            LinearLayout ll_mid = (LinearLayout) findViewById(R.id.cartItemMid);
////            LinearLayout ll_right = (LinearLayout) findViewById(R.id.cartItemRight);
////            ll_left.setVisibility(View.GONE);
////            ll_mid.setVisibility(View.GONE);
////            ll_right.setVisibility(View.GONE);
//
//        }

        //calculate total price
//        TextView textView15 = (TextView) findViewById(R.id.textView15);
//        textView15.setText(String.valueOf(total));
//        Log.d("AppMsg", "Value of total");


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

        //top search function for every Activity
        final TextInputEditText topSearchText = (TextInputEditText) findViewById(R.id.topSearch);
        ImageButton topSearchBut = (ImageButton) findViewById(R.id.topSearchButtom);
        topSearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = topSearchText.getText().toString();
                if (!key.equals("")) {
                    String[] keys = {key};
                    Intent intent = new Intent(CartActivity.this, ItemListActivity.class);
                    intent.putExtra("keywords", keys);
                    startActivity(intent);
                };

            }
        });
        //end of top search function

        ListView lv = (ListView)findViewById(R.id.cart_list_view);

        Storage storage_cartItem = new Storage(CartActivity.this, "cartItem.dat", "List<CartItem>");
        cartItemList = (List<CartItem>) storage_cartItem.readFileInternalStorage();


        adapter = new CartListAdapter(CartActivity.this, R.layout.adapter_cart_listview_layout, cartItemList, getIntent());
        lv.setAdapter(adapter);

        updateTotalSum();

        Button btn = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getitem()
//                getItem(position).quantity = 0;
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                startActivity(intent);

            }
        });
    }


    // Clear focus on touch outside EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void updateTotalSum(){
        float amount = 0;
        for (CartItem ci : cartItemList) {
            amount += ci.quantity * ci.item.price;
        }

        TextView tv_amount = (TextView)findViewById(R.id.amount);
        tv_amount.setText("$"+String.valueOf(amount));
        adapter.notifyDataSetChanged();
    }

}
