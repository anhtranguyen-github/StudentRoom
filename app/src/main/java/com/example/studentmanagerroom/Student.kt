package com.example.studentmanagerroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey val mssv: String,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "date_of_birth") val dob: String?,
    @ColumnInfo(name = "email") val email: String?
)
