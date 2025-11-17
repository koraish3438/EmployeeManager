package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LastUpdateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(update: LastUpdate)

    @Update
    suspend fun update(update: LastUpdate)

    @Query("SELECT * FROM last_updates WHERE employeeId = :empId")
    fun getUpdatesByEmployee(empId: Int): LiveData<List<LastUpdate>>
}
