package com.dictionary.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonDictionary: Button = view.findViewById(R.id.buttonDictionary)
        val buttonTranslate: Button = view.findViewById(R.id.buttonTranslate)
        val buttonAIAssistant: Button = view.findViewById(R.id.buttonAIAssistant)

        buttonDictionary.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DictionaryFragment())
                .addToBackStack(null)
                .commit()
        }

        buttonTranslate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TranslateFragment())
                .addToBackStack(null)
                .commit()
        }

        buttonAIAssistant.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AIAssistantFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}