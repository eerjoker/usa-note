package com.ort.usanote.entities

import java.io.Serializable

class DetalleOrden( var idOrden : String, var producto: Product?, var quantity: Int):
    Serializable {
        constructor(): this ("", null, 0)
}