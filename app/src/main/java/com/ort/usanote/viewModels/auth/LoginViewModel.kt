package com.ort.usanote.viewModels.auth

import android.content.ContentValues
import android.util.Log
import android.view.Menu
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    lateinit var msgErrorEmail: String
    lateinit var msgErrorPassword: String
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val loginExitoso = MutableLiveData<Boolean>()
    var esCliente = MutableLiveData<Boolean>()


    fun ingresar (email: String, password: String){
        
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if (it.isSuccessful){

                    var user = auth.currentUser
                        db.collection("usuarios").document(user!!.uid).get().addOnCompleteListener(){result ->
                            if (result.isSuccessful){
                                esCliente.value = result.result!!.getBoolean("esCliente")
                                loginExitoso.value = true
                                Log.d(ContentValues.TAG, "Login completado")
                            }
                        }
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