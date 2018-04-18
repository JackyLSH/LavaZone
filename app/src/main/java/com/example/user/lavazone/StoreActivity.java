package com.example.user.lavazone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class StoreActivity extends AppCompatActivity {
    private Store store;
    private String imgURLPrefix = "http://www2.comp.polyu.edu.hk/~15093307d/imagedb/";
    private String imgURLPostfix = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //on select navigation bar highlight
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(StoreActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_advasearch_search:
                        Intent intent1 = new Intent(StoreActivity.this, AdvancedSearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_cart:
                        Intent intent2 = new Intent(StoreActivity.this, CartActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        if (isNetworkAvailable()) {
            // Get store info
            Database db = new Database();
            int store_id = getIntent().getIntExtra("store_id", 0);
            try {
                store = db.getStoreDetailByID(store_id);
                URL store_img_url = new URL(imgURLPrefix+db.getItemImg(store_id).get(0)+imgURLPostfix);
                InputStream content = (InputStream)store_img_url.getContent();
                Drawable d = Drawable.createFromStream(content, "src");
                ImageView iv = (ImageView)this.findViewById(R.id.storeImg);
                iv.setImageDrawable(d);

                TextView tv_store_name = (TextView)this.findViewById(R.id.storeName);
                TextView tv_store_loc = (TextView)this.findViewById(R.id.storeLocation);
                TextView tv_store_tel = (TextView)this.findViewById(R.id.storeTel);
                tv_store_name.setText(store.name);
                tv_store_loc.setText(store.location);
                tv_store_tel.setText(Integer.toString(store.tel));
                hideError();
            } catch (Exception e) {
                Log.d("AppMsg", "Error");
                Log.d("AppMsg", e.getMessage());
                showError();
            }
        }
        else {
            showError();
        }

    }

    // Check network
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    // Show error page
    protected void showError() {
        ImageView iv_warngingImg = (ImageView)this.findViewById(R.id.warningImg);
        TextView tv_warningLb = (TextView)this.findViewById(R.id.warningLb);
        iv_warngingImg.setVisibility(View.VISIBLE);
        tv_warningLb.setVisibility(View.VISIBLE);

        ScrollView sv_storeScroll = (ScrollView)this.findViewById(R.id.storeScroll);
        sv_storeScroll.setVisibility(View.INVISIBLE);
    }

    // Hide error page
    protected void hideError() {
        ImageView iv_warngingImg = (ImageView)this.findViewById(R.id.warningImg);
        TextView tv_warningLb = (TextView)this.findViewById(R.id.warningLb);
        iv_warngingImg.setVisibility(View.INVISIBLE);
        tv_warningLb.setVisibility(View.INVISIBLE);

        ScrollView sv_storeScroll = (ScrollView)this.findViewById(R.id.storeScroll);
        sv_storeScroll.setVisibility(View.VISIBLE);
    }
}
