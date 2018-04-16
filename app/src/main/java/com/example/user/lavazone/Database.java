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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

    //below method need testing...
    public Item getItemDetailByID(int id) throws Exception {
        String query = "request=itembyid&itemid=" + id;
        String tempurl = url + "?" + URLEncoder.encode(query, "UTF-8");
        URL targetURL = new URL(tempurl);
        HttpURLConnection con = (HttpURLConnection) targetURL.openConnection();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Gson gson = new Gson();
        Item item = gson.fromJson(response.toString(), Item.class);
        return item;
    }

    public Store getStoreDetailByID(int id) throws Exception{
        String query = "request=itembyid&itemid=" + id;
        String tempurl = url + "?" + URLEncoder.encode(query, "UTF-8");
        URL targetURL = new URL(tempurl);
        HttpURLConnection con = (HttpURLConnection) targetURL.openConnection();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Gson gson = new Gson();
        Store store = gson.fromJson(response.toString(), Store.class);
        return store;
    }

    public List<Item> getRecentItem(int item_num){
        /*Parameter: no of item to get from Database
        *Return: List of Item object
        */
        ArrayList<Item> itemList = new ArrayList<Item>();
        //to be implement


        return  itemList;
    }

    public boolean sentOrderRecord(String name, String addr, String email, int tel){
        /*Return: a boolean of success order sent
        */

        //to be implemenet


        return false;
    }

    public List<Item> searchItem(String[] keyword){
        /*Parameter: list of string as search keyword
        Return: List of Item of search result
         */
        ArrayList<Item> itemList = new ArrayList<Item>();

        return itemList;
    }

}
