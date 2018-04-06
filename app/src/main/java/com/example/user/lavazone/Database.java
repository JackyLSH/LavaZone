package com.example.user.lavazone;

import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Database {
    private final String url = "http://www2.comp.polyu.edu.hk/~14072822d/database.php";

    public String SendStringRequest(String cmd) throws Exception {
        URL req = new URL(url + "?request=" + cmd);
        HttpURLConnection conn = (HttpURLConnection) req.openConnection();
        int responseCode = conn.getResponseCode();
        Log.d("AppMsg", "Response code: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public JsonObject SendJSONRequest(String cmd) throws Exception {
//        Gson gson = new Gson();
//        JsonReader reader = new JsonReader(new StringReader(SendStringRequest(cmd)));
//        reader.setLenient(true);
//        return gson.fromJson(String.valueOf(reader), Item.class);
        return (new JsonParser()).parse(SendStringRequest(cmd)).getAsJsonObject();
    }

    public Item GetItemList() {
        return null;
    }
}
