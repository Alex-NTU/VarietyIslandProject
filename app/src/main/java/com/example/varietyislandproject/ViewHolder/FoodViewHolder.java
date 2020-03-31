package com.example.varietyislandproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varietyislandproject.Interface.itemClickListener;
import com.example.varietyislandproject.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView foods_name;
    public ImageView foods_image;
    private com.example.varietyislandproject.Interface.itemClickListener itemClickListener;

    public void setItemClickListener(com.example.varietyislandproject.Interface.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        foods_name = (TextView)itemView.findViewById(R.id.foods_name);
        foods_image = (ImageView)itemView.findViewById(R.id.foods_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false) ;
    }
}
