package com.example.ept

import android.app.Application
import com.example.ept.data.database.AppDatabase
import com.example.ept.data.repository.*

class EPTApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val employeeRepository by lazy { EmployeeRepository(database.employeeDao()) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }
    val performanceRepository by lazy { PerformanceRepository(database.performanceDao()) }
    val attendanceRepository by lazy { AttendanceRepository(database.attendanceDao()) }
}
