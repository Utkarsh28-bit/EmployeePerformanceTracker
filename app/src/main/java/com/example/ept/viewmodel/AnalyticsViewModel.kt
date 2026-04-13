package com.example.ept.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ept.EPTApplication
import com.example.ept.data.dao.EmployeeAvgRating
import com.example.ept.data.entity.Employee
import com.example.ept.data.entity.Performance
import kotlinx.coroutines.flow.*

data class EmployeeWithRating(
    val employee: Employee,
    val avgRating: Float,
    val reviewCount: Int
)

data class DepartmentStats(
    val department: String,
    val avgRating: Float,
    val employeeCount: Int
)

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as EPTApplication
    private val employeeRepo = app.employeeRepository
    private val performanceRepo = app.performanceRepository

    val allEmployees: StateFlow<List<Employee>> =
        employeeRepo.allEmployees.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val averageRatings: StateFlow<List<EmployeeAvgRating>> =
        performanceRepo.averageRatings.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val topPerformers: StateFlow<List<EmployeeAvgRating>> =
        performanceRepo.topPerformers.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val lowPerformers: StateFlow<List<EmployeeAvgRating>> =
        performanceRepo.lowPerformers.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Employees with their average ratings combined
    val employeesWithRatings: StateFlow<List<EmployeeWithRating>> = combine(
        allEmployees, averageRatings
    ) { employees, ratings ->
        employees.mapNotNull { emp ->
            val rating = ratings.firstOrNull { it.employeeId == emp.id }
            if (rating != null) {
                EmployeeWithRating(
                    employee = emp,
                    avgRating = rating.avgRating,
                    reviewCount = 0 // simplified for bar chart
                )
            } else null
        }.sortedByDescending { it.avgRating }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Department performance statistics
    val departmentStats: StateFlow<List<DepartmentStats>> = combine(
        allEmployees, averageRatings
    ) { employees, ratings ->
        employees.groupBy { it.department }.map { (dept, emps) ->
            val deptRatings = ratings.filter { r -> emps.any { e -> e.id == r.employeeId } }
            val avg = if (deptRatings.isEmpty()) 0f else deptRatings.map { it.avgRating }.average().toFloat()
            DepartmentStats(dept, avg, emps.size)
        }.sortedByDescending { it.avgRating }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EPTApplication
                return AnalyticsViewModel(app) as T
            }
        }
    }
}
