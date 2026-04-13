package com.example.ept.data.repository

import com.example.ept.data.dao.EmployeeDao
import com.example.ept.data.entity.Employee
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val dao: EmployeeDao) {

    val allEmployees: Flow<List<Employee>> = dao.getAllEmployees()
    val departments: Flow<List<String>> = dao.getAllDepartments()
    val employeeCount: Flow<Int> = dao.getEmployeeCount()

    suspend fun insert(employee: Employee): Long = dao.insert(employee)
    suspend fun update(employee: Employee) = dao.update(employee)
    suspend fun delete(employee: Employee) = dao.delete(employee)
    suspend fun getById(id: Long): Employee? = dao.getById(id)

    fun searchByName(query: String): Flow<List<Employee>> = dao.searchByName(query)
    fun getByDepartment(dept: String): Flow<List<Employee>> = dao.getByDepartment(dept)
}
