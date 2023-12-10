package com.chensi.editapplication.item.room_database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserBean::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}