
package com.dictionary.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WordDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)

        val backButton = findViewById<Button>(R.id.backButton)
        val wordDetail = findViewById<TextView>(R.id.wordDetail)
        val meaningDetail = findViewById<TextView>(R.id.meaningDetail)
        val exampleDetail = findViewById<TextView>(R.id.exampleDetail)

        val word = intent.getStringExtra("word")
        val meaning = intent.getStringExtra("meaning")
        val example = intent.getStringExtra("example")

        wordDetail.text = word
        meaningDetail.text = "Meaning: $meaning"
        exampleDetail.text = "Example: $example"

        backButton.setOnClickListener { finish() }
    }
}
