package com.ort.usanote.entities

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.*

class Cart (
    private var productItemList : MutableList<ProductItem>,
    var onChange : (Double) -> Unit
) {
    private lateinit var db : FirebaseFirestore


    fun getProductItems () : MutableList<ProductItem> {
        return productItemList
    }

    fun modifyProductItemQuantity(pos: Int, quantity: Int) {
        productItemList[pos].quantity = quantity
        onChange(calculateSubtotal())
    }

    fun addProductItem(productItem: ProductItem) {
        productItemList.add(productItem)
        onChange(calculateSubtotal())
    }

    fun deleteProductItem(pos: Int) {
        print(productItemList)

        db = FirebaseFirestore.getInstance()
        val productoActualizar = db.collection("productos").document(productItemList[pos].product.idProducto)
        val nuevoStock = productItemList[pos].product.stock
        print(nuevoStock)
        productoActualizar.update("stock",nuevoStock)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }

        productItemList.removeAt(pos)
        onChange(calculateSubtotal())
    }

    fun calculateSubtotal() : Double {
        var subtotal = 0.0

        for (productItem in productItemList ) {
            subtotal += productItem.calculateSubtotal()
        }
        return subtotal
    }
}