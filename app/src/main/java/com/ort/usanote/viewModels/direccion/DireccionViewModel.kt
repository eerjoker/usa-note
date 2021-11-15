package com.ort.usanote.viewModels.direccion

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.ort.usanote.entities.Direccion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import java.util.regex.Pattern

class DireccionViewModel : ViewModel() {
    val db = Firebase.firestore
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var direccionExitosa = MutableLiveData<Boolean>()
    lateinit var msgErrorGeneral: String
    lateinit var msgErrorGeneralChicos: String
    lateinit var msgErrorNombreApellido: String
    lateinit var msgErrorCP: String
    lateinit var msgErrorNumeros: String

    fun agregarDireccion(alias: String, nombre: String, calle: String, localidad: String, nro: String, piso: String, depto: String, provincia: String, codigoPostal: String){

        val user = auth.currentUser

        val dbDireccion: Direccion = Direccion(user!!.uid, depto, piso, nro, alias, localidad, codigoPostal, provincia, nombre, calle)

        db.collection("direcciones").add(dbDireccion)
            .addOnCompleteListener(){
                if (it.isSuccessful){
                    db.collection("usuarios").document(user.uid).update("direcciones", FieldValue.arrayUnion(
                        it.getResult()?.id)).addOnCompleteListener(){
                        if (it.isSuccessful){
                            direccionExitosa.value = true
                            Log.d("Auth", "Se pudo guardar en la BD de direcciones")
                        }else{
                            direccionExitosa.value = false
                            Log.d("Auth", "No se ha podido guardar en la BD de direcciones")
                        }

                    }
                    Log.d("Auth", "Se pudo registrar en el usuario")
                }else{
                    direccionExitosa.value = false
                    Log.d("Auth", "No se ha podido guardar en usuarios")
                }
            }


    }

    fun validateGenerales(texto: String): Boolean {

        var textValido: Boolean = false
        val generaldRegex = Pattern.compile("^" + "([a-zA-ZÀ-ÿ0-9\\s]{3,20})" + "$")


        if (texto.isEmpty()) {
            msgErrorGeneral = "requerido"
        } else if(!generaldRegex.matcher(texto).matches()){
            msgErrorGeneral = "Se permiten entre 3 y 20 caracteres"
        } else{
            textValido = true
        }
        return textValido
    }

    fun validateGeneralesChicos(texto: String): Boolean {

        var textValido: Boolean = false
        val generaldRegex = Pattern.compile("^" + "([a-zA-ZÀ-ÿ0-9\\s]{1,10})" + "$")


        if (texto.isEmpty()) {
            msgErrorGeneralChicos = "requerido"
        } else if(!generaldRegex.matcher(texto).matches()){
            msgErrorGeneralChicos = "Máximo 5 caracteres"
        } else{
            textValido = true
        }
        return textValido
    }

    fun validateNombreApellido(nombre: String): Boolean{
        var nombreValido: Boolean = false
        val passwordRegex = Pattern.compile("^" + "([a-zA-ZÀ-ÿ\\s]{3,20})" + "$")

        if (nombre.isEmpty()){
            msgErrorNombreApellido = "requerido"
        }else if (!passwordRegex.matcher(nombre).matches()){
            msgErrorNombreApellido = "Solo se permiten letras con un máximo de 3 a 20"
        }else{
            nombreValido = true
        }
        return nombreValido
    }

    fun validateCodigoPostal(nombre: String): Boolean{
        var cpValido: Boolean = false
        val cpRegex = Pattern.compile("^" + "([0-9]{4})" + "$")

        if (nombre.isEmpty()){
            msgErrorCP = "requerido"
        }else if (!cpRegex.matcher(nombre).matches()){
            msgErrorCP = "Solo se permiten 4 dígitos"
        }else{
            cpValido = true
        }
        return cpValido
    }

    fun validateNumeros(nombre: String): Boolean{
        var numeroValido: Boolean = false
        val numeroRegex = Pattern.compile("^" + "([0-9]{1,5})" + "$")

        if (nombre.isEmpty()){
            msgErrorNumeros = "requerido"
        }else if (!numeroRegex.matcher(nombre).matches()){
            msgErrorNumeros = "Solo se permiten números"
        }else{
            numeroValido = true
        }
        return numeroValido
    }

    fun validateForm(alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean ): Boolean{
        val result = arrayOf(alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)
        if (false in result){
            return false
        }
        return true
    }
}