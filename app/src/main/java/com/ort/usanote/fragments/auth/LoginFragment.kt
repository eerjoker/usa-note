package com.ort.usanote.fragments.auth

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ort.usanote.R
import com.ort.usanote.viewModels.auth.LoginViewModel

class LoginFragment : Fragment() {

    lateinit var v: View
    lateinit var emailLogin: EditText
    lateinit var passwordLogin: EditText
    lateinit var loginButton: Button
    lateinit var irARegistro: Button
    lateinit var rootLayout: ConstraintLayout
    private val viewModelLogin: LoginViewModel by viewModels()

    // lateinit var emailV2: EditText

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.login_fragment, container, false)
        emailLogin = v.findViewById(R.id.emailLoginTxt)
        passwordLogin = v.findViewById(R.id.passwordLoginTxt)
        loginButton = v.findViewById(R.id.loginButton)
        irARegistro = v.findViewById(R.id.segundo_text_registro)
        rootLayout = v.findViewById(R.id.frameLayout2)

        //   emailV2 = v.findViewById(R.id.emailLoginTxt2)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        viewModelLogin.loginExitoso.observe(viewLifecycleOwner, Observer { result ->
            if (result){
                Snackbar.make(rootLayout, "Ingreso Exitoso", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()

                //Rompe al buscar la action.
                val action = LoginFragmentDirections.actionLoginFragment2ToInicioFragment2()
                v.findNavController().navigate(action)
            }else{
                Snackbar.make(rootLayout, "El ingreso no fue exitoso. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }
        })

        loginButton.setOnClickListener(){

            val email: String = emailLogin.text.toString()
            val password: String = passwordLogin.text.toString()

            val emailValido = viewModelLogin.validateEmail(email)
            val passwordValida = viewModelLogin.validatePassword(password)

            if (viewModelLogin.validateLogin(emailValido, passwordValida)){
                viewModelLogin.ingresar(email, password)
            }else{
                asignarErrores(emailValido, passwordValida)
                Snackbar.make(rootLayout, "Campos invalidos. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }


        }

        irARegistro.setOnClickListener(){
            val action = LoginFragmentDirections.actionLoginFragment2ToRegistroFragment2()
            v.findNavController().navigate(action)
        }
    }

    fun asignarErrores (email: Boolean, password: Boolean){
        if (!email) emailLogin.error = viewModelLogin.msgErrorEmail
        if (!password) passwordLogin.error = viewModelLogin.msgErrorPassword
    }

}