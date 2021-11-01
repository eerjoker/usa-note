package com.ort.usanote.entities

class ProductItem (
    var product : Product,
    var quantity : Int
) {

    fun calculateSubtotal() : Double {
        return this.product.price * this.quantity
    }
}