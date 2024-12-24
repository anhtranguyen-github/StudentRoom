package com.example.studentmanagerroom

import androidx.room.*

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM student")
    suspend fun getAllStudents(): List<Student>

    @Query("SELECT * FROM student WHERE mssv LIKE :query OR full_name LIKE :query")
    suspend fun searchStudents(query: String): List<Student>

    @Query("SELECT * FROM student WHERE mssv = :id")
    suspend fun getStudentById(id: String): Student?

    @Delete
    suspend fun deleteStudents(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)
}
