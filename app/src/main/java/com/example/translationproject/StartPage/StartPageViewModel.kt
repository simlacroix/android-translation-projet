package com.example.translationproject.StartPage

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translationproject.R
import com.example.translationproject.Repositories.RetrofitHelper
import com.example.translationproject.Repositories.TraductionRepository
import com.example.translationproject.UiStates.HistoryItem
import com.example.translationproject.UiStates.Translation
import com.example.translationproject.Util.TextHolder
import com.example.translationproject.Util.Languages
import com.example.translationproject.Util.SpeechToText
import com.example.translationproject.Util.SessionHolder
import com.example.translationproject.Util.codeToName
import com.example.translationproject.Util.getString
import kotlinx.coroutines.launch
import java.io.IOException

class StartPageViewModel(
    private val speechToText: SpeechToText,
    private val textHolder: TextHolder,
    private val sessionHolder: SessionHolder,
) : ViewModel(), DefaultLifecycleObserver {
    var textToTranslate: String by mutableStateOf("")
    var translatedText: String by mutableStateOf("")
    var sourceLanguage: Languages? by mutableStateOf(Languages.English)
    var targetLanguages = mutableStateListOf<Languages?>()

    var currentlyListening: Boolean by mutableStateOf(false)

    var expandedSource by mutableStateOf(false)
    var expandedTarget by mutableStateOf(false)

    var translationHistory: Array<HistoryItem> by mutableStateOf(arrayOf())
    var isLoggedIn: Boolean by mutableStateOf(false)

    var showAlert: Boolean by mutableStateOf(false)
    var alertText: String by mutableStateOf("")
    var alertDismissButton: @Composable () -> Unit by mutableStateOf({})
    var alertOnConfirmClick: () -> Unit by mutableStateOf({})

    private val traductionRepository =
        RetrofitHelper.getInstance().create(TraductionRepository::class.java)

    init {
        speechToText.setListeners(
            onBeginningOfSpeech = { onBeginningOfSpeech() },
            onResult = { text -> onResults(text) },
            onError = { onError() })
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val cameraText = textHolder.getLastReadText()
        if (cameraText != "")
            textToTranslate = cameraText
        refreshHistory()
    }

    fun onTextChange(text: String) {
        textToTranslate = if (text == "...") "" else text
        textHolder.storeNewText(textToTranslate)
    }

    fun swap() {
        if (targetLanguages.size == 1 && sourceLanguage != null && translatedText != "...") {
            val ogSourceLang = sourceLanguage
            sourceLanguage = targetLanguages[0]
            textToTranslate = translatedText
            targetLanguages = mutableStateListOf(ogSourceLang)
            translate()
        }
    }

    private fun verifyLoggedIn(tokenCode: String?): Boolean =
        if (tokenCode.isNullOrBlank()) {
            isLoggedIn = false
            isLoggedIn
        } else {
            isLoggedIn = true
            isLoggedIn
        }

    private fun refreshHistory() {
        val tokenCode = sessionHolder.getToken()
        if (verifyLoggedIn(tokenCode)) {
            viewModelScope.launch {
                translationHistory = try {
                    val token = "Bearer $tokenCode"
                    traductionRepository.history(token).reversed().sortedBy { !it.isFavorite }
                        .toTypedArray()
                } catch (e: IOException) {
                    translationHistory
                }
            }
        } else {
            return
        }
    }

    private fun connexionErrorDialog(){
        alertText = getString(R.string.connexion_error)
        showAlert = true
        alertDismissButton = {}
        alertOnConfirmClick = { showAlert = false }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            try{
                traductionRepository.clearHistory("Bearer ${sessionHolder.getToken()}")
                refreshHistory()
            }catch(e : IOException){
                connexionErrorDialog()
            }
        }
    }

    fun askClearHistory() {
        alertText = getString(R.string.clear_history)
        showAlert = true
        alertDismissButton = {
            Button(onClick = { showAlert = false }) {
                Text(text = getString(R.string.cancel))
            }
        }
        alertOnConfirmClick = {
            clearHistory()
            showAlert = false
        }
    }

    fun toggleFavorite(item: HistoryItem) {
        viewModelScope.launch {
            try{
                traductionRepository.favoriteTranslation(
                    "Bearer ${sessionHolder.getToken()}",
                    item.id,
                    !item.isFavorite
                )
                refreshHistory()
            }catch (e: IOException){
                connexionErrorDialog()
            }
        }
    }

    fun translate() {
        if (targetLanguages.isNotEmpty() && textToTranslate.isNotEmpty()) {
            val translation = Translation(
                sourceLanguage?.localeValue?.language ?: "",
                targetLanguages.map { it?.localeValue?.language!! },
                textToTranslate
            )
            translatedText = "..."
            viewModelScope.launch {
                val translationResult = try {
                    val token = if (sessionHolder.getToken().isNullOrBlank())
                        ""
                    else
                        "Bearer ${sessionHolder.getToken()}"
                    traductionRepository.translate(translation, token)
                } catch (e: IOException) {
                    translatedText = ""
                    connexionErrorDialog()
                    return@launch
                }
                if (translationResult.translations.size == 1) {
                    translatedText = translationResult.translations[0].text
                } else {
                    translatedText = ""
                    translationResult.translations.forEach { trans ->
                        translatedText += "${trans.to}: ${trans.text} \n"
                    }
                }
                if (sourceLanguage == null && translationResult.detectedLanguage != null) {
                    sourceLanguage = codeToName(translationResult.detectedLanguage.language)
                }
                refreshHistory()
            }
        }
    }

    fun handleListenClick() {
        if (sourceLanguage != null)
            speechToText.startStopListening(sourceLanguage!!)
    }

    fun changeSourceLanguage(language: Languages?) {
        sourceLanguage = language
        expandedSource = false
    }

    fun changeTargetLanguage(language: Languages?) {
        if (targetLanguages.contains(language) && targetLanguages.size > 1)
            targetLanguages.remove(language)
        else if (!targetLanguages.contains(language))
            targetLanguages.add(language)
    }

    private fun onBeginningOfSpeech() {
        currentlyListening = true
        textToTranslate = "..."
    }

    private fun onResults(result: String) {
        currentlyListening = false
        textToTranslate = result
    }

    private fun onError() {
        currentlyListening = false
    }
}