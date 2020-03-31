package com.example.varietyislandproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.varietyislandproject.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cartname, txtprice;
    public ElegantNumberButton btn_quantity;
    public ImageView img_cart_count;
    public ImageView cartImage;
    public RelativeLayout view_background;
    public LinearLayout foreground;
    private com.example.varietyislandproject.Interface.itemClickListener itemClickListener;

    public void setTxt_cartname(TextView txt_cartname) {
        this.txt_cartname = txt_cartname;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cartname = (TextView)itemView.findViewById(R.id.cart_item_name);
        txtprice = (TextView)itemView.findViewById(R.id.cart_item_price);
        //img_cart_count = (ImageView)itemView.findViewById(R.id.cart_item_count);
        btn_quantity = (ElegantNumberButton)itemView.findViewById(R.id.btn_quantity);
        cartImage = (ImageView) itemView.findViewById(R.id.cartImage);
        view_background = (RelativeLayout)itemView.findViewById(R.id.viewBackground);
        foreground = (LinearLayout)itemView.findViewById(R.id.Foreground);

    }

    @Override
    public void onClick(View v) {

    }
}