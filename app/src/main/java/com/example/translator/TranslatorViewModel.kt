package com.example.translator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TranslatorViewModel : ViewModel() {

    // LiveData to hold the selected source and target languages
    private val _sourceLanguage = MutableLiveData<String>("en") // Default source language is English
    val sourceLanguage: LiveData<String> = _sourceLanguage

    private val _targetLanguage = MutableLiveData<String>("es") // Default target language is Spanish
    val targetLanguage: LiveData<String> = _targetLanguage


    // Function to set the source language
    fun setSourceLanguage(language: String) {
        _sourceLanguage.value = language
    }

    // Function to set the target language
    fun setTargetLanguage(language: String) {
        _targetLanguage.value = language
    }

    private val _translatedText = MutableLiveData<String>()
    val translatedText: LiveData<String> = _translatedText

    // Function to set the translated text
    fun setTranslatedText(text: String) {
        _translatedText.value = text
    }
}