package com.example.varietyislandproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.varietyislandproject.Interface.itemClickListener;
import com.example.varietyislandproject.Model.Food;
import com.example.varietyislandproject.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuItem extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference listFood;
    String categoryId="";
    FirebaseRecyclerAdapter <Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        database = FirebaseDatabase.getInstance();
        listFood = database.getReference("Foods");
        recyclerView = (RecyclerView)findViewById(R.id.recylerFood);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //gets intent here
        if (getIntent()!= null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            LoadFood(categoryId);
        }

    }


    private void LoadFood(String categoryId)
    {   //fetches foods by child based on id system
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.foods,FoodViewHolder.class,listFood.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int position) {
                foodViewHolder.foods_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.foods_image);
                final Food local = food;
                foodViewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Starts the new activity for the details of the food
                        Intent foodDetails = new Intent (MenuItem.this, MenuItemInfo.class);
                        foodDetails.putExtra("FoodId",adapter.getRef(position).getKey()); //sends to the new activity
                        startActivity(foodDetails);
                    }
                });
            }
        };
        //Sets the adapter
        recyclerView.setAdapter(adapter);
    }
}
