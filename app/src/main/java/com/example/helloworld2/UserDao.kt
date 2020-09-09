package com.example.helloworld2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user WHERE user.uid = :userid")
    fun getUserContacts(userid: Long): LiveData<UserContact>
}

@Dao
interface ContactInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contactInfo: ContactInfo)
}