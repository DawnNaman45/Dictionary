package com.dictionary.app

data class ChatMessage(
    val text: String,
    val isUser: Boolean // true if from user, false if from AI
)