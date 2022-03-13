package com.ismt.ismtproject.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepo {
    private final JournalDatabase db;
    private final UserDao userDao;
    public UserRepo (Context context){
        db  = JournalDatabase.getInstance(context);
        userDao = db.userDao();
    }


//    function to get all the users from database
    LiveData<List<User>> getLiveUsers() {
        return userDao.getUsersLiveData();
    }

//    function to insert new users in database
    public void createUser(User user) {
        userDao.insertUser(user);
    }

//  function to delete User from database
        private void deleteUser(User user) {
        userDao.delete(user);
        }
}
