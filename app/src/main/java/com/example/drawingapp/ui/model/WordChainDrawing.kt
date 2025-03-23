package com.example.drawingapp.ui.model

import androidx.compose.ui.graphics.Path
import com.google.firebase.firestore.PropertyName

// import kotlinx.serialization.SerialName
// import kotlinx.serialization.Serializable

// @Serializable
data class WordChainDrawing(
    val answer :String = "",
    val drawing:List<Path>,
    @get:PropertyName("user_id") @set:PropertyName("user_id") var userId: String = ""
)