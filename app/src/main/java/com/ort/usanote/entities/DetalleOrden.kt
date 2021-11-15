package com.ort.usanote.entities

import java.io.Serializable

class DetalleOrden( var idOrden : String, var producto: Product, var quantity: Int):
    Serializable {
        constructor(): this ("", Product(), 0)

    fun calculateSubtotal() : Double {
        return this.producto.price * this.quantity.toDouble()
    }
}