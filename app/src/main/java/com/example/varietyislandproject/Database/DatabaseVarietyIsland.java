package com.example.varietyislandproject.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.varietyislandproject.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


import java.util.ArrayList;
import java.util.List;

public class DatabaseVarietyIsland extends SQLiteAssetHelper {
    private static final String DB_NAME = "varietyIsland.db";
    private static final int DB_VER = 1;
    public DatabaseVarietyIsland(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public boolean checkFood(String foodId,String userPhone)
    {
        boolean check = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= null;
        String SQL = String.format("SELECT * From OrderDetail WHERE UserPhone='%s' AND ProductID='%s'",userPhone,foodId);
        cursor = db.rawQuery(SQL,null);
        if(cursor.getCount()>0)
            check =true;
        else
            check= false;
        cursor.close();
        return check;
    }

    public List <Order> getCarts(String userPhone)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect= {"UserPhone","ProductName","ProductId","Quantity","Price","Discount","Image"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List <Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do
                {
                    result.add(new Order(
                            c.getString(c.getColumnIndex("UserPhone")),
                            c.getString(c.getColumnIndex("ProductId")),
                            (c.getString(c.getColumnIndex("ProductName"))),
                            (c.getString(c.getColumnIndex("Quantity"))),
                            (c.getString(c.getColumnIndex("Price"))),
                            (c.getString(c.getColumnIndex("Discount"))),
                            (c.getString(c.getColumnIndex("Image")))
                            ));
                } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserPhone,ProductId,ProductName,Quantity,Price,Discount,Image) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        db.execSQL(query);
    }

    public void cleanCart(String userPhone)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s'",userPhone);
        db.execSQL(query);
    }

    public void removeFromCart(String productId, String phone)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s' and ProductId='%s'",phone,productId);
            db.execSQL(query);
    }

    public void updateCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity = '%s' WHERE UserPhone = '%s' AND ProductId='%s'",order.getQuantity(),order.getUserPhone(),order.getProductId());
        db.execSQL(query);
    }
}
