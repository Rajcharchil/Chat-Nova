package com.charchil.chatnova.models

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.charchil.chatnova.MainActivity
import com.charchil.chatnova.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Use Handler to delay the redirection
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserLoginStatus()
        }, 3000) // 3 seconds delay
    }

    // ✅ Check if the user is logged in
    private fun checkUserLoginStatus() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is logged in, redirect to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not logged in, redirect to LoginPage
            startActivity(Intent(this, LoginPage::class.java))
        }

        // Finish the SplashActivity to prevent going back
        finish()
    }
}