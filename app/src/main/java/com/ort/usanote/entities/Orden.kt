package com.ort.usanote.entities

import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

class Orden(
    var numeroOrden: Int, var cantProductos: Int, var subtotal: Double,
    var total: Double, var entregadoA: String?, var envio: Envio?, var direccionEntrega: String,
    var idUsuario: String):
    Serializable {
    @ServerTimestamp
    var fecha: Date? = null
    constructor():this(0,0,0.0,0.0, "", null,
    "", ""){
    }

}