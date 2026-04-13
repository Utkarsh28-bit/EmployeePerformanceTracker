package com.example.ept.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ept.EPTApplication
import com.example.ept.data.entity.Employee
import com.example.ept.data.repository.EmployeeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmployeeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EmployeeRepository =
        (application as EPTApplication).employeeRepository

    val allEmployees: StateFlow<List<Employee>> =
        repository.allEmployees.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val departments: StateFlow<List<String>> =
        repository.departments.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val employeeCount: StateFlow<Int> =
        repository.employeeCount.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedDepartment = MutableStateFlow("All")
    val selectedDepartment: StateFlow<String> = _selectedDepartment

    val filteredEmployees: StateFlow<List<Employee>> = combine(
        allEmployees, _searchQuery, _selectedDepartment
    ) { employees, query, dept ->
        employees.filter { emp ->
            val matchesQuery = query.isEmpty() || emp.name.contains(query, ignoreCase = true)
            val matchesDept = dept == "All" || emp.department == dept
            matchesQuery && matchesDept
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setDepartmentFilter(dept: String) { _selectedDepartment.value = dept }

    fun save(employee: Employee) = viewModelScope.launch { repository.insert(employee) }
    fun update(employee: Employee) = viewModelScope.launch { repository.update(employee) }
    fun delete(employee: Employee) = viewModelScope.launch { repository.delete(employee) }

    suspend fun getById(id: Long): Employee? = repository.getById(id)

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                return EmployeeViewModel(app as Application) as T
            }
        }
    }
}
