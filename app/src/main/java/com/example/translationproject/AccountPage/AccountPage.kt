package com.example.translationproject.AccountPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.translationproject.R
import com.example.translationproject.Util.getString
import com.example.translationproject.Util.observeLifecycleEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountPage(onBackPressed: () -> Unit, viewModel: AccountPageViewModel = koinViewModel()) {
    viewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)
    Column(modifier = Modifier.padding(10.dp))
    {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "From photo",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .background(color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = viewModel.errorMessages.isNotBlank(),
                modifier = Modifier.padding(5.dp)
            ) {
                Text(text = viewModel.errorMessages, color = Color.Red)
            }
            if (viewModel.loggedIn)
                LoggedIn(viewModel)
            else if (viewModel.registering)
                SignUp(viewModel)
            else
                LogIn(viewModel)
        }
    }
}

@Composable
fun LoggedIn(viewModel: AccountPageViewModel) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = "Welcome ${viewModel.email}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.logout() }) {
            Text(text = viewModel.getString(R.string.log_out))
        }
    }
}

@Composable
fun LogIn(viewModel: AccountPageViewModel) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = viewModel.getString(R.string.log_in_account))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.email,
            textStyle = TextStyle(color = Color.Black),
            label = { Text(text = viewModel.getString(R.string.email), color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.email = it })
        OutlinedTextField(
            value = viewModel.password,
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = viewModel.getString(R.string.password), color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.password = it })
        Button(onClick = { viewModel.login() }) {
            Text(text = viewModel.getString(R.string.log_in))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = viewModel.getString(R.string.no_account))
            TextButton(onClick = {
                viewModel.registering = true
                viewModel.errorMessages = ""
            }) {
                Text(text = viewModel.getString(R.string.create_account))
            }
        }
    }
}

@Composable
fun SignUp(viewModel: AccountPageViewModel) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = "Create your account")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.email,
            textStyle = TextStyle(color = Color.Black),
            label = { Text(text = viewModel.getString(R.string.email), color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.email = it })
        OutlinedTextField(
            value = viewModel.password,
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = viewModel.getString(R.string.password), color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.password = it })
        OutlinedTextField(
            value = viewModel.confirmPassword,
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(
                    text = viewModel.getString(R.string.confirm_password),
                    color = Color.Black
                )
            },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.confirmPassword = it })
        Button(
            onClick = { viewModel.register() },
            enabled = viewModel.passwordsMatch() && viewModel.password.isNotEmpty()
        ) {
            Text(text = viewModel.getString(R.string.sign_up))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = viewModel.getString(R.string.already_account))
            TextButton(onClick = {
                viewModel.registering = false
                viewModel.errorMessages = ""
            }) {
                Text(text = viewModel.getString(R.string.log_in))
            }
        }
    }
}

private class PasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            AnnotatedString("*".repeat(text.text.length)),
            OffsetMapping.Identity
        )
    }
}