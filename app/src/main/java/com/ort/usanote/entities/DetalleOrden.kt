package com.ort.usanote.entities

class DetalleOrden(
    idOrden: String,
    producto: Product,
    quantity: Int
) {
    var producto: Product
    var quantity: Int
    var idOrden: String

    constructor(): this ("", Product(), 0)
    init {
        this.idOrden = idOrden
        this.producto = producto
        this.quantity = quantity
    }

    fun calculateSubtotal() : Double {
        return this.producto.price * this.quantity.toDouble()
    }
}