package com.example.translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
/**
 * The main activity of the translation app.
 *
 * This activity is responsible for managing user interactions and displaying translated text.
 * It sets up listeners for radio buttons to select source and target languages and updates
 * the ViewModel accordingly.
 */
class MainActivity : AppCompatActivity(){
    private lateinit var viewModel: TranslatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(TranslatorViewModel::class.java)

        val sourceRadioGroup = findViewById<RadioGroup>(R.id.sourceRadioGroup)
        val targetRadioGroup = findViewById<RadioGroup>(R.id.translationRadioGroup)
        val translatedTextView = findViewById<TextView>(R.id.translationTextView)

        // Set up listeners to update the ViewModel with selected source and target languages
        sourceRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.enSourceRadioButton -> viewModel.setSourceLanguage("en")
                R.id.esSourceRadioButton -> viewModel.setSourceLanguage("es")
                R.id.gerSourceRadioButton -> viewModel.setSourceLanguage("de")
            }
        }

        targetRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.esTranslationRadioButton -> viewModel.setTargetLanguage("es")
                R.id.gerTranslationRadioButton -> viewModel.setTargetLanguage("de")
                R.id.enTranslationRadioButton -> viewModel.setTargetLanguage("en")
            }
        }
        sourceRadioGroup.check(R.id.enSourceRadioButton)
        targetRadioGroup.check(R.id.gerTranslationRadioButton)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TranslationFragment())
                .commit()
        }
        viewModel.translatedText.observe(this, { translatedText ->
            // Update the TextView with the translated text
            translatedTextView.text = translatedText
        })

    }
}