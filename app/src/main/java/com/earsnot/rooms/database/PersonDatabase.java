package com.earsnot.rooms.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.earsnot.rooms.model.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class PersonDatabase  extends RoomDatabase {
    // DAO getter
    public abstract PersonDAO personDAO();

    //db instance for singleton pattern
    private static PersonDatabase instance;

    public static PersonDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (PersonDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            PersonDatabase.class, "person_db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // BAD PRACTICE ONLY FOR DEMO - BLOCKS UI THREAD
                            .build();
                }
            }
        }
        return instance;
    }
}

