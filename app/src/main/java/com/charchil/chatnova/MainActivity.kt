package com.charchil.chatnova

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import android.util.Base64
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<ChatMessage>
    private lateinit var drawerHandler: DrawerHandler
    private val conversationHistory = mutableListOf<String>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eTPrompt = findViewById<EditText>(R.id.eTPrompt)
        val btnSend = findViewById<ImageView>(R.id.btnSend)
        val btnImageSearch = findViewById<ImageView>(R.id.btnImageSearch)
        val recyclerViewChat = findViewById<RecyclerView>(R.id.recyclerViewChat)
        val helpTvView = findViewById<TextView>(R.id.help_tv)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        auth = FirebaseAuth.getInstance()

        val headerView = navigationView.getHeaderView(0)
        val imgProfile: ImageView = headerView.findViewById(R.id.profile_image)
        val txtUserName: TextView = headerView.findViewById(R.id.tvUsername)
        val txtUserEmail: TextView = headerView.findViewById(R.id.tvUserEmail)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            txtUserName.text = currentUser.displayName ?: "User Name"
            txtUserEmail.text = currentUser.email ?: "user@example.com"
            val photoUrl: Uri? = currentUser.photoUrl
            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).circleCrop().into(imgProfile)
            } else {
                imgProfile.setImageResource(R.drawable.charchilimg)
            }
        }

        drawerHandler = DrawerHandler(this, drawerLayout)
        navigationView.setNavigationItemSelectedListener(drawerHandler)

        toolbar.setNavigationOnClickListener { drawerLayout.open() }

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
                eTPrompt.text.clear()
                addMessageToChat(prompt, isUser = true)
                showLoadingIndicator()
                lifecycleScope.launch { fetchAIResponse() }
            }
        }

        btnImageSearch.setOnClickListener {
            showImagePickerDialog()
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
        val generativeModel = GenerativeModel("gemini-2.0-flash", apiKey)
        val fullPrompt = conversationHistory.joinToString("\n") + "\nAI:"
        val response = generativeModel.generateContent(fullPrompt)
        val aiMessage = response.text ?: "I'm sorry, I didn't understand that."

        val loadingIndex = chatList.indexOfFirst { it.isLoading }
        if (loadingIndex != -1) {
            chatList.removeAt(loadingIndex)
            chatAdapter.notifyItemRemoved(loadingIndex)
        }
        addMessageToChat(aiMessage, isUser = false)
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("❌ Network Error 🌐")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("📸 Camera", "🖼 Gallery", "📂 Document")

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog) // Custom theme use karo
        builder.setTitle("Choose an option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                    2 -> openFilePicker()
                }
            }.show()
    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 100)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 101)
    }

    private fun openFilePicker() {
        val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
        fileIntent.type = "*/*"
        startActivityForResult(fileIntent, 102)
    }
    private fun uploadImage(bitmap: Bitmap) {
        Toast.makeText(this, "📸 Camera Image Selected", Toast.LENGTH_SHORT).show()
    }

    private fun uploadUri(uri: Uri?) {
        Toast.makeText(this, "🖼 Gallery Image Selected: $uri", Toast.LENGTH_SHORT).show()
    }

    private fun uploadFile(uri: Uri?) {
        Toast.makeText(this, "📂 Document Selected: $uri", Toast.LENGTH_SHORT).show()
    }

    // 1. Bitmap to Base64 Conversion Function

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // 2. Base64 to Bitmap Conversion Function

    private fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    // 3. Upload Base64 Image to Firestore

    private fun uploadImageToFirestore(bitmap: Bitmap, imageName: String) {
        val base64String = bitmapToBase64(bitmap)
        val firestore = FirebaseFirestore.getInstance()
        val imageMap = hashMapOf("image" to base64String)

        firestore.collection("images").document(imageName)
            .set(imageMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 4. Fetch Image from Firestore and Display Context

    private fun fetchImageFromFirestore(imageName: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("images").document(imageName)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("image")) {
                    val base64String = document.getString("image") ?: ""
                    val bitmap = base64ToBitmap(base64String)
                    analyzeImage(bitmap) // Image analysis (ML Kit) ke liye
                } else {
                    Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 5. Analyze Image (using Google ML Kit)
// Yeha add kiya huu
    private fun analyzeImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val resultText = labels.joinToString(", ") { it.text }
                addMessageToChat("Image Analysis Result: $resultText", isUser = false)
            }
            .addOnFailureListener { e ->
                addMessageToChat("Error analyzing image: ${e.message}", isUser = false)
            }
    }

    // 6. Handle Image Selection (Camera/Gallery)
// Existing methods ke sath integrate kar raha hu
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> { // Camera Image
                    val bitmap = data?.extras?.get("data") as Bitmap
                    uploadImageToFirestore(bitmap, "camera_image")
                }
                101 -> { // Gallery Image
                    val uri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    uploadImageToFirestore(bitmap, "gallery_image")
                }
                102 -> { // File Picker (Optional)
                    val uri = data?.data
                    Toast.makeText(this, "Document Selected: $uri", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}


