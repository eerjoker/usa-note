package com.ort.usanote.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.entities.DetalleOrden
import com.ort.usanote.entities.Orden

class MisComprasDetalleOrdenViewModel : ViewModel() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getDetallesOrden(idOrden: String, cb: (MutableList<DetalleOrden>) -> Unit) {
        var detalleOrdenList : MutableList<DetalleOrden> = mutableListOf()

        db.collection("detalleDeOrdenes")
            .whereEqualTo("idOrden", idOrden)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (detalleOrden in snapshot) {
                        detalleOrdenList.add(detalleOrden.toObject(DetalleOrden::class.java))
                    }
                }
                cb(detalleOrdenList)
            }
            .addOnFailureListener { exception ->
                Log.d(
                    "ERROR",
                    "Couldn't get 'ordenes' list from database. Exception: ${exception.toString()}"
                )
            }
    }
}