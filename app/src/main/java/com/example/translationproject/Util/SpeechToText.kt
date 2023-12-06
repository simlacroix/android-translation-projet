package com.example.translationproject.Util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechToText(context: Context) {
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    var speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private var currentlyListening = false

    init {
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ENABLE_LANGUAGE_DETECTION, true)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ENABLE_LANGUAGE_SWITCH, true)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        //Languages.values().asList().forEach { language -> speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.localeValue.language) }
    }

    fun setListeners(
        onBeginningOfSpeech: () -> Unit,
        onResult: (String) -> Unit,
        onError: () -> Unit
    ) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onBeginningOfSpeech() {
                currentlyListening = true
                onBeginningOfSpeech()
            }

            override fun onResults(results: Bundle?) {
                val data = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                currentlyListening = false
                onResult(data!![0])
            }

            override fun onError(error: Int) {
                onError()
                currentlyListening = false
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun startStopListening(languages: Languages) {
        if (currentlyListening)
            speechRecognizer.stopListening()
        else{
            val listeningIntent = speechRecognizerIntent
            listeningIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languages.localeValue.language)
            speechRecognizer.startListening(speechRecognizerIntent)
        }
    }
}