package com.ort.usanote.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Direccion (var userId: String, var departamento: String, var piso: String, var numero: String, var alias: String, var localidad: String, var codigoPostal: String,
                 var provincia: String, var nombreCompleto: String, var calle: String): Parcelable
{
    constructor():this("","","","","","","","","","")

}