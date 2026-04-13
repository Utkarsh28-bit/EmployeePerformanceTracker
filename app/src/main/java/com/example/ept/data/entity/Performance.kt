package com.example.ept.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "performances",
    foreignKeys = [
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("employeeId")]
)
data class Performance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val employeeId: Long,
    val date: String,
    val qualityScore: Float,       // 1-5
    val timelinessScore: Float,    // 1-5
    val attendanceScore: Float,    // 1-5
    val communicationScore: Float, // 1-5
    val innovationScore: Float,    // 1-5
    val overallRating: Float,      // 1-5 (star rating)
    val remarks: String = ""
)
