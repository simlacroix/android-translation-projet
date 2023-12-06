package com.example.translationproject

enum class Navigation(val title: String, val argument: String = "") {
    START_PAGE("startPage"),
    CAMERA_PAGE("cameraPage"),
    ACCOUNT_PAGE("accountPage")
}