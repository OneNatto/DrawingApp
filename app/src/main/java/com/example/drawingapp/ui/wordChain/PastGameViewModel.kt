package com.example.drawingapp.ui.wordChain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.drawingapp.ui.model.WordChainDrawing
import com.example.drawingapp.utils.PathConverter
import com.example.drawingapp.utils.PathConverter.svgToPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PastGameViewModel:ViewModel() {
    // 書いた絵リスト
    private val _gameDrawing = MutableStateFlow<List<WordChainDrawing>>(emptyList())
    val gameDrawing: StateFlow<List<WordChainDrawing>> = _gameDrawing.asStateFlow()

    val database = FirebaseFirestore.getInstance()

    fun getGameDrawings(gameId: String) {
        // ここに過去のゲームリストを取得する処理を書く
        val subCollection =
            database.collection("word_chain_drawing").document(gameId).collection("drawings")
        subCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val stringList = document.data["drawing"] as List<String>
                    val pathList = stringList.map {
                        val data = svgToPath(it)
                        PathConverter.androidPathToComposePath(data)
                    }
                    val drawing = WordChainDrawing(
                        answer = document.data["answer"] as String,
                        drawing = pathList,
                        userId = document.data["user_id"] as String
                    )
                    val originalList = _gameDrawing.value.toMutableList()
                    originalList.add(drawing)
                    _gameDrawing.value = originalList
                }
            }
            .addOnFailureListener { Log.d("FirestoreDataGet", "取得失敗") }
    }
}