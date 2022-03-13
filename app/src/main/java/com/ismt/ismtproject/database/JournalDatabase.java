package com.ismt.ismtproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Journal.class}, exportSchema = false, version = 1)
public abstract class JournalDatabase extends RoomDatabase {
    private static final String DB_NAME = "journey_journal_db";
    private static JournalDatabase instance;

    public static synchronized JournalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), JournalDatabase.class, DB_NAME).fallbackToDestructiveMigration().
                    allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract UserDao userDao();
    public abstract JournalDao journalDao();
}
