package com.example.user.lavazone;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ItemListAdapter extends ArrayAdapter<Item> {

    private Context ctx;
    int res;

    public ItemListAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        ctx = context;
        res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int id = getItem(position).item_id;
        String name = getItem(position).name;
        String price = String.valueOf(getItem(position).price);
        Database db = new Database();
        Store store = null;
        String storeName = "";
        try {
            store = db.getStoreDetailByID(getItem(position).store_id);
            storeName = store.name;
        } catch (Exception e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(res, parent, false);

        ImageView ivItem = (ImageView) convertView.findViewById(R.id.item_image);
        TextView tvName = (TextView) convertView.findViewById(R.id.item_name);
        TextView tvStore = (TextView) convertView.findViewById(R.id.store_name);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.price);
        ImageButton ib = (ImageButton) convertView.findViewById(R.id.forwards);

        tvName.setText(name);
        tvStore.setText(storeName);
        tvPrice.setText(price);
        try {
            URL imgURL = new URL("http://www2.comp.polyu.edu.hk/~15093307d/imagedb/" + db.getItemImg(id).get(0) + ".jpg");
            InputStream content = (InputStream) imgURL.getContent();
            Drawable d = Drawable.createFromStream(content, "src");
            ivItem.setImageDrawable(d);
        } catch (Exception e) {
            Log.d("ItemAdapter", "Error");
            Log.d("Msg", e.getMessage());
        }

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(ctx, ItemDetailActivity.class);
                next.putExtra("item_id",id);
                ctx.startActivity(next);
            }
        });

        return convertView;
    }
}
