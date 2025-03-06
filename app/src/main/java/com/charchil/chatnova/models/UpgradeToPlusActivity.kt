package com.charchil.chatnova.models

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.charchil.chatnova.R

class UpgradeToPlusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade_to_plus) // Ensure this layout exists

        // Initialize buttons
        val btnSubscribe: Button = findViewById(R.id.btnSubscribe)
        val btnRestore: Button = findViewById(R.id.btnRestore)
        val btnBack: ImageView = findViewById(R.id.btnback)


        // Handle Subscribe Button Click
        btnSubscribe.setOnClickListener {
            // Redirect to subscription page (replace URL with your actual subscription page)
            val subscriptionUrl = "https://your-subscription-page.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(subscriptionUrl))
            startActivity(intent)
        }

        // Handle Restore Subscription Button Click
        btnRestore.setOnClickListener {
            // Add logic to restore subscription (e.g., contact support or redirect to a restore page)
            Toast.makeText(this, "Restore Your Subscription ✨", Toast.LENGTH_SHORT).show()
        }
        // Handle Back Button Click
        btnBack.setOnClickListener {
            // Navigate back to the previous activity
            onBackPressed()
        }
    }
}