package com.example.user.lavazone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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

public class CartListAdapter extends ArrayAdapter<CartItem>{
    private Context ctx;
    int res;
    private Item item;
    private List<CartItem> cartItems;
    private Intent intent;

    public CartListAdapter(@NonNull Context context, int resource, @NonNull List<CartItem> objects, Intent i) {
        super(context, resource, objects);
        ctx = context;
        res = resource;
        cartItems = objects;
        intent = i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        item = getItem(position).item;
        int quantity = getItem(position).quantity;

        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(res, parent, false);

        ImageView ivItemImg = (ImageView) convertView.findViewById(R.id.cart_item_image);
        TextView tvItemName = (TextView) convertView.findViewById(R.id.cart_item_name);
        TextView tvItemPrice = (TextView) convertView.findViewById(R.id.cart_item_price);
        TextView tvStoreName = (TextView) convertView.findViewById(R.id.cart_store_name);
        TextView tvItemSum = (TextView) convertView.findViewById(R.id.cart_total_item_sum);
        ImageButton ib = (ImageButton) convertView.findViewById(R.id.cart_remove);
        TextInputEditText quan = (TextInputEditText)convertView.findViewById(R.id.cart_input_quantity);

        //ivItemImg.setText(name);//local img preview
        tvItemName.setText(item.name);
        tvItemPrice.setText(String.valueOf(item.price));
        tvStoreName.setText(getItem(position).store_name);
        tvItemSum.setText(String.valueOf(item.price*quantity));
        quan.setText(String.valueOf(quantity));


        try {
            Storage st = new Storage(ctx, "cartItemImg" + item.item_id + ".jpg", "jpg");
            Bitmap d = (Bitmap)st.readImgInternalStorage();
            ivItemImg.setImageBitmap(d);
        } catch (Exception e) {
            Log.d("ItemAdapter", "Error");
            Log.d("Msg", e.getMessage());
        }


        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getitem()
//                getItem(position).quantity = 0;
                Storage st = new Storage(ctx, "cartItem.dat", "List<CartItem>");
                Log.d("AppMsg", "deleting: " + cartItems.get(position).item.name);
                st.deleteCartItem(cartItems.get(position));
                Log.d("AppMsg", "After...");
//                ((Activity)ctx).finish();
//                ctx.startActivity(intent);
                cartItems.remove(position);
                ((CartActivity)ctx).updateTotalSum();

            }
        });

        return convertView;
    }
}
