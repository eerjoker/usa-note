package com.ort.usanote.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ort.usanote.entities.Direccion
import java.util.regex.Pattern

class UpdateDireccionViewModel : ViewModel() {
    val db = Firebase.firestore
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var actualizacionExitosa = MutableLiveData<Boolean>()
    var eliminacionExitosa = MutableLiveData<Boolean>()
    lateinit var msgErrorGeneral: String
    lateinit var msgErrorNombreApellido: String

    fun actualizarDireccion(id: String, alias: String, nombre: String, calle: String, localidad: String, nro: String, piso: String, depto: String, provincia: String, codigoPostal: String){

        val dbDireccion: Direccion = Direccion(user!!.uid, depto, piso, nro, alias, localidad, codigoPostal, provincia, nombre, calle)

        db.collection("direcciones").document(id).set(dbDireccion)
            .addOnCompleteListener(){
                if (it.isSuccessful){
                    actualizacionExitosa.value = true
                    Log.d("Auth", "Se pudo actualizar en la BD de direcciones")
                }else{
                    actualizacionExitosa.value = false
                    Log.d("Auth", "No se ha podido actualizar en la BD de direcciones")
                }
            }
    }

    fun eliminarDireccion(id: String){

        db.collection("direcciones").document(id).delete().addOnCompleteListener(){
            if (it.isSuccessful){
                eliminacionExitosa.value = true
                Log.d("Auth", "Se se ha podido eliminar en la BD de direcciones")
            }else{
                eliminacionExitosa.value = false
                Log.d("Auth", "No se ha podido eliminar en la BD de direcciones")
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

    fun validateForm(alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean ): Boolean{
        val result = arrayOf(alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)
        if (false in result){
            return false
        }
        return true
    }
}