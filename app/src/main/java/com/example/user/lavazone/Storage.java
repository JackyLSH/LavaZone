package com.example.user.lavazone;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Storage {
    private Context context;
    private String filenameInternal = "";
    private String filenameExternal = "";
    private Gson gson = new Gson();
    private String classType = "";
    private String innerClassType = "";

    public Storage(Context ctx, String str, String cls) {
        context = ctx;
        filenameInternal = str;
        classType = cls;
    }


    public void writeFileInternalStorage(Object obj) {
//        String cls = obj.getClass().getName();
//        Type type = null;
//
//        // Type List<>
//        if (cls.equals("java.util.ArrayList")) {
//            List<Object> listObj = (List<Object>) obj;
//            String innerCls = listObj.get(0).getClass().getName();
//
//            // Type List<Item>
//            if (innerCls.equals("com.example.user.lavazone.Item"))
//            type = new TypeToken<List<Item>>(){}.getType();
//        }
        if (classType.equals("jpg")) {
            writeImgInternalStorage((Drawable) obj);
            return;
        }
        String str = gson.toJson(obj, getTypeFromString());
        createUpdateFile(filenameInternal, str, false);
    }

    public void appendFileInternalStorage(Object obj) {

        ArrayList<CartItem> list;
        if (new File(context.getFilesDir(), filenameInternal).exists()) {
            list = (ArrayList<CartItem>) readFileInternalStorage();
        } else {
            list = new ArrayList<CartItem>();
        }
        list.add((CartItem)obj);
        String str = gson.toJson(list, getTypeFromString());
        createUpdateFile(filenameInternal, str, false);
    }

    private void createUpdateFile(String fileName, String content, boolean update) {
        FileOutputStream outputStream;

        try {
            if (update) {
                outputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
            } else {
                outputStream = context.openFileOutput(fileName, MODE_PRIVATE);
            }
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.d("AppMsg", "Error in createUpdateFile: " + e.getMessage());
        }
    }

    public Object readFileInternalStorage() {
        if (classType.equals("jpg")) {
            return readImgInternalStorage();
        }

        File file = new File(context.getFilesDir(), filenameInternal);
        if (file.exists()){
            try {
                FileInputStream fileInputStream = context.openFileInput(filenameInternal);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

                StringBuffer sb = new StringBuffer();
                String line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }

                return gson.fromJson(sb.toString(), getTypeFromString());

            } catch (Exception e) {
                Log.d("AppMsg", "Error in readFile: " + e.getMessage() + "\n");
            }
        }
        return new ArrayList<Object>();
    }

    public void deleteCartItem(CartItem item) {
        try{
            List<CartItem> list = (List<CartItem>) readFileInternalStorage();
            list.remove(item);
            writeFileInternalStorage(list);
        }
        catch (Exception e) {
            Log.d("AppMsg", "Error in delete" + e.getMessage());
        }

    }
    public List<CartItem> readCartItemInternalStorage() {
        ArrayList<CartItem> list = new ArrayList<>();
//        JsonReader.setLenient(true);

        try {
            FileInputStream fileInputStream = context.openFileInput(filenameInternal);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                Log.d("AppMsg", line);
                sb.append(line);
                sb.append(",");
                line = reader.readLine();
                list.add((CartItem) gson.fromJson(sb.toString(), getTypeFromString()));
            }
            return list;

        } catch (Exception e) {
            Log.d("AppMsg", "Error in readFile: " + e.getMessage() + "\n");
        }
        return null;
    }

    public void writeImgInternalStorage(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ContextWrapper wrapper = new ContextWrapper(context);
        File dir = wrapper.getDir("Images",MODE_PRIVATE);
        File file = new File(dir, filenameInternal);
        try {
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//            Log.d("AppMsg", stream.);
//            stream.flush();
            stream.close();
        } catch (Exception e) {
            Log.d("AppMsg", "Error in writeImg: " + e.getMessage() + "\n");
        }
    }

    public Bitmap readImgInternalStorage() {
        ContextWrapper wrapper = new ContextWrapper(context);
        File dir = wrapper.getDir("Images",MODE_PRIVATE);
        File file = new File(dir, filenameInternal);
        try {
            //FileInputStream fileInputStream = context.openFileInput(filenameInternal);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void createTemporaryFile() {
        try {
            String fileName = "couponstemp";
            String coupons = "Get upto 50% off shoes @ xyx shop \n Get upto 80% off on shirts @ yuu shop";

            File file = File.createTempFile(fileName, null, context.getCacheDir());

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(coupons.getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
        }
    }

    public void deleteFile() {
//        try {
//            File file = File.createTempFile(filenameInternal, null, context.getCacheDir());
            File file = new File(context.getFilesDir(), filenameInternal);
            file.delete();
//        }
    }

    public void writeFileExternalStorage() {
        String cashback = "Get 2% cashback on all purchases from xyz \n Get 10% cashback on travel from dhhs shop";
        String state = Environment.getExternalStorageState();
        //external storage availability check
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), filenameExternal);


        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);

            outputStream.write(cashback.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.d("AppMsg", "Error in writeFileExternalStorage: " + e.getMessage());
        }

    }

    public Type getType(Object obj) {
        String cls = obj.getClass().getName();
        Log.d("AppMsg", "getType: " + cls);
        Type type = null;

        // Type List<>
        if (cls.equals("java.util.ArrayList")) {
            List<Object> listObj = (List<Object>) obj;
            String innerCls = listObj.get(0).getClass().getName();

            // Type List<Item>
            if (innerCls.equals("com.example.user.lavazone.Item"))
                return new TypeToken<List<Item>>(){}.getType();
        }

        // Type InputStream
        if (cls.equals("com.android.okhttp.okio.RealBufferedSource$1")) {
            return new TypeToken<InputStream>(){}.getType();
        }
        return type;
    }

    public Type getTypeFromString() {
        Type type = null;

        // Type List<>
        if (classType.equals("List<Item>")) {
            return new TypeToken<List<Item>>(){}.getType();
        }

        // Type InputStream
        if (classType.equals("jpg")) {
            return new TypeToken<InputStream>(){}.getType();
        }

        // Type CartItem
        if (classType.equals("CartItem")) {
            return new TypeToken<CartItem>(){}.getType();
        }

        // Type List<CartItem>
        if (classType.equals("List<CartItem>")) {
            return new TypeToken<List<CartItem>>(){}.getType();
        }

        return type;
    }

}
