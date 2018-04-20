package com.example.user.lavazone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

public class TransationActivity extends AppCompatActivity {

    String JSON_STRING;

    String textAddr, textName, textEmail;
    TextView tv_TextName, tv_TextCardNum,tv_BillingAddr, tv_TextExpDate, tv_TextCVC;
    EditText et_name, et_cardNum, et_billAddr, et_expDate, et_cvc, et_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transation);

        // Permitting Internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Database db = new Database();
        et_name = (EditText)findViewById(R.id.inputName2);
        et_cardNum = (EditText)findViewById(R.id.inputCard);
        et_billAddr = (EditText)findViewById(R.id.inputBill);
        et_expDate = (EditText)findViewById(R.id.dateInput);
        et_cvc = (EditText)findViewById(R.id.cvcInput);
        et_tel = (EditText)findViewById(R.id.telInput);

        if (isNetworkAvailable()) {
            textAddr = getIntent().getStringExtra("addr");
            textName = getIntent().getStringExtra("name");
            textEmail = getIntent().getStringExtra("email");
        }

    }

    public void OnConfirm(View view) {
        if (et_name.getText().toString().isEmpty() || et_cardNum.getText().toString().isEmpty() || et_billAddr.getText().toString().isEmpty() || et_expDate.getText().toString().isEmpty() || et_cvc.getText().toString().isEmpty() || et_tel.getText().toString().isEmpty()) {
            return;
        }
        Intent toupdate = new Intent(TransationActivity.this, HomeActivity.class);
//        toupdate.putExtra("addr", textAddr);
//        toupdate.putExtra("name", textName);
//        toupdate.putExtra("email", textEmail);

        Database db = new Database();
        Storage st = new Storage(this, "cartItem.dat", "List<CartItem>");

        List<CartItem> cartItemList = (List<CartItem>) st.readFileInternalStorage();
        int[] order = new int[cartItemList.size()*2];
        int i = 0;
        for (CartItem ci : cartItemList) {
            order[i++] = ci.item.item_id;
            order[i++] = ci.quantity;
        }
        try {
            db.sentOrderRecord(textName, textAddr, textEmail, Integer.parseInt(et_tel.getText().toString()), order);
        } catch (Exception e) {
            Log.d("AppMsg", "Error in sending order: " + e.getMessage());
        }

//        toupdate.putExtra("addr", tv_TextName.getText().toString());
//        toupdate.putExtra("name", tv_TextCardNum.getText().toString());
//        toupdate.putExtra("email", tv_BillingAddr.getText().toString());
//        toupdate.putExtra("email", tv_TextExpDate.getText().toString());
//        toupdate.putExtra("email", tv_TextCVC.getText().toString());

        st.deleteFile();

        startActivity(toupdate);
    }

    // Check network
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public class BackgroundTask extends AsyncTask<Void, Void, String> {
//        String JSON_URL;
//
//        @Override
//        protected void onPreExecute() {
//            JSON_URL ="http://www2.comp.polyu.edu.hk/~15093307d/database.php";
//        }
//
//        @Override
//            protected String doInBackground(Void... params) {
//                try {
//                    URL url = new URL(JSON_URL);
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    InputStream InputStream = httpURLConnection.getInputStream();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(InputStream));
//                    StringBuilder stringBuilder = new StringBuilder();
//                    while ((JSON_STRING = bufferedReader.readLine())!=null) {
//                        stringBuilder.append(JSON_STRING + "\n");
//                    }
//                    bufferedReader.close();
//                    InputStream.close();
//                    httpURLConnection.disconnect();
//                    String temp = stringBuilder.toString().trim();
//                    String[] TempSplited  = temp.split(Pattern.quote("/"));
//
//                    return stringBuilder.toString().trim();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//        }
//
//    }

}

