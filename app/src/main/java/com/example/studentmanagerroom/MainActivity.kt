package com.example.studentmanagerroom

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var adapter: StudentAdapter
    private lateinit var database: StudentDatabase
    private lateinit var deleteButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        database = StudentDatabase.getDatabase(this)
        adapter = StudentAdapter (
            onStudentClicked = { student ->
                val intent = Intent(this, AddStudentActivity::class.java)
                intent.putExtra("mssv", student.mssv) // Pass mssv as identifier
                intent.putExtra("fullName", student.fullName)
                intent.putExtra("dob", student.dob)
                intent.putExtra("email", student.email)
                startActivity(intent)
            },
            onSelectionChanged = { selectedStudents ->
                // Handle multi-selection
                deleteButton.visibility =
                    if (selectedStudents.isNotEmpty()) View.VISIBLE else View.GONE
            }
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val addStudentButton = findViewById<Button>(R.id.addStudentButton)
        deleteButton = findViewById<Button>(R.id.deleteButton)

        loadStudents()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchBar.addTextChangedListener { text ->
            searchStudents(text.toString())
        }

        addStudentButton.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }

        deleteButton.setOnClickListener {
            deleteSelectedStudents(adapter.getSelectedStudents())
        }
    }
    override fun onResume() {
        super.onResume()
        loadStudents() // Refresh the student list
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            val students = database.studentDao().getAllStudents()
            adapter.submitList(students)
        }
    }

    private fun deleteSelectedStudents(students: List<Student>) {
        lifecycleScope.launch {
            database.studentDao().deleteStudents(students)
            loadStudents() // Refresh the list
        }
    }


    private fun searchStudents(query: String) {
        lifecycleScope.launch {
            val students = database.studentDao().searchStudents("%$query%")
            adapter.submitList(students)
        }
    }
}