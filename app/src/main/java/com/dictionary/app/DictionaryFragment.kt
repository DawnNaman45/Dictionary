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

class DictionaryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wordAdapter: WordAdapter
    private lateinit var editTextWord: EditText
    private lateinit var buttonSearch: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        editTextWord = view.findViewById(R.id.editTextWord)
        buttonSearch = view.findViewById(R.id.buttonSearch)

        recyclerView.layoutManager = LinearLayoutManager(context)
        wordAdapter = WordAdapter(emptyList())
        recyclerView.adapter = wordAdapter

        buttonSearch.setOnClickListener {
            val query = editTextWord.text.toString().trim()
            if (query.isNotEmpty()) {
                getGeminiDictionaryResponse(query)
            } else {
                Toast.makeText(context, "Please enter a word", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGeminiDictionaryResponse(query: String) {
        val model = Firebase.ai.generativeModel(
            modelName = "gemini-2.5-flash"
        )
        val prompt = """
            Act as a dictionary. For the word "$query", provide:
            Part of Speech: [part of speech]
            Definition: [definition]
            Example: [example sentence]
            If the word is not found, respond with "No result found".
        """.trimIndent()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = model.generateContent(
                    Content(
                        parts = listOf(
                            TextPart(prompt)
                        )
                    )
                )
                activity?.runOnUiThread {
                    val responseText = response.text ?: ""
                    if (responseText.contains("No result found")) {
                        wordAdapter.updateData(listOf(responseText))
                    } else {
                        val lines = responseText.split("\n")
                        wordAdapter.updateData(lines)
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Gemini API error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}