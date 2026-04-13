package com.example.ept.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ept.EPTApplication
import com.example.ept.data.dao.EmployeeAvgRating
import com.example.ept.data.entity.Performance
import com.example.ept.data.repository.PerformanceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PerformanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PerformanceRepository =
        (application as EPTApplication).performanceRepository

    val topPerformers: StateFlow<List<EmployeeAvgRating>> =
        repository.topPerformers.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val lowPerformers: StateFlow<List<EmployeeAvgRating>> =
        repository.lowPerformers.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val averageRatings: StateFlow<List<EmployeeAvgRating>> =
        repository.averageRatings.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalReviewCount: StateFlow<Int> =
        repository.totalReviewCount.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun getPerformanceForEmployee(employeeId: Long): Flow<List<Performance>> =
        repository.getPerformanceForEmployee(employeeId)

    fun getReviewCountForEmployee(employeeId: Long): Flow<Int> =
        repository.getReviewCountForEmployee(employeeId)

    fun insert(performance: Performance) = viewModelScope.launch { repository.insert(performance) }
    fun update(performance: Performance) = viewModelScope.launch { repository.update(performance) }
    fun delete(performance: Performance) = viewModelScope.launch { repository.delete(performance) }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                return PerformanceViewModel(app as Application) as T
            }
        }
    }
}
