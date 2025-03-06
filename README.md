# 🌟 ChatNova - AI-Powered Chatbot

Welcome to **ChatNova** – An AI-powered chatbot application integrated with **Google Gemini AI** for seamless, smart conversations! 🚀

<img src="https://github.com/user-attachments/assets/c9fe87c6-f2c2-4b74-9c45-fab770a8423f" width="150">

---

## 🔥 Features

✅ **Gemini AI Integration** - Smart AI-powered chat responses.  
✅ **User-friendly Chat UI** - Interactive chat interface using RecyclerView.  
✅ **Navigation Drawer** - Smooth navigation for an enhanced user experience.  
✅ **Dot-Style Progress Indicator** - Animated loader for AI response wait time.  
✅ **Authentication System** - Google Sign-In for secure access.    
✅ **Dark Mode Support** - Enjoy chatting in dark mode for a comfortable experience.  
✅ **Lightweight & Optimized** - Fast and efficient app performance.  

---

## 📸 Interface

- **[Login Page](#)**  
- **[Signup Page](#)**  
- **[Chat Page](#)**  
- **[Profile Page](#)**  
- **[UPgrade Page](#)**  

| Splash Screen | Chat Screen | Login Page | SignUp Page |
|--------------|------------|------------|------------|
| <img src="https://github.com/user-attachments/assets/ce5c90da-084a-4754-8bc1-f09c4c186a2f" width="200" height="400"> | <img src="https://github.com/user-attachments/assets/63a2293e-be91-4579-a19c-af436aea23d5" width="200" height="400"> | <img src="https://github.com/user-attachments/assets/f163bb5a-be6a-42d7-9f03-9894a31d5bea" width="200" height="400"> | <img src="https://github.com/user-attachments/assets/b78d011c-824d-45c8-a3a5-e8ecf66f9a3d" width="200" height="400"> |

| Forget Page | Profile Page | Upgrade Page |
|------------|------------|------------|
| <img src="https://github.com/user-attachments/assets/8ca26856-38cd-4f2e-b491-27423e816cdf" width="200" height="400"> | <img src="https://github.com/user-attachments/assets/be9255ab-1c6c-4c66-a6d4-0b7735ecb151" width="200" height="400"> | <img src="https://github.com/user-attachments/assets/56f2a761-5f41-43a3-b990-df71d9cc2403" width="200" height="400"> |




## 🚀 Getting Started

Follow these steps to set up **ChatNova** locally:

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/Rajcharchil/ChatNova.git
cd ChatNova
```

### 2️⃣ Open in Android Studio

- Open **Android Studio**
- Select **"Open an existing project"**
- Navigate to the cloned `ChatNova` directory

### 3️⃣ Install Dependencies

Ensure you have the required dependencies in `build.gradle`:

```gradle
dependencies {
    implementation 'com.google.ai.client.generativeai:generativeai:0.4.0' // Gemini AI
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'com.google.android.material:material:1.7.0'
}
```

### 4️⃣ Setup Google Gemini API

1. Get your **Google Gemini API Key** from [Google AI](https://ai.google.com/)
2. Add it to `res/values/strings.xml`:

```xml
<string name="gemini_api_key">YOUR_GEMINI_API_KEY</string>
```

### 5️⃣ Run the App 🎉

Click **Run ▶️** in Android Studio or use:

```bash
gradlew assembleDebug
```

---

## 🎯 How It Works

1. **User Signs In** via Google Authentication.  
2. **Chat Activity Loads** with a RecyclerView-based chat UI.  
3. **AI Responds** using Gemini API for chat messages.  
4. **Progress Indicator** shows while waiting for AI responses.  
5. **User Can Generate Images** using the built-in AI image generator.  

---

## 🛠 Project Structure

```
com.charchil.chatnova
├── adapter
│   ├── ChatAdapter.kt
├── api
│   ├── ApiInterface.kt
│   ├── GeminiApiUtilities.kt
├── models
│   ├── ChatMessage.kt
│   ├── ImageResponse.kt
├── activities
│   ├── ChatActivity.kt
│   ├── ImageGenerateActivity.kt
│   ├── MainActivity.kt
│   ├── LoginPage.kt
│   ├── Signup.kt
├── layouts
│   ├── activity_chat.xml
│   ├── chatleftitem.xml
│   ├── chatrightitem.xml
│   ├── drawer_menu.xml
│   ├── loading_progress.xml
```

---

## 📌 Future Enhancements

- [ ] **Voice Input Support 🎙️**  
- [ ] **Multi-language AI Chat 🌍**  
- [ ] **Cloud-based Chat History ☁️**  
- [ ] **Real-time Chat Analytics 📊**  

---

## 👥 Contributors

Thanks to the following people who have contributed to this project:

- [**Amartya kaushik**](https://github.com/Amartyakaushik)  


Want to contribute? Feel free to submit a pull request! 🚀


### **Steps to Contribute:**

1. **Fork the repository**
2. **Create a new branch**: `git checkout -b feature-xyz`
3. **Commit changes**: `git commit -m 'Added new feature'`
4. **Push changes**: `git push origin feature-xyz`
5. **Open a Pull Request** 🚀

---

## 📜 License

This project is **MIT Licensed**. Feel free to use and modify it as needed.

---

## 🙌 Support

If you have any questions or need help, feel free to open an issue or reach out!

📧 **Contact:** rajcahrchil555@gmail.com

🌟 **Give this project a star if you like it! ⭐**
