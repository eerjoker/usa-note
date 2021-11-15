package com.ort.usanote.fragments.auth

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.viewModels.auth.LoginViewModel


class LoginFragment : Fragment() {

    lateinit var v: View
    lateinit var email2TxtLayout: TextInputLayout
    lateinit var email2TextInputEdit: TextInputEditText
    lateinit var password2TxtLayout: TextInputLayout
    lateinit var password2TextInputEdit: TextInputEditText
    lateinit var loginButton: Button
    lateinit var irARegistro: Button
    lateinit var updatePassword: Button
    lateinit var rootLayout: ConstraintLayout
    lateinit var progressBar: ProgressBar
    private lateinit var toolbar : Toolbar
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var theme : Resources.Theme

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.login_fragment, container, false)
        email2TxtLayout = v.findViewById(R.id.emailInputLayOutTxt)
        email2TextInputEdit = v.findViewById(R.id.emailRegistroTxt)
        password2TxtLayout = v.findViewById(R.id.passInputLayOutTxt)
        password2TextInputEdit = v.findViewById(R.id.passRegistroTxt)
        loginButton = v.findViewById(R.id.updateButton2)
        irARegistro = v.findViewById(R.id.segundo_text_registro)
        updatePassword = v.findViewById(R.id.recovery_password)
        progressBar = v.findViewById(R.id.progressBar)
        rootLayout = v.findViewById(R.id.frameLayout2)
        toolbar = (activity as MainActivity).toolbar
        bottomNavigationView = (activity as MainActivity).bottomNavigationView
        theme = (activity as MainActivity).theme

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        progressBar.setVisibility(View.GONE)

        viewModel.loginExitoso.observe(viewLifecycleOwner, Observer { result ->
            if (result) {
                Snackbar.make(rootLayout, getString(R.string.ingreso_exitoso), Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.verde_confirm, theme))
                    .show()
                progressBar.setVisibility(View.GONE)

                updateContextOnLogin()
                val action = LoginFragmentDirections.actionLoginFragmentToInicioFragment()
                v.findNavController().navigate(action)
            } else {
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, getString(R.string.ingreso_no_exitoso), Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
                    .show()
            }
        })

        loginButton.setOnClickListener(){

            var email: String = email2TextInputEdit.text.toString()
            var password: String = password2TextInputEdit.text.toString()

            var emailValido = viewModel.validateEmail(email)
            var passwordValida = viewModel.validatePassword(password)

            sacarErrores(emailValido, passwordValida)

            if (viewModel.validateLogin(emailValido, passwordValida)){
                progressBar.setVisibility(View.VISIBLE)
                viewModel.ingresar(email, password)
            }else{
                asignarErrores(emailValido, passwordValida)
                Snackbar.make(rootLayout, getString(R.string.campos_invalidos), Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
                    .show()
            }

        }

        irARegistro.setOnClickListener(){
           val action = LoginFragmentDirections.actionLoginFragmentToRegistroFragment()
            v.findNavController().navigate(action)
        }

        updatePassword.setOnClickListener(){
            val action = LoginFragmentDirections.actionLoginFragmentToUpdatePasswordFragment()
            v.findNavController().navigate(action)
        }
    }

    fun asignarErrores (email: Boolean, password: Boolean){
        if (!email) email2TxtLayout.error = viewModel.msgErrorEmail
        if (!password) password2TxtLayout.error = viewModel.msgErrorPassword
    }

    fun sacarErrores (email: Boolean, password: Boolean){
        if (email) email2TxtLayout.error = null
        if (password) password2TxtLayout.error = null
    }

    private fun updateContextOnLogin() {
        // toolbar - cambia icono a logout
        val loginItem = toolbar.menu.findItem(R.id.loginFragment)
        loginItem.icon = resources.getDrawable(R.drawable.baseline_logout_white_24dp, theme)

        // bottombar - ve si agrega item para estadisticas
        viewModel.changeToAdminMenu(bottomNavigationView.menu)
    }
}