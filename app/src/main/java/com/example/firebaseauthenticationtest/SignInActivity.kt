package com.example.firebaseauthenticationtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var data: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        data = Firebase.auth
        data= Firebase.auth
        var email=findViewById<TextInputEditText>(R.id.emailEt)
        var pass=findViewById<TextInputEditText>(R.id.passET)
        var button=findViewById<Button>(R.id.button)
        button.setOnClickListener{
            data.signInWithEmailAndPassword(email.text.toString(),pass.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"Login Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,HomePageActivity::class.java))
                }
                .addOnFailureListener{
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }


        }
    }
}