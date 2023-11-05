package com.exercise.firebaseex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.exercise.firebaseex.databinding.ActivityHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!
        database = FirebaseDatabase.getInstance()

        val userEmail = user.email
        val userUID = user.uid

        val userRef = database.getReference("users").child(userUID).child("username")
        userRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userUsername = snapshot.value.toString()
                binding.emailName.text = userEmail
                binding.userName.text = userUsername
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomePageActivity, "Username not found in the database", Toast.LENGTH_SHORT).show()
            }

        })
    }


}