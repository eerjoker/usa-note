package com.ort.usanote.viewModels.auth

import android.content.ContentValues
import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val loginExitoso = MutableLiveData<Boolean>()
    lateinit var msgErrorEmail: String
    lateinit var msgErrorPassword: String

    fun ingresar (email: String, password: String){

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if (it.isSuccessful){
                    loginExitoso.value = true
                    Log.d(ContentValues.TAG, "Login completado")
                }else{
                    loginExitoso.value = false
                    Log.d(ContentValues.TAG, "No se completo el Login")
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

    fun validatePassword(pass: String): Boolean {
        var passwordValida: Boolean = false
        if (pass.isEmpty()){
            msgErrorPassword = "Debe completar su contraseña"
        }else{
            passwordValida = true
        }
        return passwordValida
    }

    fun validateLogin (email: Boolean, password: Boolean): Boolean{
        val result = arrayOf(email, password)

        if (false in result){
            return false
        }
        return true
    }
}