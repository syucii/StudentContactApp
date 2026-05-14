package com.example.studentcontactapp.utils

import android.content.Context
import java.io.File

/**
 * Objek helper untuk operasi baca/tulis catatan mahasiswa menggunakan Internal Storage.
 * File catatan disimpan dengan format nama "note_{nim}.txt" di direktori internal aplikasi.
 */
object FileHelper {

    /**
     * Menyimpan catatan mahasiswa ke Internal Storage.
     * @param ctx Context aplikasi
     * @param nim Nomor Induk Mahasiswa sebagai identifier unik file
     * @param text Isi catatan yang akan disimpan
     * @return Ukuran file dalam bytes setelah disimpan
     */
    fun saveNote(ctx: Context, nim: String, text: String): Long {
        val file = File(ctx.filesDir, "note_$nim.txt")
        file.writeText(text)
        return file.length()
    }

    /**
     * Memuat catatan mahasiswa dari Internal Storage.
     * @param ctx Context aplikasi
     * @param nim Nomor Induk Mahasiswa sebagai identifier unik file
     * @return Isi catatan sebagai String, atau string kosong jika file tidak ada
     */
    fun loadNote(ctx: Context, nim: String): String {
        val file = File(ctx.filesDir, "note_$nim.txt")
        return if (file.exists()) file.readText() else ""
    }

    /**
     * Memeriksa apakah file catatan untuk mahasiswa tertentu sudah ada.
     */
    fun noteExists(ctx: Context, nim: String): Boolean {
        return File(ctx.filesDir, "note_$nim.txt").exists()
    }

    /**
     * Menghapus file catatan mahasiswa dari Internal Storage.
     */
    fun deleteNote(ctx: Context, nim: String) {
        File(ctx.filesDir, "note_$nim.txt").delete()
    }
}
