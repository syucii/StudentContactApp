package com.example.studentcontactapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.R
import com.example.studentcontactapp.database.entity.StudentEntity

class StudentAdapter(
    private var students: MutableList<StudentEntity>,
    private val onEdit: (StudentEntity) -> Unit,
    private val onDelete: (StudentEntity) -> Unit,
    private val onClick: (StudentEntity) -> Unit
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAvatar: TextView = view.findViewById(R.id.tvAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvNim: TextView = view.findViewById(R.id.tvNim)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]

        // inisial nama sebagai avatar
        val initials = student.name
            .split(" ")
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
        holder.tvAvatar.text = initials

        holder.tvName.text = student.name
        holder.tvNim.text = student.nim

        holder.btnEdit.setOnClickListener { onEdit(student) }
        holder.btnDelete.setOnClickListener { onDelete(student) }
        holder.itemView.setOnClickListener { onClick(student) }
    }

    override fun getItemCount(): Int = students.size

    fun updateData(newStudents: List<StudentEntity>) {
        students.clear()
        students.addAll(newStudents)
        notifyDataSetChanged()
    }

    fun getStudentAt(position: Int): StudentEntity = students[position]
}
