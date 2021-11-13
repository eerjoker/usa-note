package com.ort.usanote.viewModels

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
    lateinit var msgErrorNombreApellido: String
    lateinit var msgErrorNumeros: String


    fun agregarDireccion(alias: String, nombre: String, calle: String, localidad: String, nro: String, piso: String, depto: String, provincia: String, codigoPostal: String){

        val user = auth.currentUser
        val dbDireccion: Direccion = Direccion(user!!.uid, alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)

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
        if (texto.isEmpty()){
            msgErrorGeneral = "Debe completar este campo"
        }else{
            textValido = true
        }
        return textValido
    }

    fun validateNombreApellido(nombre: String): Boolean{
        var nombreValido: Boolean = false
        val passwordRegex = Pattern.compile("^" + "([a-zA-ZÀ-ÿ\\s]{1,40})" + "$")

        if (nombre.isEmpty()){
            msgErrorNombreApellido = "debe completar este campo"
        }else if (!passwordRegex.matcher(nombre).matches()){
            msgErrorNombreApellido = "Solo se permiten letras"
        }else{
            nombreValido = true
        }
        return nombreValido
    }

    fun validateNumeros(nombre: String): Boolean{
        var nombreValido: Boolean = false
        val passwordRegex = Pattern.compile("^" + "([0-9]{1,10})" + "$")

        if (nombre.isEmpty()){
            msgErrorNumeros = "debe completar este campo"
        }else if (!passwordRegex.matcher(nombre).matches()){
            msgErrorNumeros = "Solo se permiten letras"
        }else{
            nombreValido = true
        }
        return nombreValido
    }

    fun validateForm(alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean ): Boolean{
        val result = arrayOf(alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)
        if (false in result){
            return false
        }
        return true
    }
}