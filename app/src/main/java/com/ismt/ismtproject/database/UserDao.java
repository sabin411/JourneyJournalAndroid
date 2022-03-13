package com.ismt.ismtproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

//    define instances here
//    generally in interfaces we define variables and declare methods.

    double pi = 3.14d;

    @Query("SELECT * FROM user")
    List<User> getAll();

//    @Query("")
//    List<User> loadAllByIds(int[] id);

//    @Query("")
//    User findByName(String first, String last);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getUsersLiveData();

    @Insert
    void insertUser(User users);

    @Delete
    void delete(User user);
}
