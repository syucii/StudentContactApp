package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.studentcontactapp.utils.PrefManager
import com.example.studentcontactapp.utils.SettingsManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = PrefManager(requireContext())
        val settings = SettingsManager(requireContext())

        // Tampilkan nama pengguna yang tersimpan di SharedPreferences
        view.findViewById<TextView>(R.id.tvUser).text = pref.getUsername()

        // Toggle Dark Mode
        val swDark = view.findViewById<SwitchCompat>(R.id.swDark)
        swDark.isChecked = settings.isDarkMode()
        swDark.setOnCheckedChangeListener { _, isChecked ->
            settings.setDarkMode(isChecked)
        }

        // Toggle Ukuran Font
        val swFont = view.findViewById<SwitchCompat>(R.id.swFont)
        swFont.isChecked = settings.getFontSize() > 14
        swFont.setOnCheckedChangeListener { _, isChecked ->
            settings.setFontSize(if (isChecked) 18 else 14)
        }

        // Toggle Notifikasi
        val swNotif = view.findViewById<SwitchCompat>(R.id.swNotif)
        swNotif.isChecked = settings.isNotifikasi()
        swNotif.setOnCheckedChangeListener { _, isChecked ->
            settings.setNotifikasi(isChecked)
        }

        // Tombol Logout
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // Hapus data sesi dari SharedPreferences
            pref.logout()
            // Kembali ke halaman login dan hapus back stack
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
