package com.charchil.chatnova.models

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.charchil.chatnova.MainActivity
import com.charchil.chatnova.R
import com.charchil.chatnova.databinding.ActivityLoginPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001  // Result code for Google Sign-In

    // Flag to track if LoginPage is opened from Signup activity
    private var isFromSignup = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if LoginPage is opened from Signup activity
        isFromSignup = intent.getBooleanExtra("FROM_SIGNUP", false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 🔹 Google Sign-In Configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_ID)) // Fixed typo: web_clint_ID -> web_client_ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 🔹 UI Element References
        val emailField = binding.editTextEmailLogin
        val passwordField = binding.editTextPasswordLogin
        val loginButton = binding.buttonLogin
        val signupButton = binding.textViewSignup
        val forgotPasswordButton = binding.forgetPasscode
        val googleSignInButton = binding.googleBox

        // 🔥 Login Button Click
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showAlertDialog("Invalid Email", "Please enter a valid email address.")
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6) {
                showAlertDialog("Invalid Password", "Password must be at least 6 characters long.")
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        // 🔹 Google Sign-In Button Click
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // 🔹 Forgot Password Click
        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        // 🔹 Sign-Up Text Click
        signupButton.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    // ✅ Firebase User Login
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    showAlertDialog("Login Failed", task.exception?.message ?: "Please try again.")
                }
            }
    }

    // ✅ Google Sign-In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("LoginPage", "Google Sign-In failed", e)
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Firebase Authentication with Google
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        showAlertDialog("Authentication Failed", task.exception?.message ?: "Please try again.")
                    }
                }
        }
    }

    // ✅ Show Alert Dialog for Errors
    private fun showAlertDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    // ✅ Update UI based on user authentication state
    private fun updateUI(user: FirebaseUser?) {
        if (user != null && !isFromSignup) {
            // User is signed in and LoginPage is not opened from Signup activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // User is not signed in or LoginPage is opened from Signup activity
            Toast.makeText(this, "Please sign in to continue.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}