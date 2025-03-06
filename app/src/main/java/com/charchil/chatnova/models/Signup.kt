package com.charchil.chatnova.models

import android.app.ProgressDialog
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
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Firebase Auth Initialization
        auth = FirebaseAuth.getInstance()

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing up...")
        progressDialog.setCancelable(false)

        // UI Element References
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
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }

        // 🔥 "Already have an account? Login" -> Navigate to LoginPage
        findViewById<TextView>(R.id.textView16).setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            intent.putExtra("FROM_SIGNUP", true) // Indicate that LoginPage is opened from Signup
            startActivity(intent)
        }

        // 🔥 Contact Us Click -> Open LinkedIn
        contactUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
            startActivity(intent)
        }
    }

    // ✅ Firebase User Signup
    private fun registerUser(email: String, password: String) {
        progressDialog.show() // Show loading indicator

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss() // Hide loading indicator

                if (task.isSuccessful) {
                    Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginPage::class.java)
                    intent.putExtra("FROM_SIGNUP", true) // Indicate that LoginPage is opened from Signup
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}