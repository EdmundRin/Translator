package com.example.translator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.translator.databinding.FragmentTranslationBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator


/**
 * A Fragment responsible for handling text translation.
 *
 * This Fragment allows users to enter text in an EditText field and automatically translates it
 * based on the selected source and target languages. It uses the ML Kit translator to perform
 * translations.
 */
class TranslationFragment : Fragment() {
    private lateinit var editTextTranslation: EditText
    private lateinit var viewModel: TranslatorViewModel
    private lateinit var translator: Translator
    private var _binding: FragmentTranslationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTranslationBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize UI components
        editTextTranslation = view.findViewById(R.id.editTextTranslation)

        // Initialize the translator
        val options = TranslatorOptions.Builder()
            .setSourceLanguage("en")
            .setTargetLanguage("de")
            .build()
        translator = Translation.getClient(options)
        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "DownloadModelIfNeeded success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "DownloadModelIfNeeded Failure", Toast.LENGTH_SHORT).show()
            }
        lifecycle.addObserver(translator)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TranslatorViewModel::class.java)

        // Set up a TextWatcher to detect text changes
        editTextTranslation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Handle before text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Perform translation here using the 'translator' instance
                translateText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Handle after text changed
            }
        })

        // Observe changes in the ViewModel's sourceLanguage propertyw
        viewModel.sourceLanguage.observe(viewLifecycleOwner, { sourceLanguage ->
            // Update the source language in the translation options
            translator = Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage)
                    .setTargetLanguage(viewModel.targetLanguage.value.toString()) // Get the target language from the ViewModel
                    .build()
            )
        })

        // Observe changes in the ViewModel's targetLanguage property
        viewModel.targetLanguage.observe(viewLifecycleOwner, { targetLanguage ->
            // Update the target language in the translation options
            translator = Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(viewModel.sourceLanguage.value.toString()) // Get the source language from the ViewModel
                    .setTargetLanguage(targetLanguage)
                    .build()
            )
        })
    }
    /**
     * Translates the provided text using the ML Kit translator and updates the ViewModel.
     *
     * This function takes the input text, translates it using the configured translator, and
     * updates the ViewModel's `translatedText` property with the translated result. It also
     * handles translation failures by displaying an error message.
     *
     * @param textToTranslate The text to be translated.
     */
    private fun translateText(textToTranslate: String) {
        // Use the ML Kit translator to translate the text
        translator.translate(textToTranslate)
            .addOnSuccessListener { translatedText ->
                viewModel.setTranslatedText(translatedText)
            }
            .addOnFailureListener { exception ->
                // Handle translation failure
                val errorMessage = "Translation failed: ${exception.message}"
                Log.e("TranslationFragment", errorMessage)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
    }
    /**
     * Performs cleanup when the view of this Fragment is being destroyed.
     *
     * This function is responsible for removing the translator from the Fragment's Lifecycle
     * to avoid memory leaks and closing the translator to release resources.
     */
    override fun onDestroyView() {
        // Remove the translator from the Fragment's Lifecycle to avoid leaks
        lifecycle.removeObserver(translator)
        translator.close()
        _binding = null
        super.onDestroyView()
    }
}