package com.example.user.lavazone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;

public class ItemDetailActivity extends AppCompatActivity {
    Item item = null;
    Store store = null;
    Drawable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

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
                        Intent intent0 = new Intent(ItemDetailActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_advasearch_search:
                        Intent intent1 = new Intent(ItemDetailActivity.this, AdvancedSearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_cart:
                        Intent intent2 = new Intent(ItemDetailActivity.this, CartActivity.class);
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
                    Intent intent = new Intent(ItemDetailActivity.this, ItemListActivity.class);
                    intent.putExtra("keywords", keys);
                    startActivity(intent);
                };

            }
        });
        //end of top search function

        Intent intent = getIntent();
        int curItemID = intent.getIntExtra("item_id",0);
        Database db = new Database();

        if (curItemID!=0) {
            try {
                item = db.getItemDetailByID(curItemID);
                store = db.getStoreDetailByID(item.store_id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (item!=null && store!=null) {
                //display
                try {
                    ImageView ivItemImage = (ImageView)findViewById(R.id.imageView1);
                    URL imgURL = new URL("http://www2.comp.polyu.edu.hk/~15093307d/imagedb/" + db.getItemImg(curItemID).get(0) + ".jpg");
                    InputStream content = (InputStream) imgURL.getContent();
                    d = Drawable.createFromStream(content, "src");
                    ivItemImage.setImageDrawable(d);
                } catch (Exception e) {
                    Log.d("ItemAdapter", "Error");
                    Log.d("Msg", e.getMessage());
                }
                TextView textView7 = (TextView) findViewById(R.id.textView7);
                textView7.setText(item.name);
                TextView textView8 = (TextView) findViewById(R.id.textView8);
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dat = formatter.format(item.post_date);
                textView8.setText(dat);
                TextView textView9 = (TextView) findViewById(R.id.textView9);
                textView9.setText(String.valueOf(item.price));
                TextView textView10 = (TextView) findViewById(R.id.textView10);
                textView10.setText(item.item_description);
                TextView textView11 = (TextView) findViewById(R.id.textView11);
                textView11.setText(store.name);
                TextView textView12 = (TextView) findViewById(R.id.textView12);
                textView12.setText(store.location);

                Button addButton = (Button) findViewById(R.id.Addbutton);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edit = (EditText) findViewById(R.id.editText4);
                        if (edit.getText().toString().isEmpty()) {
                            return;
                        }
                        // Write cartItem to storage
                        int quantity = Integer.parseInt(edit.getText().toString());
                        CartItem cartItem = new CartItem(item, quantity);
                        Storage storage_cartItem = new Storage(ItemDetailActivity.this, "cartItem.dat", "List<CartItem>");
                        storage_cartItem.appendFileInternalStorage(cartItem);

                        // Write cartItemImg to storage
                        Storage storage_cartItemImg = new Storage(ItemDetailActivity.this, "cartItemImg" + item.item_id + ".jpg", "jpg");
                        storage_cartItemImg.writeImgInternalStorage(d);

                        Intent intent = new Intent(ItemDetailActivity.this, CartActivity.class);
                        startActivity(intent);

//                        intent.putExtra("item_id", 1);
//                        intent.putExtra("store_id", 1);
//                        intent.putExtra("qq", quantity);
                    }
                });
            }
        }
    }


}
