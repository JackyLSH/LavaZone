package com.example.user.lavazone;

import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private final String url = "http://www2.comp.polyu.edu.hk/~15093307d/database.php";

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
        String query = "request=itembyid&id=" + String.valueOf(id);
        String tempurl = url + "?" + query;
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
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Item item = gson.fromJson(response.toString(), Item.class);
        return item;
    }

    public Store getStoreDetailByID(int id) throws Exception{
        String query = "request=storebyid&id=" + String.valueOf(id);
        String tempurl = url + "?" + query;
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

    public List<Item> getRecentItem(int item_num) throws Exception{
        /*Parameter: no of item to get from Database
        *Return: List of Item object
        */
        ArrayList<Item> itemList = new ArrayList<Item>();
        String query = "request=newitem&num=" + String.valueOf(item_num);
        String tempurl = url + "?" + query;
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
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Item[] itemListtemp = gson.fromJson(response.toString(),Item[].class);
        itemList = new ArrayList<Item>(Arrays.asList(itemListtemp));
        return itemList;
    }

    public boolean sentOrderRecord(String name, String addr, String email, int tel,int[] order) throws Exception{
        /*Parameter: order struc {id,quantity,id,quantity...}
        Return: a boolean of success order sent
        */

        String query = "request=makeorder&";
        StringBuilder sb = new StringBuilder();
        sb.append("recipient=");
        sb.append(URLEncoder.encode(name,"UTF-8"));
        sb.append("&address=");
        sb.append(URLEncoder.encode(addr,"UTF-8"));
        sb.append("&email=");
        sb.append(URLEncoder.encode(email,"UTF-8"));
        sb.append("&tel=");
        sb.append(tel);
        sb.append("&orders=");
        for (int i = 0; i < order.length; i++){
            if (i > 0){
                sb.append(",");
            }
            sb.append(order[i]);
            sb.append(",");
            i++;
            sb.append(order[i]);
        }
        String tempurl = url + "?" + query + sb.toString();
        System.out.println(tempurl);
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
        if (response.toString().equals("OK")){
            return true;
        }
        return false;
    }

    public List<Item> searchItem(String[] keywords) throws Exception{
        /*Parameter: list of string as search keyword
         *Return: List of Item of search result
         */
        ArrayList<Item> itemList = new ArrayList<Item>();
        String query = "request=searchitem&keyword=";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keywords.length; i++){
            if (i > 0){
                sb.append(",");
            }
            sb.append(URLEncoder.encode(keywords[i],"UTF-8"));
        }
        String tempurl = url + "?" + query + sb.toString();
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
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Item[] itemListtemp = gson.fromJson(response.toString(),Item[].class);
        itemList = new ArrayList<Item>(Arrays.asList(itemListtemp));
        return itemList;
    }

    public List<Integer> getItemImg(int id) throws Exception{
        /*Parameter: the item_id of specific
        * Return: List of image reference number
        */
        ArrayList<Integer> imgReference = new ArrayList<Integer>();
        String query = "request=itemimg&id=" + String.valueOf(id);
        String tempurl = url + "?" + query;
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
        int[] templist = gson.fromJson(response.toString(),int[].class);
        for (int i : templist) {
            imgReference.add(i);
        }
        return imgReference;
    }

    public List<Integer> getStoreImg(int id) throws Exception{
        /*Parameter: the item_id of specific
         * Return: List of image reference number
         */
        ArrayList<Integer> imgReference = new ArrayList<Integer>();
        String query = "request=storeimg&id=" + String.valueOf(id);
        String tempurl = url + "?" + query;
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
        int[] templist = gson.fromJson(response.toString(),int[].class);
        for (int i : templist) {
            imgReference.add(i);
        }
        return imgReference;
    }
}
