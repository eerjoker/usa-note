package com.ort.usanote.entities

import java.io.Serializable
import java.util.*

class Envio (
    var minutosEstimados : Int, var tipoEnvio: String, var costoEnvio: Double):
    Serializable {

        constructor():this (0, "", 0.0)
}