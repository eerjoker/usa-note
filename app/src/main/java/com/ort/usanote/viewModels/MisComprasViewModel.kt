package com.ort.usanote.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.entities.Orden
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MisComprasViewModel : ViewModel() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getOrdenes(idUsuario: String) : MutableList<Orden> {
        var ordenList : MutableList<Orden> = mutableListOf()

        viewModelScope.launch {
            db.collection("ordenes")
                .whereEqualTo("idUsuario", idUsuario)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        for (orden in snapshot) {
                            ordenList.add(orden.toObject(Orden::class.java))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ERROR", "Couldn't get 'ordenes' list from database. Exception: ${exception.toString()}")
                }
        }
        return ordenList
    }
}