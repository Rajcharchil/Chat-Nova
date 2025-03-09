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
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import java.util.Arrays

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private val RC_SIGN_IN = 9001
    private var isFromSignup = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFromSignup = intent.getBooleanExtra("FROM_SIGNUP", false)
        auth = FirebaseAuth.getInstance()

        // 🔹 Google Sign-In Configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_ID))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 🔹 Facebook Login Configuration
        callbackManager = CallbackManager.Factory.create()

        binding.facebookeBox.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // Handle Facebook Login Success
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginPage) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@LoginPage, "Facebook Login Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginPage, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginPage, "Facebook Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onCancel() {
                Toast.makeText(this@LoginPage, "Facebook Login Canceled!", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@LoginPage, "Facebook Login Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // 🔹 UI Element References
        val emailField = binding.editTextEmailLogin
        val passwordField = binding.editTextPasswordLogin
        val loginButton = binding.buttonLogin
        val signupButton = binding.textViewSignup
        val forgotPasswordButton = binding.forgetPasscode
        val googleSignInButton = binding.googleBox

        // 🔹 Email and Password Login
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

        // 🔹 Google Sign-In
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // 🔹 Forgot Password
        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        // 🔹 Signup
        signupButton.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data) // Handle Facebook Login

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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun showAlertDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null && !isFromSignup) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Please sign in to continue.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}