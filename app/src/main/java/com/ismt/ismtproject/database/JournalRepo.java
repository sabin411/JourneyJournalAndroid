package com.ismt.ismtproject.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class JournalRepo {

    private final JournalDatabase db;
    private final JournalDao journalDao;

    public JournalRepo (Context context){
        this.db  = JournalDatabase.getInstance(context);
        journalDao = db.journalDao();
    }

//    function to get all the journal from database\
    long insertJournal(Journal journal){
        return journalDao.insertJournal(journal);
    }

    LiveData<List<Journal>> retrieveJournals(){
        return journalDao.retrieveJournals();
    }

}
