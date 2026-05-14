package com.example.studentcontactapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var editStudentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        db = AppDatabase.getDB(this)
        editStudentId = intent.getIntExtra("student_id", -1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =
            if (editStudentId == -1) "Tambah Mahasiswa" else "Edit Mahasiswa"

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etNim = findViewById<TextInputEditText>(R.id.etNim)
        val etProdi = findViewById<AutoCompleteTextView>(R.id.etProdi)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etSemester = findViewById<TextInputEditText>(R.id.etSemester)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Isi dropdown pilihan prodi
        val prodiOptions = arrayOf(
            "T. Informatika", "Sistem Informasi", "T. Elektro",
            "T. Sipil", "T. Mesin", "Manajemen", "Akuntansi"
        )
        etProdi.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, prodiOptions)
        )

        // mode edit, ambil data dari database lalu isi ke semua field
        if (editStudentId != -1) {
            lifecycleScope.launch {
                val student = db.studentDao().getStudentById(editStudentId)
                student?.let {
                    etName.setText(it.name)
                    etNim.setText(it.nim)
                    etProdi.setText(it.prodi, false)
                    etEmail.setText(it.email)
                    etSemester.setText(it.semester.toString())
                }
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val nim = etNim.text.toString().trim()
            val prodi = etProdi.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val semester = etSemester.text.toString().trim().toIntOrNull() ?: 1

            // Validasi field wajib
            if (name.isEmpty()) {
                etName.error = "Nama wajib diisi"
                etName.requestFocus()
                return@setOnClickListener
            }
            if (nim.isEmpty()) {
                etNim.error = "NIM wajib diisi"
                etNim.requestFocus()
                return@setOnClickListener
            }

            // Simpan ke database menggunakan coroutine
            lifecycleScope.launch {
                if (editStudentId == -1) {
                    // Mode TAMBAH: insert data baru
                    db.studentDao().insert(
                        StudentEntity(
                            name = name,
                            nim = nim,
                            prodi = prodi,
                            email = email,
                            semester = semester
                        )
                    )
                    Toast.makeText(
                        this@FormActivity,
                        "Mahasiswa berhasil ditambahkan!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Mode EDIT: update data yang sudah ada
                    db.studentDao().update(
                        StudentEntity(
                            id = editStudentId,
                            name = name,
                            nim = nim,
                            prodi = prodi,
                            email = email,
                            semester = semester
                        )
                    )
                    Toast.makeText(
                        this@FormActivity,
                        "Data berhasil diperbarui!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
