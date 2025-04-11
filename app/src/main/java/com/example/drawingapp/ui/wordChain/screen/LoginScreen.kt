package com.example.drawingapp.ui.wordChain.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drawingapp.ui.wordChain.viewmodel.AuthMode
import com.example.drawingapp.ui.wordChain.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val authMode = viewModel.authMode.collectAsState()
    val userId = viewModel.userId.collectAsState()
    val userPassword = viewModel.userPassword.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        InputField(
            onValueChange = {
                viewModel.setUserId(it)
            },
            description = "ユーザー名",
            value = userId.value
        )
        InputField(
            onValueChange = {
                viewModel.setUserPassword(it)
            },
            description = "パスワード",
            value = userPassword.value
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = viewModel::sendAuth,
            modifier = Modifier.fillMaxWidth(0.7F)
        ) {
            Text(
                text = authMode.value.title
            )
        }
        Text(
            text = if (authMode.value == AuthMode.LOGIN) "アカウントを作成する" else "ログインはこちら",
            fontSize = 13.sp,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                viewModel.switchAuthModel()
            }
        )

    }
}

@Composable
fun InputField(
    onValueChange: (String) -> Unit,
    description: String = "",
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.8F)
    ) {
        Text(
            text = description,
            fontSize = 14.sp
        )
        TextField(
            onValueChange = onValueChange,
            value = value,
            modifier = Modifier.fillMaxWidth()
        )
    }
}