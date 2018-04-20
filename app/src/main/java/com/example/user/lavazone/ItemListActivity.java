package com.example.user.lavazone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

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
                        Intent intent0 = new Intent(ItemListActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_advasearch_search:
                        Intent intent1 = new Intent(ItemListActivity.this, AdvancedSearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_cart:
                        Intent intent2 = new Intent(ItemListActivity.this, CartActivity.class);
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
                    Intent intent = new Intent(ItemListActivity.this, ItemListActivity.class);
                    intent.putExtra("keywords", keys);
                    startActivity(intent);
                };

            }
        });
        //end of top search function

        //get passing value from prev activity
        Intent intent = getIntent();
        String[] keys = intent.getStringArrayExtra("keywords");

        //get the list from database
        Database db = new Database();
        List<Item> itemList = null;
        try {
            itemList = db.searchItem(keys);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //set the view
        ListView itemListView = (ListView) findViewById(R.id.item_list_listview);
        ItemListAdapter adapter = new ItemListAdapter(this, R.layout.adapter_item_listview_layout, itemList);
        itemListView.setAdapter(adapter);

    }
}
