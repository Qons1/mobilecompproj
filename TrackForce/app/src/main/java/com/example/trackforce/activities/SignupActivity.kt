package com.example.trackforce.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class SignupActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etRole = findViewById<EditText>(R.id.etRole)

        btnSignup.setOnClickListener {
            viewModel.signup(etEmail.text.toString(), etPassword.text.toString(), etRole.text.toString()) { success, msg ->
                if (success) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

