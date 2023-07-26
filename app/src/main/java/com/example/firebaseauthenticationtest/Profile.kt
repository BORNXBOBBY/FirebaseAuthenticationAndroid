package com.example.firebaseauthenticationtest

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class  Profile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  roeView =inflater.inflate(R.layout.fragment_profile, container, false)
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance().currentUser?.uid
        val gallaryAccess = roeView .findViewById<ImageView>(R.id.images)
        val name = roeView .findViewById<EditText>(R.id.names)
        val number =roeView . findViewById<EditText>(R.id.numbers)
        val email = roeView .findViewById<EditText>(R.id.emails)


        val button =roeView . findViewById<Button>(R.id.edit)

        button.setOnClickListener {
            startActivity(Intent(requireContext(), GalleryAccess::class.java))
        }


        if (auth != null) {
            db.collection("users").document(auth)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) { // Check if snapshot is not null and exists

                        val myData: DataModel? = snapshot.toObject(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null

//                            FashionList.add(myData)

                            name.setText(myData.name)
                            number.setText(myData.number)
                            email.setText(myData.email)
                            Glide.with(this)
                                .load(myData.imageUrl)
                                .error(R.drawable.ic_launcher_background)
                                .into(gallaryAccess)


                        } else {
                            Log.e(ContentValues.TAG, "DataModel is null")
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Snapshot is null or doesn't exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error adding document", e)
                }
        }
        return  roeView
    }


    }
