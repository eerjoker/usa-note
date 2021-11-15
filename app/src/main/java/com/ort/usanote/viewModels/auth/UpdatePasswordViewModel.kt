package com.ort.usanote.viewModels.auth

import android.content.ContentValues
import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val updateExitoso = MutableLiveData<Boolean>()
    lateinit var msgErrorEmail: String


    fun recuperar(email: String){

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(){
            if (it.isSuccessful){
                updateExitoso.value = true
                Log.d(ContentValues.TAG, "updateExitoso completado")
            }else{
                updateExitoso.value = false
                Log.d(ContentValues.TAG, "No se completo el updateExitoso")
            }
        }
    }

    fun validateEmail(email: String): Boolean {
        var emailValido: Boolean = false
        if (email.isEmpty()){
            msgErrorEmail = "Debe completar su email"
        }else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            msgErrorEmail = "Debe completar un email valido"
        }else{
            emailValido = true
        }
        return emailValido
    }

    fun validateUpdate (email: Boolean): Boolean{
        val result = arrayOf(email)

        if (false in result){
            return false
        }
        return true
    }
}