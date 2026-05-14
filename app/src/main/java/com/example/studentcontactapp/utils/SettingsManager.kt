package com.example.studentcontactapp.utils

import android.content.Context

/**
 * Kelas helper untuk mengelola pengaturan aplikasi menggunakan SharedPreferences.
 * Menyimpan preferensi Dark Mode, Notifikasi, dan Ukuran Font.
 */
class SettingsManager(context: Context) {
    private val pref = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    // --- Dark Mode ---
    fun setDarkMode(enabled: Boolean) = pref.edit().putBoolean("dark_mode", enabled).apply()
    fun isDarkMode(): Boolean = pref.getBoolean("dark_mode", false)

    // --- Notifikasi ---
    fun setNotifikasi(enabled: Boolean) = pref.edit().putBoolean("notifikasi", enabled).apply()
    fun isNotifikasi(): Boolean = pref.getBoolean("notifikasi", true)

    // --- Ukuran Font ---
    fun setFontSize(size: Int) = pref.edit().putInt("font_size", size).apply()
    fun getFontSize(): Int = pref.getInt("font_size", 14)
}
