# android-translation-projet
## Name
Translator

## Description
The Android client is entirely coded in Kotlin, utilizing the Jetpack Compose library for building graphical interfaces. Several other libraries have been employed in this project, including:

Koin: Used for dependency injection.

Retrofit: Employed for handling all REST API calls.

CameraX: Utilized to access and use the device camera.

ML Kit Text Recognition: Integrated for text recognition in photos.

The Android client operates as follows:

Users can perform anonymous text translations. The source language can be automatically detected, and the text can be translated accurately into the chosen destination language(s). Users can select multiple destination languages for simultaneous translations. Additionally, users have the option to use the microphone for speech translation using SpeechRecognizer. Another feature allows users to capture text through photos, which is then detected and translated.

Users can choose to create an account and log in. By doing so, the translation history of that user is saved to their account, and they can review it in a table format. The translations in the history can be favorited, in the case where it is a sentence that often needs to be remembered. The favorite translations will always apppear on top of the history list. Users also have the option to log out and either connect to another account or continue making anonymous translations.

## Visuals
<img src="https://github.com/simlacroix/android-translation-projet/assets/47335007/5a97fb24-91a7-42f3-8eb4-43f95e9c991c" width="300">
<img src="https://github.com/simlacroix/android-translation-projet/assets/47335007/e5538b5d-3807-4902-b9e7-0189dfbc94ac" width="300">

### App Demo
[App Demo Video](https://github.com/simlacroix/android-translation-projet/assets/47335007/6e085993-c619-4d1a-824f-d2e95941775c)

### Camera Text Detection Demo
[Camera Text Detection Demo Video](https://github.com/simlacroix/android-translation-projet/assets/47335007/2b45aa23-7152-48d4-b918-b5c5e0a47b80)

App Icon from https://commons.wikimedia.org/wiki/File:Logo_traduction_vert.svg

## Personal Note (Simon Lacroix)
I saw this project as an opportunity to improve my skills as an Android developer by experimenting with many different technologies used in the industry today. For example, the usage of peripherals like the camera and the microphone, getting input from those directly into the app. The usage of Koin and Retrofit in this project helped me master them to have well functionning and structured code. The usage of Jetpack Compose and the NavController class let me experiment with making an app with a user-friendly and appealing UI with smooth navigation and transitions.

## Usage
Azure Api key is needed to properly run backend, Azure AI Translator API is used for the translations.
Backend needs to be executed for the clients to function properly.

## Authors and acknowledgment
- Simon Lacroix
- Antoine Toutant
- Mattéo Firrone
- Gwenn Durpoix-Espinasson
- Maxime Edeline

## Project status
Development complete for now, possibility of modification for improvement of code quality, better UI or minor features
