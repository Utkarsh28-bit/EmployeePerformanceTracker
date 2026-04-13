package com.example.ept.data.dao

import androidx.room.*
import com.example.ept.data.entity.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: Attendance): Long

    @Update
    suspend fun update(attendance: Attendance)

    @Delete
    suspend fun delete(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE employeeId = :employeeId ORDER BY date DESC")
    fun getAttendanceForEmployee(employeeId: Long): Flow<List<Attendance>>

    @Query("SELECT COUNT(*) FROM attendance WHERE employeeId = :employeeId AND status = 'Present'")
    suspend fun getPresentCountForEmployee(employeeId: Long): Int

    @Query("SELECT COUNT(*) FROM attendance WHERE employeeId = :employeeId")
    suspend fun getTotalDaysForEmployee(employeeId: Long): Int
}
