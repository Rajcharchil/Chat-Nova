package com.charchil.chatnova.models



data class ChatMessage(
    val text: String,
    val isUser: Boolean, // true -> User message, false -> AI message
    val isLoading : Boolean = false
)
