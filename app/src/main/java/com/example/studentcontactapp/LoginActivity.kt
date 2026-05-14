package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentcontactapp.utils.PrefManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = PrefManager(this)

        // Cek apakah pengguna sudah login dan "Remember Me" aktif
        if (pref.isLoggedIn() && pref.isRemember()) {
            navigateToMain()
            return
        }

        setContentView(R.layout.activity_login)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPass = findViewById<EditText>(R.id.etPass)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUser.text.toString().trim()
            val password = etPass.text.toString().trim()

            when {
                username.isEmpty() -> {
                    etUser.error = "Username tidak boleh kosong"
                    etUser.requestFocus()
                }
                password.isEmpty() -> {
                    etPass.error = "Password tidak boleh kosong"
                    etPass.requestFocus()
                }
                username == "admin" && password == "123456" -> {
                    // Login berhasil: simpan data ke SharedPreferences
                    pref.saveLogin(username, cbRemember.isChecked)
                    navigateToMain()
                }
                else -> {
                    Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
