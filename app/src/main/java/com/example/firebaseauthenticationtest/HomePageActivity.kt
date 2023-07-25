package com.example.firebaseauthenticationtest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val navbar=findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val logoutBtn=findViewById<Button>(R.id.logout)
        LoadFragment(Home())
        navbar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->LoadFragment(Home())
                R.id.search->LoadFragment(search())
                R.id.profile->LoadFragment(Profile())
            }
            true
        }

        logoutBtn.setOnClickListener {
            val sharedPreferences =getSharedPreferences("userPref", Context.MODE_PRIVATE)
            val editUserPref = sharedPreferences.edit()
            editUserPref.putBoolean("userLogin",false)
            editUserPref.apply()
            startActivity(Intent(this,FirebaseAuthActivity::class.java))
        }
    }
    private fun LoadFragment(framgment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fream,framgment).commit()
    }
}