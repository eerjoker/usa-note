package com.ort.usanote.viewModels.user

import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.ort.usanote.entities.Direccion
import com.ort.usanote.entities.Usuario
import java.util.regex.Pattern

class UserViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var direccionesUser = MutableLiveData<MutableList<Direccion>??>()
    var idDireccionesUser = MutableLiveData<MutableList<String>>()
    var userDb = MutableLiveData<Usuario?>()

    fun getDirecciones() {

        db.collection("direcciones").get().addOnCompleteListener() {
            if (it.isSuccessful) {
                var auxCollection = mutableListOf<Direccion>()
                var auxIds = mutableListOf<String>()
                for (document in it.result!!.documents) {
                    if (document["userId"] == auth.uid) {
                        var dirAux: Direccion
                        dirAux = document.toObject()!!
                        auxCollection.add(dirAux)
                        auxIds.add(document.id)
                    }
                }
                direccionesUser.value = auxCollection.toMutableList()
                idDireccionesUser.value = auxIds
            }
        }

    }

    fun getUser() {

        db.collection("usuarios").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it != null) {
                userDb.value = it.toObject<Usuario>()
            } else {
                userDb.value = null
            }
        }
    }

    fun updateUser(field: String, value: String) {

        db.collection("usuarios").document(auth.currentUser!!.uid).update(field, value)
            .addOnSuccessListener {
            }
    }

    fun updateEmail(newEmail: String){
        auth.currentUser?.updateEmail(newEmail)
    }


    fun validateEmail(email: String): String? {

        var msgErrorEmail: String? = null

        if (email.isEmpty()) {
            msgErrorEmail = "Debe completar su email"
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            msgErrorEmail = "Debe completar un email valido"
        }
        return msgErrorEmail

    }

    fun validateNombreApellido(valor: String): String?{

        var msgErrorGeneral: String? = null
        val passwordRegex = Pattern.compile("^" + "([a-zA-ZÀ-ÿ]{3,20})" + "$")

        if (valor.isEmpty()){
            msgErrorGeneral = "Debe completar el campo"
        }else if (!passwordRegex.matcher(valor).matches()){
            msgErrorGeneral = "Solo se permiten letras con un máximo de 3 a 20"
        }

        return msgErrorGeneral
    }

    fun validateTelefono(telefono: String): String? {
        var msgErrorTelefono: String? = null

        if (telefono.isEmpty()){
            msgErrorTelefono = "Debe completar el campo"
        }else if (telefono.length != 10){
            msgErrorTelefono = "Debe ser un telefono de 10 dígítos incluyendo el código de área"
        }

        return msgErrorTelefono
    }


}
