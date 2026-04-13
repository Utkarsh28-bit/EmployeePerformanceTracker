package com.example.ept.data.repository

import com.example.ept.data.dao.EmployeeAvgRating
import com.example.ept.data.dao.PerformanceDao
import com.example.ept.data.entity.Performance
import kotlinx.coroutines.flow.Flow

class PerformanceRepository(private val dao: PerformanceDao) {

    val allPerformances: Flow<List<Performance>> = dao.getAllPerformances()
    val averageRatings: Flow<List<EmployeeAvgRating>> = dao.getAverageRatings()
    val topPerformers: Flow<List<EmployeeAvgRating>> = dao.getTopPerformers()
    val lowPerformers: Flow<List<EmployeeAvgRating>> = dao.getLowPerformers()
    val totalReviewCount: Flow<Int> = dao.getTotalReviewCount()

    suspend fun insert(performance: Performance): Long = dao.insert(performance)
    suspend fun update(performance: Performance) = dao.update(performance)
    suspend fun delete(performance: Performance) = dao.delete(performance)

    fun getPerformanceForEmployee(employeeId: Long): Flow<List<Performance>> =
        dao.getPerformanceForEmployee(employeeId)

    fun getReviewCountForEmployee(employeeId: Long): Flow<Int> =
        dao.getReviewCountForEmployee(employeeId)

    suspend fun getAverageRatingForEmployee(employeeId: Long): Float? =
        dao.getAverageRatingForEmployee(employeeId)
}
