package com.example.varietyislandproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.varietyislandproject.Common.Common;
import com.example.varietyislandproject.Database.DatabaseVarietyIsland;
import com.example.varietyislandproject.Model.Food;
import com.example.varietyislandproject.Model.Order;
import com.example.varietyislandproject.Model.Rating;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class MenuItemInfo extends AppCompatActivity implements RatingDialogListener {

    TextView foodName, foodPrice, foodDetails;
    ImageView foodImage;
    String foodId="";

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    FloatingActionButton btnMenu,btnReview;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratings;
    Food currentFood;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");
        ratings = database.getReference("Rating");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_Button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        btnMenu = (FloatingActionButton)findViewById(R.id.btnmenu);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        btnReview = (FloatingActionButton)findViewById(R.id.btnReview);

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(MenuItemInfo.this, MainMenu.class);
                startActivity(menuIntent);
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatabaseVarietyIsland(getBaseContext()).addToCart(new Order(
                        Common.currentUser.getPhone(),
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount(),
                        currentFood.getImage()
                ));
                Toast.makeText(MenuItemInfo.this,"Added To Your Cart",Toast.LENGTH_SHORT).show();
            }
        });

        foodDetails = (TextView)findViewById(R.id.food_Details);
        foodName = (TextView)findViewById(R.id.food_Name);
        foodPrice = (TextView)findViewById(R.id.food_Price);
        foodImage = (ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.Collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //Gets the foods id from intent
        if(getIntent()!= null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
           getFoodDetails(foodId);
           getRatingFood(foodId);
        }
    }

    private void getRatingFood(String foodId)
    {
        Query foodRating = ratings.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0,sum = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0)
                {
                    float average = sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFoodDetails(final String foodId)
    {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(foodImage);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                foodPrice.setText(currentFood.getPrice());
                foodName.setText(currentFood.getName());
                foodDetails.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void   showRatingDialog()
    {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Bad","Not Great","Average","Great","Awesome"))
                .setDefaultRating(1)
                .setTitle("Rate This Food Item")
                .setDescription("Please Select Your Rating")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please enter your feedback here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(MenuItemInfo.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int v, String s) {
        final Rating rating = new Rating(Common.currentUser.getPhone(),
                foodId,
                String.valueOf(v),
                s);
        ratings.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {
                ratings.child(Common.currentUser.getPhone()).removeValue();
                ratings.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else
                    {
                        ratings.child(Common.currentUser.getPhone()).setValue(rating);
                    }
                Toast.makeText(MenuItemInfo.this,"Variety Island thanks you for your feedback,", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
