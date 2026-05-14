package com.example.studentcontactapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.utils.FileHelper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Mahasiswa"

        val studentId = intent.getIntExtra("student_id", -1)
        if (studentId == -1) {
            finish()
            return
        }

        val db = AppDatabase.getDB(this)

        // Ambil data mahasiswa dari Room Database menggunakan coroutine
        lifecycleScope.launch {
            val student = db.studentDao().getStudentById(studentId)
            if (student == null) {
                finish()
                return@launch
            }

            // Tampilkan inisial nama sebagai avatar
            val initials = student.name
                .split(" ")
                .take(2)
                .joinToString("") { it.take(1).uppercase() }

            findViewById<TextView>(R.id.tvAvatar).text = initials
            findViewById<TextView>(R.id.tvName).text = student.name
            findViewById<TextView>(R.id.tvNimProdi).text = "${student.nim} · ${student.prodi}"

            val etNote = findViewById<TextInputEditText>(R.id.etNote)
            val tvStatus = findViewById<TextView>(R.id.tvNoteStatus)
            val btnSave = findViewById<Button>(R.id.btnSaveNote)
            val btnLoad = findViewById<Button>(R.id.btnLoadNote)

            // Muat catatan yang sudah ada saat halaman dibuka
            val existingNote = FileHelper.loadNote(this@DetailActivity, student.nim)
            if (existingNote.isNotEmpty()) {
                etNote.setText(existingNote)
                tvStatus.text = "✓ Catatan tersedia"
            }

            // Tombol Simpan: menyimpan catatan ke Internal Storage
            btnSave.setOnClickListener {
                val noteText = etNote.text.toString()
                val fileSize = FileHelper.saveNote(this@DetailActivity, student.nim, noteText)
                tvStatus.text = "✓ Tersimpan ($fileSize bytes)"
                Toast.makeText(this@DetailActivity, "Catatan berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }

            // Tombol Muat: memuat catatan dari Internal Storage
            btnLoad.setOnClickListener {
                val loaded = FileHelper.loadNote(this@DetailActivity, student.nim)
                if (loaded.isNotEmpty()) {
                    etNote.setText(loaded)
                    Toast.makeText(this@DetailActivity, "Catatan berhasil dimuat!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailActivity, "Belum ada catatan tersimpan.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
