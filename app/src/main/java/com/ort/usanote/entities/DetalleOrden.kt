package com.ort.usanote.entities

class DetalleOrden (
    var product : Product,
    var quantity : Int
) {
    private lateinit var idOrden : String

    fun calculateSubtotal() : Double {
        return this.product.price * this.quantity.toDouble()
    }
}