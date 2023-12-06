package com.example.translationproject

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.translationproject.StartPage.StartPage
import com.example.translationproject.ui.theme.InternetMobiliteProjetTheme
import android.Manifest
import com.example.translationproject.AccountPage.AccountPage
import com.example.translationproject.CameraPage.CameraPage

class MainActivity : ComponentActivity() {
    private var speechRecognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission()
        }



        setContent {
            InternetMobiliteProjetTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Navigation.START_PAGE.title
                ) {
                    composable(Navigation.START_PAGE.title) {
                        StartPage(
                            onNavigateToCamera = {
                                navController.navigate(Navigation.CAMERA_PAGE.title)
                            },
                            onNavigateToAccount = {
                                navController.navigate(Navigation.ACCOUNT_PAGE.title)
                            })
                    }
                    composable(Navigation.CAMERA_PAGE.title) {
                        CameraPage(onBackPressed = { navController.popBackStack() })
                    }
                    composable(Navigation.ACCOUNT_PAGE.title) {
                        AccountPage(onBackPressed = { navController.popBackStack() })
                    }
                }
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA),
                1
            )
        }
    }
}