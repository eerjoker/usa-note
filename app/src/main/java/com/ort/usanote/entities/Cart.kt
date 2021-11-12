package com.ort.usanote.entities

import android.util.Log
import com.google.firebase.firestore.*

class Cart (
    private var detalleOrdenList : MutableList<DetalleOrden>,
    var onChange : (Double, Int) -> Unit
) {
    private lateinit var db : FirebaseFirestore

    fun getProductItems () : MutableList<DetalleOrden> {
        return detalleOrdenList
    }

    fun notifyChange() {
        onChange(calculateSubtotal(), detalleOrdenList?.size)
    }

    fun addProductItem(detalleOrden: DetalleOrden) {
        detalleOrdenList.add(detalleOrden)
        notifyChange()
    }

    fun deleteProductItem(pos: Int) {
        db = FirebaseFirestore.getInstance()
        val productoActualizar = db.collection("productos").document(detalleOrdenList[pos].product.idProducto)
        val nuevoStock = detalleOrdenList[pos].quantity.toDouble()
        productoActualizar.update("stock",FieldValue.increment(nuevoStock))
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
        detalleOrdenList.removeAt(pos)
        notifyChange()
    }

    fun incrementProductQuantity(pos: Int, quantity: Int) {
        db = FirebaseFirestore.getInstance()
        val productoActualizar = db.collection("productos").document(detalleOrdenList[pos].product.idProducto)
        productoActualizar.update("stock", FieldValue.increment(-quantity.toDouble()))
        notifyChange()
    }

    fun calculateSubtotal() : Double {
        var subtotal = 0.0

        for (productItem in detalleOrdenList ) {
            subtotal += productItem.calculateSubtotal()
        }
        return subtotal
    }

    fun clear() {
        for(i in (detalleOrdenList.size - 1) downTo 0) {
            deleteProductItem(i)
        }
    }
}