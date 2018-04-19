package com.example.user.lavazone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private File externalStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    File recentItemsFile = new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItems.json");
    File[] recentItemsImgFiles = {
            new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg1.jpg"),
            new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg2.jpg"),
            new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg3.jpg"),
            new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg4.jpg")
    };
//    File recentItemsImgFile1 = new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg1.json");
//    File recentItemsImgFile2 = new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg2.json");
//    File recentItemsImgFile3 = new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg3.json");
//    File recentItemsImgFile4 = new File(externalStorageDirectory + File.separator + "LavaZone" + File.separator + "homepage", "homeRecentItemsImg4.json");

    private String imgURLPrefix = "http://www2.comp.polyu.edu.hk/~15093307d/imagedb/";
    private String imgURLPostfix = ".jpg";

    private Database db;
    private List<Item> recentItems;
    private LinearLayout[] ll_latestItem;

    private Storage storage_recentItems = new Storage(this, "homeRecentItems.json", "List<Item>");
    private Storage[] storage_recentItemsImg =  {
            new Storage(this, "homeRecentItemsImg1.jpg", "jpg"),
            new Storage(this, "homeRecentItemsImg2.jpg", "jpg"),
            new Storage(this, "homeRecentItemsImg3.jpg", "jpg"),
            new Storage(this, "homeRecentItemsImg4.jpg", "jpg"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //on select navigation bar highlisht
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_advasearch_search:
                        Intent intent1 = new Intent(HomeActivity.this, AdvancedSearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_cart:
                        Intent intent2 = new Intent(HomeActivity.this, CartActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        // Permitting Internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Storage Permissions
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        db = new Database();
        ImageView[] iv_latestItemImg = {
                (ImageView)this.findViewById(R.id.latestItemImg1),
                (ImageView)this.findViewById(R.id.latestItemImg2),
                (ImageView)this.findViewById(R.id.latestItemImg3),
                (ImageView)this.findViewById(R.id.latestItemImg4)
        };

        TextView[] tv_latestItemName = {
                (TextView)this.findViewById(R.id.latestItemName1),
                (TextView)this.findViewById(R.id.latestItemName2),
                (TextView)this.findViewById(R.id.latestItemName3),
                (TextView)this.findViewById(R.id.latestItemName4)
        };

        LinearLayout[] ll_latestItem = {
                (LinearLayout)this.findViewById(R.id.latestItem1),
                (LinearLayout)this.findViewById(R.id.latestItem2),
                (LinearLayout)this.findViewById(R.id.latestItem3),
                (LinearLayout)this.findViewById(R.id.latestItem4)
        };

        this.ll_latestItem = ll_latestItem;
        Gson gson = new Gson();

        if (isNetworkAvailable()) {
            try {
                recentItems = db.getRecentItem(4);

                // save recentItems to storage
                writeToFile(storage_recentItems, recentItems);

                for (int num=0; num<4; num++) {
                    if (recentItems.size() > num) {
                        // set item
                        URL imgURL = new URL(imgURLPrefix + Integer.toString(db.getItemImg(recentItems.get(num).item_id).get(0)) + imgURLPostfix);
                        InputStream content = (InputStream)imgURL.getContent();
                        Drawable d = Drawable.createFromStream(content, "src");
                        iv_latestItemImg[num].setImageDrawable(d);
                        tv_latestItemName[num].setText(recentItems.get(num).name);

                        // save recentItemImg to storage
//                        gson.toJson(content, new FileWriter(recentItemsImgFiles[num].getAbsolutePath()));
                        writeToFile(storage_recentItemsImg[num], d);

                    } else {
                        // set invisible
                        iv_latestItemImg[num].setVisibility(View.GONE);
                        tv_latestItemName[num].setVisibility(View.GONE);

                    }
                }
            } catch (Exception e) {
                Log.d("AppMsg", "Error");
                Log.d("AppMsg", e.getMessage());
            }
            //itemOnClick();
        } else {

                recentItems = (List<Item>) readFromFile(storage_recentItems);

                if (recentItems == null) {
                    // Error
                    Log.d("AppMsg", "Fatal Error 1: no recentItems");
                }

                for (int num=0; num<4; num++) {
                    if (recentItems.size() > num) {
                        // set item
                        tv_latestItemName[num].setText(recentItems.get(num).name);

                        Bitmap b = (Bitmap) readFromFile(storage_recentItemsImg[num]);
                        iv_latestItemImg[num].setImageBitmap(b);

                    } else {
                        // set invisible
                        iv_latestItemImg[num].setVisibility(View.GONE);
                        tv_latestItemName[num].setVisibility(View.GONE);

                    }
                }

        }
    }

    // Check network
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("AppMsg", "Network check: " + (activeNetworkInfo != null && activeNetworkInfo.isConnected()));
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Checks if external storage is available to at least read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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

    // Item OnClick
    public void itemOnClick(View v) {
        for (int num=0; num<recentItems.size(); num++) {
            if (v == ll_latestItem[num]) {
                Intent intent = new Intent(this, ItemDetailActivity.class);
                intent.putExtra("item_id", recentItems.get(num).item_id);
                startActivity(intent);
                return;
            }
        }

        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("store_id", 1);
        startActivity(intent);
    }

    // Write to file
    private void writeToFile(Storage storage, Object obj) {
        if (isExternalStorageWritable()) {
//            if (!file.exists()) {
//                try {
//                    file.mkdirs();
//                    file.createNewFile();
//                } catch (IOException e) {
//                    Log.d("AppMsg", "Error in creating file: " + e.getMessage());
//                }
//            }
//            try {
//                Gson gson = new Gson();
//                FileOutputStream fOut = new FileOutputStream(file);
//                OutputStreamWriter fileWriter = new OutputStreamWriter(fOut);
//                fileWriter.write(gson.toJson(obj));
//                fileWriter.close();
//                fOut.close();
//                Log.d("AppMsg", "File written to " + file.getAbsolutePath());
//            } catch (Exception e) {
//                Log.d("AppMsg", "Error in writing: " + e.getMessage());
//            }
            storage.writeFileInternalStorage(obj);
        }
        else {
            Log.d("AppMsg", "Storage not writable");
        }
    }

    private Object readFromFile(Storage storage) {
        if (isExternalStorageReadable()) {
//            if (!file.exists()) {
//                Log.d("AppMsg", "File not found");
//                return null;
//            }
//            Log.d("AppMsg", file.getName()+" found");
//            try {
//                Gson gson = new Gson();
//                BufferedReader br = new BufferedReader(new FileReader(file));
//                String line;
//                StringBuilder text = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    text.append(line);
//                    text.append('\n');
//                }
//                br.close();
//                return text.toString();
//            } catch (IOException e) {
//                Log.d("AppMsg", "Error in reading file: " + e.getMessage());
//            }
            return storage.readFileInternalStorage();
        }

        Log.d("AppMsg", "Storage not readable");
        return null;
    }


}
