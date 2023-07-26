package com.example.firebaseauthenticationtest

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class FirebaseAuthActivity : AppCompatActivity() {
    lateinit var phoneNumber: String
    private lateinit var vId: String
    lateinit var auth: FirebaseAuth
    private var requestcode = 1234
    private lateinit var Data: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_firebase_auth)


        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        val gallery = findViewById<Button>(R.id.gallery)
        gallery.setOnClickListener {
            startActivity(Intent(this, GalleryAccess::class.java))
        }


        var phone = findViewById<EditText>(R.id.phone_number)
        var ccp = findViewById<CountryCodePicker>(R.id.ccp)
        var buttonauth = findViewById<Button>(R.id.sendOtp)

        buttonauth.setOnClickListener {
            ccp.registerCarrierNumberEditText(phone)

            phoneNumber = ccp.fullNumberWithPlus.replace(" ", "")
            initiateOtp()


        }
        // verify otp


        var enterOtp = findViewById<EditText>(R.id.otp)
        var verifyButton = findViewById<Button>(R.id.verifyOtp)
        verifyButton.setOnClickListener {

            val credential = PhoneAuthProvider.getCredential(vId, enterOtp.text.toString())
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Registration successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomePageActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "not verify", Toast.LENGTH_SHORT).show()
                }

        }
        // EmailAndPassword

        var email = findViewById<EditText>(R.id.email)
        var pass = findViewById<EditText>(R.id.password)
        var button = findViewById<Button>(R.id.submit)
        button.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "SignUp Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomePageActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            auth.signOut()
            val googleIntent = googleSignInClient.signInIntent
            startActivityForResult(googleIntent, requestcode)
        }

    }
// send otp message


    private fun initiateOtp() {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    vId = verificationId
                    Toast.makeText(
                        this@FirebaseAuthActivity,
                        "send $verificationId",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {

                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    override fun onActivityResult(ActivityRequestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(ActivityRequestCode, resultCode, data)


        if (ActivityRequestCode == requestcode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener { it ->
                val credencial = GoogleAuthProvider.getCredential(it.idToken, null)
                auth.signInWithCredential(credencial)
                    .addOnSuccessListener {
                        val auth = FirebaseAuth.getInstance().currentUser?.uid

                        val db = FirebaseFirestore.getInstance()
                        val user = hashMapOf(
                            "name" to it.user?.displayName,
                            "imageUrl" to it.user?.photoUrl,
                            "email" to it.user?.email,
                            "number" to it.user?.phoneNumber
                        )

// Add a new document with a generated ID
                        if (auth != null) {
                            db.collection("users").document(auth)
                                .set(user)
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID")
                                    startActivity(Intent(this, GalleryAccess::class.java))

                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }

    }
}