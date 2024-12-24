package com.example.studentmanagerroom

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddStudentActivity : ComponentActivity() {

    private lateinit var database: StudentDatabase
    private lateinit var mssvEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addButton: Button
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        // Initialize views
        mssvEditText = findViewById(R.id.mssvEditText)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        dobEditText = findViewById(R.id.dobEditText)
        emailEditText = findViewById(R.id.emailEditText)
        addButton = findViewById(R.id.addButton)

        database = StudentDatabase.getDatabase(this)

        // Check if this is edit mode
        val mssv = intent.getStringExtra("mssv")
        if (mssv != null) {
            isEditMode = true
            loadStudentDetails(mssv)
        }

        // Set up button click listener
        addButton.setOnClickListener {
            if (isEditMode) {
                updateStudent()
            } else {
                addStudent()
            }
        }
    }

    private fun loadStudentDetails(mssv: String) {
        lifecycleScope.launch {
            val student = database.studentDao().getStudentById(mssv)
            if (student != null) {
                mssvEditText.setText(student.mssv)
                fullNameEditText.setText(student.fullName)
                dobEditText.setText(student.dob)
                emailEditText.setText(student.email)
                mssvEditText.isEnabled = false // MSSV (primary key) cannot be edited
                addButton.text = "Update Student"
            } else {
                Toast.makeText(this@AddStudentActivity, "Student not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun addStudent() {
        val mssv = mssvEditText.text.toString()
        val fullName = fullNameEditText.text.toString()
        val dob = dobEditText.text.toString()
        val email = emailEditText.text.toString()

        if (mssv.isBlank() || fullName.isBlank()) {
            Toast.makeText(this, "MSSV and Full Name are required", Toast.LENGTH_SHORT).show()
            return
        }

        val newStudent = Student(mssv, fullName, dob, email)
        lifecycleScope.launch {
            database.studentDao().insertStudent(newStudent)
            Toast.makeText(this@AddStudentActivity, "Student added successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateStudent() {
        val mssv = mssvEditText.text.toString()
        val fullName = fullNameEditText.text.toString()
        val dob = dobEditText.text.toString()
        val email = emailEditText.text.toString()

        val updatedStudent = Student(mssv, fullName, dob, email)
        lifecycleScope.launch {
            database.studentDao().updateStudent(updatedStudent)
            Toast.makeText(this@AddStudentActivity, "Student updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
