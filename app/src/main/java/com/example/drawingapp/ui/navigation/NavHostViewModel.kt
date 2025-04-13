package com.example.drawingapp.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavHostViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    private val _isLoggedIn = MutableStateFlow(currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val authListner = AuthStateListener {
        _isLoggedIn.value = it.currentUser != null
    }

    init {
        auth.addAuthStateListener(authListner)
    }

}