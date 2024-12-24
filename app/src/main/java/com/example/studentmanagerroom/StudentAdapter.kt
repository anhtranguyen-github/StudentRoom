package com.example.studentmanagerroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val onStudentClicked:  (Student) -> Unit,
    private val onSelectionChanged: (List<Student>) -> Unit) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var studentList: List<Student> = emptyList()
    private val selectedStudents: MutableList<Student> = mutableListOf()

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mssv: TextView = itemView.findViewById(R.id.mssvTextView)
        val fullName: TextView = itemView.findViewById(R.id.fullNameTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.studentCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_list_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.mssv.text = student.mssv
        holder.fullName.text = student.fullName

        holder.itemView.setOnClickListener {
            onStudentClicked(student) // Pass the clicked student
        }

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedStudents.contains(student)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedStudents.add(student) else selectedStudents.remove(student)
            onSelectionChanged(selectedStudents)
        }
    }

    override fun getItemCount(): Int = studentList.size

    fun submitList(students: List<Student>) {
        studentList = students
        notifyDataSetChanged()
    }

    fun getSelectedStudents(): List<Student> = selectedStudents

}
