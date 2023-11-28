package com.example.da1_e_max.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.da1_e_max.model.Products;


@Database(entities = {Products.class}, version = 1)
public abstract class ProductDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "products.db";

    private static ProductDatabase instance;

    public static synchronized ProductDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ProductDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ProductDAO productDAO();
}
