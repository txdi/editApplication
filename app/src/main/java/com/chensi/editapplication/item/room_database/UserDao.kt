package com.chensi.editapplication.item.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllData():List<UserBean>
    @Insert
    fun addUser(user:UserBean)

}