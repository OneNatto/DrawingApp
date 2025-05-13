package com.example.drawingapp.ui.wordChain.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.tanh

enum class AuthMode(val title: String) {
    LOGIN("ログイン"), REGISTER("登録")
}

class AuthViewModel : ViewModel() {
    // 認証モード
    private val _authMode = MutableStateFlow(AuthMode.LOGIN)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    // ユーザーID
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    // パスワード
    private val _userPassword = MutableStateFlow("")
    val userPassword: StateFlow<String> = _userPassword.asStateFlow()

    // 共有のIntentを作成
    private val _sendEvent = MutableSharedFlow<Unit>()
    val sendEvent: SharedFlow<Unit> = _sendEvent.asSharedFlow()

    fun sendIntent() {
        viewModelScope.launch {
            _sendEvent.emit(Unit)
        }
    }

    val auth = FirebaseAuth.getInstance()

    fun setUserId(value: String) {
        _userId.value = value
    }

    fun setUserPassword(value: String) {
        _userPassword.value = value
    }

    fun sendAuth() {
        val id = _userId.value
        val password = _userPassword.value
        if (_authMode.value == AuthMode.LOGIN) {
            signIn(id, password)
        } else {
            signUp(id, password)
        }
    }

    fun signIn(id: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(id, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userId.value = ""
                    _userPassword.value = ""
                } else {
                    _userId.value = task.exception?.message.toString()
                }
            }
        } catch (e: Exception) {
            Log.d("signInWithEmailAndPassword", e.toString())
        }
    }

    fun signUp(id: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(id, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userId.value = ""
                    _userPassword.value = ""
                } else {
                    _userId.value = task.exception?.message.toString()
                }
            }
        } catch (e: Exception) {
            Log.d("createUserWithEmailAndPassword", e.toString())
        }
    }

    fun switchAuthModel() {
        if (_authMode.value == AuthMode.LOGIN) {
            _authMode.value = AuthMode.REGISTER
        } else {
            _authMode.value = AuthMode.LOGIN
        }
    }
}