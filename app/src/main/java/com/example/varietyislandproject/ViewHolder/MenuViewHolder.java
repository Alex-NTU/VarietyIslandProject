package com.example.varietyislandproject.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varietyislandproject.Interface.itemClickListener;
import com.example.varietyislandproject.R;

import org.w3c.dom.Text;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtMenuName;
        public ImageView imageView;
        private com.example.varietyislandproject.Interface.itemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName = (TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView)itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(com.example.varietyislandproject.Interface.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
