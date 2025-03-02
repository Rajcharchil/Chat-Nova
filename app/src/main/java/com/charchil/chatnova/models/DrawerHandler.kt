
package com.charchil.chatnova.models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.charchil.chatnova.R

class DrawerHandler(private val context: Context, private val drawerLayout: DrawerLayout) :
    NavigationView.OnNavigationItemSelectedListener {

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
                    data = Uri.parse("tel:+919999999999")
                }
                context.startActivity(dialIntent)
            }

            R.id.nav_upgrade -> showToast("Upgrade to Plus Clicked")
            R.id.nav_customize -> showToast("Customize Clicked")
            R.id.nav_data -> showToast("Data Controls Clicked")
            R.id.nav_voice -> showToast("Voice Clicked")
            R.id.nav_about -> showToast("About Clicked")

            R.id.nav_signout -> {
                showToast("Signing Out...")
                (context as? AppCompatActivity)?.finishAffinity() // Closes all activities
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START) // Close drawer after selection
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}