package com.example.ept.data.repository

import com.example.ept.data.dao.AttendanceDao
import com.example.ept.data.entity.Attendance
import kotlinx.coroutines.flow.Flow

class AttendanceRepository(private val dao: AttendanceDao) {

    suspend fun insert(attendance: Attendance): Long = dao.insert(attendance)
    suspend fun update(attendance: Attendance) = dao.update(attendance)
    suspend fun delete(attendance: Attendance) = dao.delete(attendance)

    fun getAttendanceForEmployee(employeeId: Long): Flow<List<Attendance>> =
        dao.getAttendanceForEmployee(employeeId)

    suspend fun getPresentCountForEmployee(employeeId: Long): Int =
        dao.getPresentCountForEmployee(employeeId)

    suspend fun getTotalDaysForEmployee(employeeId: Long): Int =
        dao.getTotalDaysForEmployee(employeeId)
}
