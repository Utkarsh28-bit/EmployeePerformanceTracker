package com.example.ept.data.dao

import androidx.room.*
import com.example.ept.data.entity.Employee
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee): Long

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT * FROM employees ORDER BY name ASC")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getById(id: Long): Employee?

    @Query("SELECT * FROM employees WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchByName(query: String): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE department = :department ORDER BY name ASC")
    fun getByDepartment(department: String): Flow<List<Employee>>

    @Query("SELECT DISTINCT department FROM employees ORDER BY department ASC")
    fun getAllDepartments(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM employees")
    fun getEmployeeCount(): Flow<Int>
}
