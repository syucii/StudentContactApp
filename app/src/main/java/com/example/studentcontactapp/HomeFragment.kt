package com.example.studentcontactapp

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.adapter.StudentAdapter
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity
import com.example.studentcontactapp.utils.PrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var adapter: StudentAdapter
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDB(requireContext())

        // Tampilkan sapaan berdasarkan nama pengguna dari SharedPreferences
        val pref = PrefManager(requireContext())
        view.findViewById<TextView>(R.id.tvWelcome).text =
            "Selamat datang, ${pref.getUsername()}!"

        // Inisialisasi RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvStudents)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = StudentAdapter(
            students = mutableListOf(),
            onEdit = { student ->
                val intent = Intent(requireContext(), FormActivity::class.java)
                intent.putExtra("student_id", student.id)
                startActivity(intent)
            },
            onDelete = { student ->
                showDeleteConfirmation(student.id, student.name)
            },
            onClick = { student ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("student_id", student.id)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        // Pasang Swipe to Delete
        setupSwipeToDelete(recyclerView)

        // Tombol untuk menambah mahasiswa baru
        view.findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(requireContext(), FormActivity::class.java))
        }

        // Muat data awal
        loadStudentsWithSampleData()
    }

    override fun onResume() {
        super.onResume()
        // Muat ulang data setiap kali fragment kembali aktif
        loadStudents()
    }

    private fun loadStudentsWithSampleData() {
        lifecycleScope.launch {
            val count = db.studentDao().getStudentCount()

            // sample data terlebih dahulu jika databae kosong
            if (count == 0) {
                val sampleData = listOf(
                    StudentEntity(
                        name = "Ahmad Fauzi",
                        nim = "2024001",
                        prodi = "T. Informatika",
                        email = "ahmad@email.com",
                        semester = 3
                    ),
                    StudentEntity(
                        name = "Budi Santoso",
                        nim = "2024002",
                        prodi = "Sistem Informasi",
                        email = "budi@email.com",
                        semester = 3
                    ),
                    StudentEntity(
                        name = "Clara Wijaya",
                        nim = "2024003",
                        prodi = "T. Informatika",
                        email = "clara@email.com",
                        semester = 3
                    )
                )
                db.studentDao().insertAll(sampleData)
            }

            // Setelah data di tambahkan tampilkan semua data
            val students = db.studentDao().getAllStudents()
            adapter.updateData(students)
            updateEmptyView(students.isEmpty())
        }
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            val students = db.studentDao().getAllStudents()
            adapter.updateData(students)
            updateEmptyView(students.isEmpty())
        }
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        view?.findViewById<TextView>(R.id.tvEmpty)?.visibility =
            if (isEmpty) View.VISIBLE else View.GONE
    }
    private fun showDeleteConfirmation(studentId: Int, studentName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data?")
            .setMessage("Hapus \"$studentName\"? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    db.studentDao().deleteById(studentId)
                    loadStudents()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0, // drag directions: tidak ada drag
            ItemTouchHelper.LEFT // swipe direction hanya ke kiri
        ) {
            override fun onMove(
                rv: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val student = adapter.getStudentAt(position)

                // Tampilkan dialog konfirmasi
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Data?")
                    .setMessage("Hapus \"${student.name}\"? Tindakan ini tidak dapat dibatalkan.")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            db.studentDao().deleteById(student.id)
                            loadStudents()
                        }
                    }
                    .setNegativeButton("Batal") { _, _ ->
                        // Batalkan swipe, kembalikan item ke posisi semula
                        adapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }

            // geser = HAPUS
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val paint = Paint()

                // Latar merah
                paint.color = Color.parseColor("#E53935")
                c.drawRect(
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    paint
                )

                // Teks "HAPUS"
                paint.color = Color.WHITE
                paint.textSize = 48f
                paint.textAlign = Paint.Align.CENTER
                val textY = (itemView.top + itemView.bottom) / 2f + paint.textSize / 3
                c.drawText(
                    "HAPUS",
                    itemView.right - 120f,
                    textY,
                    paint
                )

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }
}
