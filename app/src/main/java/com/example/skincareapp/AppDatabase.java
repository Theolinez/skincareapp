package com.example.skincareapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ProductEntity.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class, "skincare-db"
            ).build();
        }
        return instance;
    }
}
