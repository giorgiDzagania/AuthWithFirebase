package com.exercise.firebaseex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.exercise.firebaseex.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAouth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAouth = FirebaseAuth.getInstance()

        binding.backBtnToMainPage.setOnClickListener {
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.etEmailLogIn.text.toString()
            val password = binding.etPasswordLogIn.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAouth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this@LoginActivity,"LogIn succeeded! ${firebaseAouth.currentUser?.email}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,HomePageActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@LoginActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                toast(getString(R.string.enter_valid_info))
            }
        }


    }

    private fun toast(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}