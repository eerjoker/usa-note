package com.ort.usanote.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Envio (
    var minutosEstimados : Int, var tipoEnvio: String, var costoEnvio: Double):
    Parcelable {

        constructor():this (0, "", 0.0)
}