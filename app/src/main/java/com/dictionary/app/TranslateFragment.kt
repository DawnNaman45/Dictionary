package com.dictionary.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment // This is the key change
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

class TranslateFragment : Fragment() { // Corrected to extend Fragment

    private lateinit var recyclerView: RecyclerView
    private lateinit var wordAdapter: WordAdapter
    private lateinit var editTextWord: EditText
    private lateinit var buttonSearch: Button
    private lateinit var sourceLanguageSpinner: Spinner
    private lateinit var targetLanguageSpinner: Spinner

    // Use onCreateView to inflate the layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    // Use onViewCreated to find views and set up listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The MainActivity's toolbar handles navigation, so these are no longer needed
        // setSupportActionBar(findViewById(R.id.toolbar))
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // supportActionBar?.title = "Translator"

        recyclerView = view.findViewById(R.id.recyclerView)
        editTextWord = view.findViewById(R.id.editTextWord)
        buttonSearch = view.findViewById(R.id.buttonSearch)
        sourceLanguageSpinner = view.findViewById(R.id.sourceLanguageSpinner)
        targetLanguageSpinner = view.findViewById(R.id.targetLanguageSpinner)

        recyclerView.layoutManager = LinearLayoutManager(context)
        wordAdapter = WordAdapter(emptyList())
        recyclerView.adapter = wordAdapter

        val languages = getSupportedLanguages()
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceLanguageSpinner.adapter = adapter
        targetLanguageSpinner.adapter = adapter

        buttonSearch.setOnClickListener {
            val query = editTextWord.text.toString().trim()
            if (query.isNotEmpty()) {
                val fromLanguageName = sourceLanguageSpinner.selectedItem.toString()
                val toLanguageName = targetLanguageSpinner.selectedItem.toString()
                getGeminiTranslationResponse(query, fromLanguageName, toLanguageName)
            } else {
                Toast.makeText(context, "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This method is for Activities, so it is no longer needed in the Fragment
    // override fun onSupportNavigateUp(): Boolean {
    //     onBackPressedDispatcher.onBackPressed()
    //     return true
    // }

    private fun getSupportedLanguages(): List<String> {
        return listOf("English", "Spanish", "French", "German", "Japanese", "Chinese", "Arabic", "Hindi", "Italian")
    }

    private fun getGeminiTranslationResponse(query: String, fromLanguage: String, toLanguage: String) {
        val model = Firebase.ai.generativeModel(
            modelName = "gemini-2.5-flash"
        )
        val prompt = """
            Translate the following text from $fromLanguage to $toLanguage.
            Text to translate: "$query"
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
                    wordAdapter.updateData(listOf(response.text ?: "Translation failed."))
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Gemini API error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}