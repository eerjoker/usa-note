package com.ort.usanote.entities

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.DocumentId
import java.io.Serializable
import java.util.*

class Orden(
    var numeroOrden: Int,
    var cantProductos: Int,
    var subtotal: Double,
    var total: Double,
    var entregadoA: String?,
    var envio: Envio?,
    var direccionEntrega: String,
    var idUsuario: String):Serializable {
    @DocumentId
    lateinit var idOrden: String
    @ServerTimestamp
    var fecha: Date? = null
    constructor():this( 0,0,0.0,0.0, "", null,
    "", "")
}