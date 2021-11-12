package com.ort.usanote.entities

class DetalleOrden (
    var idOrden : String,
    var product : Product,
    var quantity : Int
) {

    fun calculateSubtotal() : Double {
        return this.product.price * this.quantity.toDouble()
    }
}