package com.example.ept.data.repository

import com.example.ept.data.dao.TaskDao
import com.example.ept.data.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    val allTasks: Flow<List<Task>> = dao.getAllTasks()
    val pendingTaskCount: Flow<Int> = dao.getPendingTaskCount()

    suspend fun insert(task: Task): Long = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)

    fun getTasksForEmployee(employeeId: Long): Flow<List<Task>> =
        dao.getTasksForEmployee(employeeId)

    fun getByStatus(status: String): Flow<List<Task>> = dao.getByStatus(status)

    suspend fun getCompletedCountForEmployee(employeeId: Long): Int =
        dao.getCompletedCountForEmployee(employeeId)

    suspend fun getTotalCountForEmployee(employeeId: Long): Int =
        dao.getTotalCountForEmployee(employeeId)
}
