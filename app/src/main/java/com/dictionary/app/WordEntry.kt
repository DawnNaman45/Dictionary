
package com.dictionary.app

data class WordEntry(val word: String, val meaning: String, val example: String)

fun sampleWords() = listOf(
    WordEntry("abate", "to become less intense or widespread", "The storm finally abated after three hours."),
    WordEntry("benevolent", "well meaning and kindly", "A benevolent donor paid for the new library wing."),
    WordEntry("candid", "truthful and straightforward", "Be candid about your concerns."),
    WordEntry("daunt", "to make someone feel intimidated", "The scale of the task daunted him."),
    WordEntry("elated", "make (someone) ecstatically happy", "She was elated at the news."),
    WordEntry("futile", "incapable of producing any useful result", "Their attempts to negotiate proved futile."),
    WordEntry("gregarious", "fond of company; sociable", "He was a popular and gregarious man.")
)
