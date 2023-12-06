package com.example.translationproject.Util

class SessionHolder {
    private var _token: String? = null
    private var _email: String? = null

    fun getToken(): String? = _token
    fun getEmail(): String? = _email
    fun storeNewSession(newToken: String?, newEmail: String?) {
        _token = newToken
        _email = newEmail
    }
    fun logout(){
        _token = null
        _email = null
    }
}