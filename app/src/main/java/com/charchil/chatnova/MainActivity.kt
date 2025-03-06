package com.charchil.chatnova

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.charchil.chatnova.adapter.ChatAdapter
import com.charchil.chatnova.models.ChatMessage
import com.charchil.chatnova.models.DrawerHandler
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<ChatMessage>
    private lateinit var drawerHandler: DrawerHandler
    private val conversationHistory = mutableListOf<String>() // Stores chat history
    private lateinit var auth: FirebaseAuth //  Add FirebaseAuth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eTPrompt = findViewById<EditText>(R.id.eTPrompt)
        val btnSend = findViewById<ImageView>(R.id.btnSend)
        val recyclerViewChat = findViewById<RecyclerView>(R.id.recyclerViewChat)
        val helpTvView = findViewById<TextView>(R.id.help_tv)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        //  Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //  Update NavigationView header with user data
        val headerView = navigationView.getHeaderView(0)
        val imgProfile: ImageView = headerView.findViewById(R.id.profile_image)
        val txtUserName: TextView = headerView.findViewById(R.id.tvUsername)
        val txtUserEmail: TextView = headerView.findViewById(R.id.tvUserEmail) // Ensure this TextView exists in your header XML

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Set user name
            val userName = currentUser.displayName ?: "User Name"
            txtUserName.text = userName

            // Set user email
            val userEmail = currentUser.email ?: "user@example.com"
            txtUserEmail.text = userEmail

            // Set profile picture
            val photoUrl: Uri? = currentUser.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop() // Make the image circular
                    .into(imgProfile)
            } else {
                // Use a default profile picture if no photo is available
                imgProfile.setImageResource(R.drawable.charchilimg)
            }
        }

        val drawerHandler = DrawerHandler(this, drawerLayout)
        navigationView.setNavigationItemSelectedListener(drawerHandler)

        // Open drawer when clicking on the navigation icon
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        // Handle input field focus
        eTPrompt.setOnFocusChangeListener { _, hasFocus ->
            recyclerViewChat.visibility = if (hasFocus) View.VISIBLE else View.GONE
            helpTvView.visibility = if (hasFocus) View.GONE else View.VISIBLE
        }

        chatList = ArrayList()
        chatAdapter = ChatAdapter(chatList)
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
        recyclerViewChat.adapter = chatAdapter

        btnSend.setOnClickListener {
            val prompt = eTPrompt.text.toString().trim()
            if (prompt.isNotEmpty()) {
                if (!isInternetAvailable()) {
                    showNoInternetDialog()
                    return@setOnClickListener
                }

                //  Input Box ko Clear Karo (Text Disappear)
                eTPrompt.text.clear()

                // Add user message and loading indicator
                addMessageToChat(prompt, isUser = true)
                showLoadingIndicator()

                // Fetch AI response
                lifecycleScope.launch {
                    fetchAIResponse()
                }
            }
        }
    }

    private fun addMessageToChat(message: String, isUser: Boolean) {
        chatList.add(ChatMessage(message, isUser))
        conversationHistory.add(if (isUser) "User: $message" else "AI: $message")
        chatAdapter.notifyItemInserted(chatList.size - 1)
        findViewById<RecyclerView>(R.id.recyclerViewChat).scrollToPosition(chatList.size - 1)
    }

    private fun showLoadingIndicator() {
        chatList.add(ChatMessage("", isUser = false, isLoading = true))
        chatAdapter.notifyItemInserted(chatList.size - 1)
    }

    private suspend fun fetchAIResponse() {
        val apiKey = getString(R.string.API_KEY)
        val generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = apiKey
        )

        val fullPrompt = conversationHistory.joinToString("\n") + "\nAI:"
        val response = generativeModel.generateContent(fullPrompt)
        val aiMessage = response.text ?: "I'm sorry, I didn't understand that."

        // Remove loading indicator
        val loadingIndex = chatList.indexOfFirst { it.isLoading }
        if (loadingIndex != -1) {
            chatList.removeAt(loadingIndex)
            chatAdapter.notifyItemRemoved(loadingIndex)
        }

        // Add AI response
        addMessageToChat(aiMessage, isUser = false)
    }


    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("❌ Network Error 🌐")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}