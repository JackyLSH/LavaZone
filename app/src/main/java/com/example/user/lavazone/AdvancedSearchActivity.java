package com.example.user.lavazone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;


public class AdvancedSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

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
                        Intent intent0 = new Intent(AdvancedSearchActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_advasearch_search:
                        Intent intent1 = new Intent(AdvancedSearchActivity.this, AdvancedSearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_cart:
                        Intent intent2 = new Intent(AdvancedSearchActivity.this, CartActivity.class);
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
                    Intent intent = new Intent(AdvancedSearchActivity.this, ItemListActivity.class);
                    intent.putExtra("keywords", keys);
                    startActivity(intent);
                };

            }
        });
        //end of top search function


        final Spinner spinnerCatelog = (Spinner) findViewById(R.id.categories_spinner);
        final ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(AdvancedSearchActivity.this,R.array.categories_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerCatelog.setAdapter(adapter0);

        final Spinner spinnerTexture = (Spinner) findViewById(R.id.texture_spinner);
        final ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(AdvancedSearchActivity.this,R.array.texture_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerTexture.setAdapter(adapter1);

        final Spinner spinnerColor = (Spinner) findViewById(R.id.color_spinner);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(AdvancedSearchActivity.this,R.array.color_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapter2);

        Button submit = (Button) findViewById(R.id.as_search_but);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] stringArr = {spinnerCatelog.getSelectedItem().toString(), spinnerTexture.getSelectedItem().toString(), spinnerColor.getSelectedItem().toString()};
                Intent intent = new Intent(AdvancedSearchActivity.this, ItemListActivity.class);
                intent.putExtra("keywords", stringArr);
                startActivity(intent);
            }
        });
    }
}
