package com.ort.usanote.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.ort.usanote.entities.Direccion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore

class DireccionViewModel : ViewModel() {
    val db = Firebase.firestore
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var direccionExitosa = MutableLiveData<Boolean>()
    lateinit var msgErrorGeneral: String


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

    fun validateForm(alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean ): Boolean{
        val result = arrayOf(alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)
        if (false in result){
            return false
        }
        return true
    }
}