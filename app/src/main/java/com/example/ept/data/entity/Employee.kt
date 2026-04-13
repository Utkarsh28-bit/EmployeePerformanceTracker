package com.example.ept.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val role: String,
    val department: String,
    val joiningDate: String,
    val email: String,
    val contact: String,
    val profileImageUri: String = ""
)
