package com.example.translationproject.AccountPage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translationproject.R
import com.example.translationproject.Repositories.AccountRepository
import com.example.translationproject.Repositories.RetrofitHelper
import com.example.translationproject.UiStates.Credentials
import com.example.translationproject.UiStates.LoginCredentials
import com.example.translationproject.Util.SessionHolder
import com.example.translationproject.Util.getString
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException


class AccountPageViewModel(private val sessionHolder: SessionHolder) : ViewModel(),
    DefaultLifecycleObserver {
    var loggedIn: Boolean by mutableStateOf(false)
    var registering: Boolean by mutableStateOf(false)

    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var confirmPassword: String by mutableStateOf("")

    var errorMessages: String by mutableStateOf("")

    private val accountRepository =
        RetrofitHelper.getInstance().create(AccountRepository::class.java)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        email = sessionHolder.getEmail() ?: ""
        if (!email.isBlank())
            loggedIn = true
    }

    fun passwordsMatch(): Boolean = password == confirmPassword
    fun login() {
        val credentials = LoginCredentials(email, password)
        viewModelScope.launch {
            val response = try {
                accountRepository.login(credentials)
            } catch (e: IOException){
                errorMessages =  getString(R.string.connexion_error)
                return@launch
            }
            if (response.isSuccessful) {
                loggedIn = true
                errorMessages = ""
                sessionHolder.storeNewSession(response.body()?.accessToken, email)
            } else {
                errorMessages = getString(R.string.wrong_credentials)
            }
        }
    }

    fun register() {
        val credentials = Credentials(email, password)
        viewModelScope.launch {
            val response = try {
                accountRepository.register(credentials)
            } catch (e: IOException){
                errorMessages = getString(R.string.connexion_error)
                return@launch
            }
            if (response.isSuccessful) {
                login()
                registering = false
                errorMessages = ""
            } else {
                val jObjError = JSONObject(response.errorBody()!!.string())
                val keys = jObjError.getJSONObject("errors").keys()
                val errors = mutableListOf<String>()
                keys.forEach { key ->
                    errors.add(
                        jObjError.getJSONObject("errors").getJSONArray(key).get(0).toString()
                    )
                }
                errorMessages = (errors.joinToString { "$it\n" }).replace(",","")
            }
        }
    }

    fun logout() {
        loggedIn = false
        sessionHolder.logout()
        password = ""
        confirmPassword = ""
    }

}