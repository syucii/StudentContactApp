package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.adapter.StudentAdapter
import com.example.studentcontactapp.database.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var adapter: StudentAdapter
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDB(requireContext())

        val etSearch = view.findViewById<TextInputEditText>(R.id.etSearch)
        val rvResult = view.findViewById<RecyclerView>(R.id.rvResult)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)

        rvResult.layoutManager = LinearLayoutManager(requireContext())

        adapter = StudentAdapter(
            students = mutableListOf(),
            onEdit = { student ->
                val intent = Intent(requireContext(), FormActivity::class.java)
                intent.putExtra("student_id", student.id)
                startActivity(intent)
            },
            onDelete = { student ->
                showDeleteConfirmation(student.id, student.name) {
                    performSearch(etSearch.text.toString(), tvEmpty)
                }
            },
            onClick = { student ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("student_id", student.id)
                startActivity(intent)
            }
        )
        rvResult.adapter = adapter

        // Penggunaan lifecycleScope.launch saat pecarian RealTIme
        etSearch.addTextChangedListener { text ->
            performSearch(text.toString(), tvEmpty)
        }

        // Tampilkan semua mahasiswa saat fragment pertama kali dibuka
        performSearch("", tvEmpty)
    }

    override fun onResume() {
        super.onResume()
        val etSearch = view?.findViewById<TextInputEditText>(R.id.etSearch)
        val tvEmpty = view?.findViewById<TextView>(R.id.tvEmpty)
        if (etSearch != null && tvEmpty != null) {
            performSearch(etSearch.text.toString(), tvEmpty)
        }
    }

    private fun performSearch(keyword: String, tvEmpty: TextView) {
        lifecycleScope.launch {
            val results = db.studentDao().searchStudents(keyword)
            adapter.updateData(results)
            tvEmpty.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showDeleteConfirmation(studentId: Int, studentName: String, onDeleted: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data?")
            .setMessage("Hapus \"$studentName\"? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    db.studentDao().deleteById(studentId)
                    onDeleted()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
