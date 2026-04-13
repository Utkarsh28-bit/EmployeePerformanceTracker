package com.example.ept.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ept.EPTApplication
import com.example.ept.data.entity.Task
import com.example.ept.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository =
        (application as EPTApplication).taskRepository

    val allTasks: StateFlow<List<Task>> =
        repository.allTasks.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val pendingTaskCount: StateFlow<Int> =
        repository.pendingTaskCount.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun getTasksForEmployee(employeeId: Long): Flow<List<Task>> =
        repository.getTasksForEmployee(employeeId)

    fun insert(task: Task) = viewModelScope.launch { repository.insert(task) }
    fun update(task: Task) = viewModelScope.launch { repository.update(task) }
    fun delete(task: Task) = viewModelScope.launch { repository.delete(task) }

    fun updateStatus(task: Task, newStatus: String) =
        viewModelScope.launch { repository.update(task.copy(status = newStatus)) }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                return TaskViewModel(app as Application) as T
            }
        }
    }
}
