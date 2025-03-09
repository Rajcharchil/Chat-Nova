package com.charchil.chatnova.models

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.charchil.chatnova.R
import com.charchil.chatnova.VoiceAssistantActivity
import com.google.android.material.textfield.TextInputEditText

class ChatActivity : AppCompatActivity() {
    private lateinit var btnSend: ImageView
    private lateinit var edtMessage: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        btnSend = findViewById(R.id.btnSend)
        edtMessage = findViewById(R.id.eTPrompt)

        // Debug log
        Log.d("DEBUG", "Views initialized successfully")

        // Text change listener to toggle button icon
        edtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("DEBUG", "Text changed: $s")
                btnSend.post {
                    if (s.isNullOrEmpty()) {
                        btnSend.setImageResource(R.drawable.mic) // Mic icon
                    } else {
                        btnSend.setImageResource(R.drawable.send) // Send icon
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Button click event
        btnSend.setOnClickListener {
            if (edtMessage.text.isNullOrEmpty()) {
                // Open Voice Assistant Activity
                val intent = Intent(this, VoiceAssistantActivity::class.java)
                startActivity(intent)
            } else {
                // Send message logic
                sendMessage(edtMessage.text.toString())
            }
        }
    }

    private fun sendMessage(message: String) {
        // Message sending logic
        Log.d("DEBUG", "Message sent: $message")
        edtMessage.setText("") // Clear message after sending
    }
}
