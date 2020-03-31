package com.example.varietyislandproject.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.varietyislandproject.Common.Common;
import com.example.varietyislandproject.Database.DatabaseVarietyIsland;
import com.example.varietyislandproject.Model.Order;
import com.example.varietyislandproject.R;
import com.example.varietyislandproject.ShopCart;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class AdapterCart extends RecyclerView.Adapter<CartViewHolder>
{

    private List<Order> orderList = new ArrayList<>();
    private ShopCart shopCart;

    public AdapterCart(List<Order> orderList, ShopCart shopCart) {
        this.orderList = orderList;
        this.shopCart = shopCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(shopCart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {

        Picasso.with(shopCart.getBaseContext())
                .load(orderList.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cartImage);

        holder.btn_quantity.setNumber(orderList.get(position).getQuantity());
;        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = orderList.get(position);
                order.setQuantity(String.valueOf(newValue));
                new DatabaseVarietyIsland(shopCart).updateCart(order);

                float total = 0;
                List<Order> orders = new DatabaseVarietyIsland(shopCart).getCarts(Common.currentUser.getPhone()) ;
                for (Order item:orders)
                    total+=(Float.valueOf(order.getPrice()))*(Float.valueOf(item.getQuantity()));

                Locale locale = new Locale("en","GB");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                shopCart.txtTotalPrice.setText(format.format(total));
            }
        });

        Locale locale = new Locale("en","GB");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        float price = (Float.valueOf(orderList.get(position).getPrice()))*(Float.valueOf(orderList.get(position).getQuantity()));
        holder.txtprice.setText(format.format(price));
        holder.txt_cartname.setText(orderList.get(position).getProductName());

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void removeItems(int position)
    {
        orderList.remove(position);
        notifyItemRemoved(position);

    }

    public Order getItem(int position){
        return orderList.get(position);
    }

    public void restore(Order item, int position)
    {
        orderList.add(position,item);
        notifyItemInserted(position);

    }
}
