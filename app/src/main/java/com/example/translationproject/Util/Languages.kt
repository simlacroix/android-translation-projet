package com.example.translationproject.Util

import java.util.Locale

enum class Languages(val localeValue: Locale) {
    English(Locale.ENGLISH),
    French(Locale.FRENCH),
    Italian(Locale.ITALIAN),
    Japanese(Locale.JAPANESE),
    German(Locale.GERMAN),
    Spanish(Locale("es","")),
}

fun codeToName(code: String): Languages? = when (code) {
    Languages.English.localeValue.language -> Languages.English
    Languages.French.localeValue.language -> Languages.French
    Languages.German.localeValue.language -> Languages.German
    Languages.Japanese.localeValue.language -> Languages.Japanese
    Languages.Italian.localeValue.language -> Languages.Italian
    Languages.Spanish.localeValue.language -> Languages.Spanish
    else -> null
}