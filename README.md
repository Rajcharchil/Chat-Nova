ChatNova - AI-Powered Chatbot 🚀

ChatNova is an advanced AI chatbot application designed to provide seamless and intelligent conversations.
Powered by Google Gemini AI, it offers real-time responses, image generation, and smart interactions within an intuitive chat interface.

📌 Features
✅ AI-Powered Chat – Chat with an intelligent AI assistant using Google Gemini AI.
✅ Real-Time Conversations – Interactive and dynamic chat experience with a RecyclerView UI.
✅ Navigation Drawer – Easy-to-use Material Design Navigation Drawer for app sections.
✅ Smooth UI & Animations – Integrated dot-style progress indicator while AI responds.
✅ User Authentication – Signup, Login, and Forget Password functionality.
✅ Modern Android Design – Built with Jetpack Compose & XML layouts.

🔧 How It Works
1️⃣ User enters a message in the chat input.
2️⃣ The app sends the query to Gemini AI via API.
3️⃣ While waiting, a dot progress indicator is shown.
4️⃣ AI responds with text/image, displayed in the chat UI.
5️⃣ User can continue the conversation seamlessly.



🛠️ How ChatNova is Built?
🗂️ Project Structure

com.charchil.chatnova  
├── adapter  
│   ├── ChatAdapter.kt  (Handles RecyclerView chat UI)  
├── api  
│   ├── ApiInterface.kt  (API endpoint definitions)  
│   ├── ApiUtilities.kt  (Retrofit setup for API calls)  
│   ├── GeminiApiUtilities.kt  (Gemini AI integration)  
├── models  
│   ├── ChatMessage.kt  (Data model for chat messages)  
│   ├── ImageResponse.kt  (Data model for AI-generated images)  
├── activities  
│   ├── ChatActivity.kt  (Handles user input & AI responses)  
│   ├── ImageGenerateActivity.kt  (Handles AI-generated images)  
│   ├── LoginPage.kt  (User authentication)  
│   ├── Signup.kt  (User registration)  
│   ├── MainActivity.kt  (Navigation to chat)  
│   ├── SplashActivity.kt  (Startup screen)  
├── layouts  
│   ├── activity_chat.xml  (Main chat screen layout)  
│   ├── chatleftitem.xml  (Bot message bubble)  
│   ├── chatrightitem.xml  (User message bubble)  
│   ├── activity_main.xml  (Main screen layout)  
│   ├── nav_header.xml  (Navigation drawer header)


⚙️ Technologies Used
Google Gemini AI API (for AI-powered responses)
Retrofit (for network calls)
XML (for UI design)
RecyclerView (for chat history)
Firebase Authentication (for user login/signup)
Material Design Components (for modern UI)


🚀 How to Use ChatNova?
📥 Installation
1️⃣ Clone the repository:

git clone https://github.com/Rajcharchil/ChatNova.git  
2️⃣ Open the project in Android Studio.
3️⃣ Install dependencies and build the project.
4️⃣ Run the app on an Android emulator or real device.

📝 Usage
Sign up and log in to start chatting.
Type a message and send it to AI.
View AI-generated responses.
Use the navigation drawer for easy access.

🤝 Contributions & Support

Want to improve ChatNova? Contributions are welcome!
Fork the repo & submit a PR.
Report bugs or suggest features in Issues.
📩 Contact: rajcharchil555@gmail.com

📜 License
This project is open-source under the MIT License.
