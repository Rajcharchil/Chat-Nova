package com.charchil.chatnova
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import android.os.Bundle
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.charchil.chatnova.adapter.ChatAdapter
//import com.charchil.chatnova.models.ChatMessage
//
//import com.google.ai.client.generativeai.GenerativeModel
//
//import kotlinx.coroutines.launch
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var chatAdapter: ChatAdapter
//    private lateinit var chatList: ArrayList<ChatMessage>
//    private lateinit var progressBar: ProgressBar
//
//    // Store conversation history as a list of strings
//    private val conversationHistory = mutableListOf<String>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val eTPrompt = findViewById<EditText>(R.id.eTPrompt)
//        val btnSend = findViewById<ImageView>(R.id.btnSend)
//        val recyclerViewChat = findViewById<RecyclerView>(R.id.recyclerViewChat)
//        val helpTvView = findViewById<TextView>(R.id.help_tv)
//
//        eTPrompt.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                recyclerViewChat.visibility = View.VISIBLE
//                helpTvView.visibility = View.GONE
//            } else {
//                recyclerViewChat.visibility = View.GONE
//                helpTvView.visibility = View.VISIBLE
//            }
//        }
//
//        chatList = ArrayList()
//        chatAdapter = ChatAdapter(chatList)
//        recyclerViewChat.layoutManager = LinearLayoutManager(this)
//        recyclerViewChat.adapter = chatAdapter
//
//        btnSend.setOnClickListener {
//            val prompt = eTPrompt.text.toString()
//            if (prompt.isNotEmpty()) {
//                if (!isInternetAvailable()) {
//                    showNoInternetDialog()
//                    return@setOnClickListener
//                }
//
//                // Add user message to chat list and conversation history
//                chatList.add(ChatMessage(prompt, true))
//                conversationHistory.add("User: $prompt")
//                chatAdapter.notifyItemInserted(chatList.size - 1)
//                recyclerViewChat.scrollToPosition(chatList.size - 1)
//                eTPrompt.text.clear()
//
//                // Add a loading message to the chat list
//                chatList.add(ChatMessage("", false, isLoading = true))
//                chatAdapter.notifyItemInserted(chatList.size - 1)
//                recyclerViewChat.scrollToPosition(chatList.size - 1)
//
//                // Use CoroutineScope for background execution
//                lifecycleScope.launch {
//                    // AI Response
//                    val generativeModel = GenerativeModel(
//                        modelName = "gemini-2.0-flash",
//                        apiKey = getString(R.string.API_KEY) // Add your API Key here
//                    )
//
//                    // Prepare the full prompt with conversation history
//                    val fullPrompt = conversationHistory.joinToString("\n") + "\nAI:"
//                    val response = generativeModel.generateContent(fullPrompt)
//                    val aiMessage = response.text ?: "No response"
//
//                    // Remove the loading message
//                    val loadingIndex = chatList.indexOfFirst { it.isLoading }
//                    if (loadingIndex != -1) {
//                        chatList.removeAt(loadingIndex)
//                        chatAdapter.notifyItemRemoved(loadingIndex)
//                    }
//
//                    // Add AI response to chat list and conversation history
//                    chatList.add(ChatMessage(aiMessage, isLoading = false, isUser = false))
//                    conversationHistory.add("AI: $aiMessage")
//                    chatAdapter.notifyItemInserted(chatList.size - 1)
//                    recyclerViewChat.scrollToPosition(chatList.size - 1)
//                }
//            }
//        }
//
//
//
//
//    }
//
//    private fun isInternetAvailable(): Boolean {
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val network = connectivityManager.activeNetwork ?: return false
//        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
//        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//    }
//
//    private fun showNoInternetDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("❌Network Error\uD83C\uDF10")
//            .setMessage("Please check your internet connection and try again.")
//            .setPositiveButton("OK") { _, _ -> finishActivity(0) }
//            .setCancelable(false)
//            .show()
//    }
//}



import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.charchil.chatnova.adapter.ChatAdapter
import com.charchil.chatnova.models.ChatMessage
import com.charchil.chatnova.models.DrawerHandler
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<ChatMessage>
    private lateinit var drawerHandler: DrawerHandler
    private val conversationHistory = mutableListOf<String>() // Stores chat history

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


        // Initialize navigation drawer
//        drawerHandler = DrawerHandler(this, drawerLayout)
//        drawerHandler.setupDrawer()
        // Set up navigation drawer toggle
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
