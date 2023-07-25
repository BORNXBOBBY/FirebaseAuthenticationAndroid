package com.example.firebaseauthenticationtest

import  android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private  lateinit var imageView: ImageView
    private lateinit var selectImgBtn: Button
    private lateinit var uploadImgBtn: Button
    private var storageRef= Firebase.storage
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            startActivity(Intent(this,FirebaseAuthActivity::class.java))
            val splashSharedPreferences =getSharedPreferences("userPref", Context.MODE_PRIVATE)
            val loginStatus = splashSharedPreferences.getBoolean("userLogin",false)
            if (loginStatus)
            {
                val intent = Intent(this, FirebaseAuthActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, HomePageActivity ::class.java)
                startActivity(intent)
                finish()
            }
        },2000)

    }
}