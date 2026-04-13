package com.example.ept.data.dao

import androidx.room.*
import com.example.ept.data.entity.Performance
import kotlinx.coroutines.flow.Flow

data class EmployeeAvgRating(
    val employeeId: Long,
    val avgRating: Float
)

@Dao
interface PerformanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(performance: Performance): Long

    @Update
    suspend fun update(performance: Performance)

    @Delete
    suspend fun delete(performance: Performance)

    @Query("SELECT * FROM performances WHERE employeeId = :employeeId ORDER BY date DESC")
    fun getPerformanceForEmployee(employeeId: Long): Flow<List<Performance>>

    @Query("SELECT * FROM performances ORDER BY date DESC")
    fun getAllPerformances(): Flow<List<Performance>>

    @Query("""
        SELECT employeeId, AVG(overallRating) as avgRating 
        FROM performances 
        GROUP BY employeeId 
        ORDER BY avgRating DESC
    """)
    fun getAverageRatings(): Flow<List<EmployeeAvgRating>>

    @Query("""
        SELECT employeeId, AVG(overallRating) as avgRating 
        FROM performances 
        GROUP BY employeeId 
        ORDER BY avgRating DESC 
        LIMIT 3
    """)
    fun getTopPerformers(): Flow<List<EmployeeAvgRating>>

    @Query("""
        SELECT employeeId, AVG(overallRating) as avgRating 
        FROM performances 
        GROUP BY employeeId 
        ORDER BY avgRating ASC 
        LIMIT 3
    """)
    fun getLowPerformers(): Flow<List<EmployeeAvgRating>>

    @Query("SELECT AVG(overallRating) FROM performances WHERE employeeId = :employeeId")
    suspend fun getAverageRatingForEmployee(employeeId: Long): Float?

    @Query("SELECT COUNT(*) FROM performances WHERE employeeId = :employeeId")
    fun getReviewCountForEmployee(employeeId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM performances")
    fun getTotalReviewCount(): Flow<Int>
}
