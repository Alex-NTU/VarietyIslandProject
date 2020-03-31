package com.example.varietyislandproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varietyislandproject.Common.Common;
import com.example.varietyislandproject.Database.DatabaseVarietyIsland;
import com.example.varietyislandproject.Interface.DeleteFunction;
import com.example.varietyislandproject.Interface.RecyclerItemTouchHelper;
import com.example.varietyislandproject.Model.Order;
import com.example.varietyislandproject.Model.Request;
import com.example.varietyislandproject.ViewHolder.AdapterCart;
import com.example.varietyislandproject.ViewHolder.CartViewHolder;
import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class ShopCart extends AppCompatActivity implements RecyclerItemTouchHelper {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    Place Orderaddress;
    PlacesClient placesClient;
    AutocompleteSupportFragment autocompleteSupportFragment;
    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS);

    public TextView txtTotalPrice;
    FButton btnPlace;
    List<Order> carts = new ArrayList<>();
    AdapterCart adapter;
    RelativeLayout baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        baseLayout = (RelativeLayout)findViewById(R.id.baseLayout);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (FButton)findViewById(R.id.btnOrder);

        ItemTouchHelper.SimpleCallback simpleCallback = new DeleteFunction(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create request intent
                OrderFood();

            }

            private void OrderFood()
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder((ShopCart.this));
                alertDialog.setTitle("One More Thing");
                alertDialog.setMessage("Enter Your Delivery Address");

                LayoutInflater inflater = getLayoutInflater();
                View order_process = inflater.inflate(R.layout.order_process,null); //may be issue

                final MaterialEditText editComment = (MaterialEditText)order_process.findViewById(R.id.editcomment);
                alertDialog.setView(order_process);
                alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int I) {
                        Request request = new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getName(),
                                Orderaddress.getAddress().toString(),
                                txtTotalPrice.getText().toString(),
                                "0",
                                editComment.getText().toString(),
                                String.format("%s,%s",Orderaddress.getLatLng().latitude,Orderaddress.getLatLng().longitude),
                                carts
                        );

                        double lat1 = Orderaddress.getLatLng().latitude;
                        double lon1 = Orderaddress.getLatLng().longitude;
                        double lat2 = 53.202492;
                        double lon2 = -1.213127;
                        double theta = lon1 - lon2;
                        double dist = Math.sin(deg2rad(lat1))
                                * Math.sin(deg2rad(lat2))
                                + Math.cos(deg2rad(lat1))
                                * Math.cos(deg2rad(lat2))
                                * Math.cos(deg2rad(theta));
                        dist = Math.acos(dist);
                        dist = rad2deg(dist);
                        dist = dist * 60 * 1.1515;
                        Log.d("ADebugTag", "Value: " + Double.toString(dist));

                        //Submits order to firebase database
                        requests.child(String.valueOf(System.currentTimeMillis()))
                                .setValue(request);
                        //Deletes content of cart
                        new DatabaseVarietyIsland(getBaseContext()).cleanCart(Common.currentUser.getPhone());

                        if (dist > 3.5 && dist < 4.9)
                        {
                            Toast.makeText(ShopCart.this,"Thank You Your Order Has Been Placed" +  String.format("%.2f", dist),Toast.LENGTH_LONG).show();
                            Toast.makeText(ShopCart.this, "Delivery charge of £3 has been added", Toast.LENGTH_LONG).show();
                        }
                        if (dist < 3.5)
                        {
                            Toast.makeText(ShopCart.this,"Thank You Your Order Has Been Placed" +  String.format("%.2f", dist),Toast.LENGTH_LONG).show();
                            Toast.makeText(ShopCart.this, "Delivery charge of £1 has been added", Toast.LENGTH_LONG).show();
                        }
                        if (dist > 5 && dist < 9.9)
                        {
                            Toast.makeText(ShopCart.this,"Thank You Your Order Has Been Placed" +  String.format("%.2f", dist),Toast.LENGTH_LONG).show();
                            Toast.makeText(ShopCart.this, "Delivery charge of £5 has been added", Toast.LENGTH_LONG).show();
                        }
                        if (dist >  10)
                        {
                            Toast.makeText(ShopCart.this, "We are sorry we cannot deliver beyond 10 miles", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            //Remove Fragment
                            getSupportFragmentManager().beginTransaction()
                                    .remove(getSupportFragmentManager().findFragmentById(R.id.place_auto))
                                    .commit();
                        }
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Remove Fragment
                        getSupportFragmentManager().beginTransaction()
                                .remove(getSupportFragmentManager().findFragmentById(R.id.place_auto))
                                .commit();
                    }
                });
                alertDialog.show();
                initPlaces();
                setupPlaceAutocomplete();
            }
        });
        CalculatePrice();
    }

        public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

        public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public void CalculatePrice()
    {
        carts = new DatabaseVarietyIsland(this).getCarts(Common.currentUser.getPhone());
        adapter = new AdapterCart(carts,this);
        recyclerView.setAdapter(adapter);

        //this will calculate the total price of the users order
         float total = 0;
        for (Order order:carts)
            total+=(Float.valueOf(order.getPrice()))*(Float.valueOf(order.getQuantity()));

        Locale locale = new Locale("en","GB");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(format.format(total));

    }

    @Override
    public  void onSwiped(RecyclerView.ViewHolder viewHolder, int direction,int position)
    {
        if (viewHolder instanceof CartViewHolder)
        {
            String name = ((AdapterCart)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            final Order deleteItem = ((AdapterCart)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            adapter.removeItems(deleteIndex);
            new DatabaseVarietyIsland(getBaseContext()).removeFromCart(deleteItem.getProductId(),Common.currentUser.getPhone());

            float total = 0;
            List<Order> orders = new DatabaseVarietyIsland(getBaseContext()).getCarts(Common.currentUser.getPhone()) ;
            for (Order item:orders)
                total+=(Float.valueOf(item.getPrice()))*(Float.valueOf(item.getQuantity()));

            Locale locale = new Locale("en","GB");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            txtTotalPrice.setText(format.format(total));

            Snackbar snackbar = Snackbar.make(baseLayout,name+"Item Removed From Cart", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restore(deleteItem,deleteIndex);
                    new DatabaseVarietyIsland(getBaseContext()).addToCart(deleteItem);

                    float total = 0;
                    List<Order> orders = new DatabaseVarietyIsland(getBaseContext()).getCarts(Common.currentUser.getPhone()) ;
                    for (Order item:orders)
                        total+=(Float.valueOf(item.getPrice()))*(Float.valueOf(item.getQuantity()));

                    Locale locale = new Locale("en","GB");
                    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                    txtTotalPrice.setText(format.format(total));
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void setupPlaceAutocomplete() {
        autocompleteSupportFragment =(AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.place_auto);
        //hide search icon before fragment
        autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        //set hint for autocomplete EditText
        ((EditText)autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setHint("Enter your Address");
        //set Text size
        ((EditText)autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(14);

        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Orderaddress = place;
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    private void initPlaces() {
    //copy your api to string.xml
        Places.initialize(this,getString(R.string.places_api_key));
        placesClient = Places.createClient(ShopCart.this);
    }

}
