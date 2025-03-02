package com.charchil.chatnova.models

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.charchil.chatnova.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Firebase Auth Initialization
        auth = FirebaseAuth.getInstance()

        val nameField = findViewById<TextInputEditText>(R.id.editTextName)
        val emailField = findViewById<TextInputEditText>(R.id.editTextEmail)
        val passwordField = findViewById<TextInputEditText>(R.id.editTextPassword)
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)
        val contactUs = findViewById<TextView>(R.id.contactUs)
        val linkedInUrl = "https://www.linkedin.com/company/chatnova-ai-llc/"


        // 🔥 Sign-Up Button Click
        signUpButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all the fields correctly", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }

        // 🔥 "Already have an account? Login" -> Login Page pe bheje
        findViewById<TextView>(R.id.textView16).setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }
        
        // 🔥 Contact Us Click -> LinkedIn Open
        contactUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
            startActivity(intent)
        }
    }

    // ✅ Firebase User Signup
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginPage::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

}
