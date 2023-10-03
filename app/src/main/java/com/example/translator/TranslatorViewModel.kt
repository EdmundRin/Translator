package com.example.translator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
/**
 * ViewModel class for managing translation-related data and functionality.
 *
 * This class provides LiveData objects to hold the selected source and target languages,
 * as well as the translated text. It also includes functions to set these values.
 *
 * By default, the source language is set to English ("en"), and the target language is set
 * to Spanish ("es").
 */
class TranslatorViewModel : ViewModel() {

    // LiveData to hold the selected source and target languages
    private val _sourceLanguage = MutableLiveData<String>("en") // Default source language is English
    val sourceLanguage: LiveData<String> = _sourceLanguage

    private val _targetLanguage = MutableLiveData<String>("de") // Default target language is German
    val targetLanguage: LiveData<String> = _targetLanguage


    /**
     * Sets the selected source language for translation.
     *
     * @param language The code representing the source language (e.g., "en" for English).
     */
    fun setSourceLanguage(language: String) {
        _sourceLanguage.value = language
    }

    /**
     * Sets the selected target language for translation.
     *
     * @param language The code representing the target language (e.g., "en" for English).
     */
    fun setTargetLanguage(language: String) {
        _targetLanguage.value = language
    }

    private val _translatedText = MutableLiveData<String>()
    val translatedText: LiveData<String> = _translatedText

    /**
     * Sets the translated text result.
     *
     * This method updates the LiveData object holding the translated text with the provided
     * text value.
     *
     * @param text The translated text to set.
     */
    fun setTranslatedText(text: String) {
        _translatedText.value = text
    }
}