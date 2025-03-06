package com.charchil.chatnova.models

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.charchil.chatnova.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // UI Element References
        val emailField = findViewById<TextInputEditText>(R.id.editTextEmailLogin)
        val resetPasswordButton = findViewById<Button>(R.id.btnResetPassword)
        val backButton = findViewById<Button>(R.id.backbtn)

        // 🔥 Reset Password Button Click
        resetPasswordButton.setOnClickListener {
            val email = emailField.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else {
                sendPasswordResetEmail(email)
            }
        }

        // 🔥 Back Button Click
        backButton.setOnClickListener {
            // Navigate back to LoginPage
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    // ✅ Send Password Reset Email
    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}