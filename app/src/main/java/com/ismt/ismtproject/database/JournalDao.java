package com.ismt.ismtproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JournalDao {
    @Insert
    long insertJournal(Journal journal);

    @Query("SELECT * FROM journal")
    LiveData<List<Journal>> retrieveJournals();

    @Update
    void updateJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);
}
