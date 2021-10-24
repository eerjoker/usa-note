package com.ort.usanote.entities

import java.io.Serializable

class Usuario (var id: String?, var nombre: String, var apellido: String, var email: String, var telefono: String,
               var esCliente: Boolean, var direcciones:MutableList<String>?, var pedidos: MutableList<String>?):
    Serializable

{
    constructor():this("","","","","",true, null, null){

    }


}