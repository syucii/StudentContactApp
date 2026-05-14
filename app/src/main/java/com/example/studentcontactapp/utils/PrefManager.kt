package com.example.studentcontactapp.utils

import android.content.Context

/**
 * Kelas helper untuk mengelola data login pengguna menggunakan SharedPreferences.
 * Menyimpan status login, nama pengguna, dan preferensi "Remember Me".
 */
class PrefManager(context: Context) {
    private val pref = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE)

    /**
     * Menyimpan data login pengguna ke SharedPreferences.
     * @param username Nama pengguna yang berhasil login
     * @param remember Status centang "Remember Me"
     */
    fun saveLogin(username: String, remember: Boolean) {
        pref.edit()
            .putBoolean("is_login", true)
            .putString("username", username)
            .putBoolean("remember", remember)
            .apply()
    }

    fun isLoggedIn(): Boolean = pref.getBoolean("is_login", false)
    fun isRemember(): Boolean = pref.getBoolean("remember", false)
    fun getUsername(): String = pref.getString("username", "") ?: ""

    /**
     * Menghapus semua data sesi pengguna saat logout.
     */
    fun logout() = pref.edit().clear().apply()
}
