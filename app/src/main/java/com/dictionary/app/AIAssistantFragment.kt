package com.dictionary.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.TextPart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AIAssistantFragment : Fragment() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: Button

    private val chatMessages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ai_assistant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        editTextMessage = view.findViewById(R.id.editTextMessage)
        buttonSendMessage = view.findViewById(R.id.buttonSendMessage)

        chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(chatMessages)
        chatRecyclerView.adapter = chatAdapter

        buttonSendMessage.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                addMessageToChat(messageText, true) // Add user message
                editTextMessage.text.clear()
                getGeminiResponse(messageText)
            }
        }
    }

    private fun addMessageToChat(text: String, isUser: Boolean) {
        val message = ChatMessage(text, isUser)
        chatMessages.add(message)
        chatAdapter.notifyItemInserted(chatMessages.size - 1)
        chatRecyclerView.scrollToPosition(chatMessages.size - 1)
    }

    private fun getGeminiResponse(query: String) {
        val model = Firebase.ai.generativeModel(
            modelName = "gemini-2.5-flash"
        )

        // Add a placeholder for the AI response immediately
        addMessageToChat("Typing...", false)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = model.generateContent(
                    Content(
                        parts = listOf(
                            TextPart(query)
                        )
                    )
                )
                withContext(Dispatchers.Main) {
                    val aiResponse = response.text ?: "No response from AI."
                    // Update the last message (the "Typing..." placeholder)
                    chatMessages[chatMessages.size - 1] = ChatMessage(aiResponse, false)
                    chatAdapter.notifyItemChanged(chatMessages.size - 1)
                    chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Update the last message with an error
                    chatMessages[chatMessages.size - 1] = ChatMessage("Error: ${e.message}", false)
                    chatAdapter.notifyItemChanged(chatMessages.size - 1)
                    chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                    Toast.makeText(context, "Gemini API error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}