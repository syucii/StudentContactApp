package com.example.studentcontactapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tampilkan HomeFragment sebagai tampilan awal
        if (savedInstanceState == null) {
            replace(HomeFragment())
        }

        // Atur navigasi bottom bar
        findViewById<Button>(R.id.btnHome).setOnClickListener {
            replace(HomeFragment())
        }
        findViewById<Button>(R.id.btnSearch).setOnClickListener {
            replace(SearchFragment())
        }
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            replace(ProfileFragment())
        }
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
