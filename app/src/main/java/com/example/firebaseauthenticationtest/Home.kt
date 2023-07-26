package com.example.firebaseauthenticationtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val roeView= inflater.inflate(R.layout.fragment_home, container, false)
        val imageView=roeView.findViewById<ImageView>(R.id.taylor)
        val firebaseData = FirebaseFirestore.getInstance()
        firebaseData.collection("imageURL").document("1690280546098").get()
            .addOnSuccessListener { documentSnapshot ->

                    // 3. Retrieve the imageURL from the documentSnapshot
                    val imageUrl = documentSnapshot.getString("imageUrl")

                    // 4. Make sure the imageUrl is not null before using Glide
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(imageView)

                    // Handle case where the document does not exist
                    // You might want to show a placeholder image or handle this situation accordingly.
                }
            .addOnFailureListener { exception ->
                // 5. Handle any failure in retrieving the data from Firestore
                // For example, you can log the error:
            }

return  roeView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}