package com.exercise.firebaseex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exercise.firebaseex.databinding.ActivityRegisterPageOneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ActivityRegisterSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPageOneBinding
    private lateinit var firebaseAouth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTermsServices.setOnClickListener {
            val browseTermAndServicePage = Intent(Intent.ACTION_VIEW,Uri.parse("https://firebase.google.com/terms"))
            startActivity(browseTermAndServicePage)
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            val browsePrivatePolicePage = Intent(Intent.ACTION_VIEW,Uri.parse("https://firebase.google.com/support/privacy"))
            startActivity(browsePrivatePolicePage)
        }

        firebaseAouth = FirebaseAuth.getInstance()
        lLayoutEmailAndPassword()


        binding.btnNext.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (isEmailValid(email) && isPasswordValid(password)){
                lLinLayoutUserName()
                binding.btnSignUp.setOnClickListener {
                    val username = binding.etUsername.text.toString()
                    if (username.isNotEmpty() && username.length >= 6){

                        firebaseAouth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener {

                            if (it.isSuccessful){
                                val newUser = firebaseAouth.currentUser
                                val userUID = firebaseAouth.uid
                                saveUsernameToRTDB(userUID, username)
                                Toast.makeText(this, "New User Registered " +
                                        "${newUser?.email}", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,HomePageActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "Registration Failed " +
                                        "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        toast(getString(R.string.min_six_characters))
                    }
                }
            }else{
                toast(getString(R.string.enter_valid_info))
            }
        }

        binding.backBtn.setOnClickListener {
            if (binding.linLayoutEmailPassword.visibility == View.VISIBLE) {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }else{
                lLayoutEmailAndPassword()
            }
        }

    }

    private fun saveUsernameToRTDB(userUID:String?,username:String) {
        if (userUID != null){
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            val userNode = databaseReference.child(userUID)
            userNode.child("username").setValue(username)
                .addOnSuccessListener {
                    toast(getString(R.string.username_saved))
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed to save username. Error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun toast(text: String){
        Toast.makeText(this,
            text, Toast.LENGTH_SHORT).show()
    }

    private fun lLayoutEmailAndPassword(){
        binding.linLayoutEmailPassword.visibility = View.VISIBLE
        binding.linLayoutUsername.visibility = View.INVISIBLE
    }

    private fun lLinLayoutUserName(){
        binding.linLayoutEmailPassword.visibility = View.INVISIBLE
        binding.linLayoutUsername.visibility = View.VISIBLE
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
        return emailRegex.matches(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

}