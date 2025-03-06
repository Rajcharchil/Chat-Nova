package com.charchil.chatnova.models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.charchil.chatnova.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class DrawerHandler(private val context: Context, private val drawerLayout: DrawerLayout) :
    NavigationView.OnNavigationItemSelectedListener {

    private val auth = FirebaseAuth.getInstance() // Initialize Firebase Auth

    // Initialize Google Sign-In Client
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_ID)) // Use your web_client_ID
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_email -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@example.com")
                }
                context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
            }

            R.id.nav_phone -> {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:")
                }
                context.startActivity(dialIntent)
            }

            R.id.nav_upgrade -> {
                val intent = Intent(context, UpgradeToPlusActivity::class.java)
                context.startActivity(intent)
                true
            }
            R.id.nav_customize -> showToast("Customize Clicked")
            R.id.nav_data -> showToast("Data Controls Clicked")
            R.id.nav_voice -> showToast("Voice Clicked")
            R.id.nav_about -> showToast("About Clicked")

            R.id.nav_signout -> {
                showToast("Signing Out...")
                signOutUser() // Sign out the user
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START) // Close drawer after selection
        return true
    }

    // ✅ Sign Out the User
    private fun signOutUser() {
        // Sign out from Firebase
        auth.signOut()

        // Revoke Google Sign-In session
        googleSignInClient.signOut().addOnCompleteListener {
            // Navigate to LoginPage and clear the activity stack
            val intent = Intent(context, LoginPage::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)

            // Finish the current activity (if the context is an AppCompatActivity)
            (context as? AppCompatActivity)?.finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}