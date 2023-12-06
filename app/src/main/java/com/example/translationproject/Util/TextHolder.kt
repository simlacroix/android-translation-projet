package com.example.translationproject.Util

class TextHolder {
    private var _currentText: String = ""

    fun getLastReadText(): String = _currentText
    fun storeNewText(newText: String) {
        _currentText = newText
    }
}