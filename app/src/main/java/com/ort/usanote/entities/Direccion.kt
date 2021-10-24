package com.ort.usanote.entities

class Direccion (var userId: String, var departamento: String, var piso: String, var numero: String, var alias: String, var localidad: String, var codigoPostal: String,
                 var provincia: String, var nombreCompleto: String, var calle: String)
{
    constructor():this("","","","","","","","","","")

}