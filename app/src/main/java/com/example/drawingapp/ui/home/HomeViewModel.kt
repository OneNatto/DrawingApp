package com.example.drawingapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.drawingapp.ui.model.WordChainDrawing
import com.example.drawingapp.utils.PathConverter
import com.example.drawingapp.utils.PathConverter.svgToPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    // 書いた絵リスト
    private val _pastGameList = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val pastGameList: StateFlow<List<Pair<String, String>>> = _pastGameList.asStateFlow()

    val database = FirebaseFirestore.getInstance()

    init {
        getPastGameList()
    }

    fun getPastGameList() {
        // ここに過去のゲームリストを取得する処理を書く
        val collection =
            database.collection("word_chain_drawing")
        collection.get()
            .addOnSuccessListener { result ->
                // 取得したデータをMutableListに追加していく
                for (document in result) {
                    val originalList = _pastGameList.value.toMutableList()
                    val pair = Pair(document.id, document["first_letter"] as String)
                    originalList.add(pair)
                    _pastGameList.value = originalList
                }
            }
            .addOnFailureListener { Log.d("FirestoreDataGet", "取得失敗") }
    }
}