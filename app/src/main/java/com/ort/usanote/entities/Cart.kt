package com.ort.usanote.entities

import android.util.Log
import com.google.firebase.firestore.*

class Cart (
    private var productItemList : MutableList<ProductItem>,
    var onChange : (Double, Int) -> Unit
) {
    private lateinit var db : FirebaseFirestore

    fun getProductItems () : MutableList<ProductItem> {
        return productItemList
    }

    fun notifyChange() {
        onChange(calculateSubtotal(), productItemList?.size)
    }

    fun addProductItem(productItem: ProductItem) {
        productItemList.add(productItem)
        notifyChange()
    }

    fun deleteProductItem(pos: Int) {
        db = FirebaseFirestore.getInstance()
        val productoActualizar = db.collection("productos").document(productItemList[pos].product.idProducto)
        val nuevoStock = productItemList[pos].quantity.toDouble()
        productoActualizar.update("stock",FieldValue.increment(nuevoStock))
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
        productItemList.removeAt(pos)
        notifyChange()
    }

    fun incrementProductQuantity(pos: Int, quantity: Int) {
        db = FirebaseFirestore.getInstance()
        val productoActualizar = db.collection("productos").document(productItemList[pos].product.idProducto)
        productoActualizar.update("stock", FieldValue.increment(-quantity.toDouble()))
        notifyChange()
    }

    fun calculateSubtotal() : Double {
        var subtotal = 0.0

        for (productItem in productItemList ) {
            subtotal += productItem.calculateSubtotal()
        }
        return subtotal
    }

    fun clear() {
        for(i in (productItemList.size - 1) downTo 0) {
            deleteProductItem(i)
        }
    }
}