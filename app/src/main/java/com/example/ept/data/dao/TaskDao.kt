package com.example.ept.data.dao

import androidx.room.*
import com.example.ept.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE employeeId = :employeeId ORDER BY deadline ASC")
    fun getTasksForEmployee(employeeId: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY deadline ASC")
    fun getByStatus(status: String): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM tasks WHERE status = 'Pending' OR status = 'In Progress'")
    fun getPendingTaskCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE employeeId = :employeeId AND status = 'Completed'")
    suspend fun getCompletedCountForEmployee(employeeId: Long): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE employeeId = :employeeId")
    suspend fun getTotalCountForEmployee(employeeId: Long): Int
}
