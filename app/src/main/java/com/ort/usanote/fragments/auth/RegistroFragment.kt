package com.ort.usanote.fragments.auth

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ort.usanote.R
import com.ort.usanote.viewModels.auth.RegistroViewModel

class RegistroFragment : Fragment() {

    lateinit var v: View
    lateinit var registroButton: Button
    lateinit var rootLayout: ConstraintLayout
    lateinit var nombre2TxtLayout: TextInputLayout
    lateinit var nombre2TextInputEdit: TextInputEditText
    lateinit var apellido2TxtLayout: TextInputLayout
    lateinit var apellido2TextInputEdit: TextInputEditText
    lateinit var telefono2TxtLayout: TextInputLayout
    lateinit var telefono2TextInputEdit: TextInputEditText
    lateinit var email2TxtLayout: TextInputLayout
    lateinit var email2TextInputEdit: TextInputEditText
    lateinit var password2TxtLayout: TextInputLayout
    lateinit var password2TextInputEdit: TextInputEditText
    lateinit var progressBar: ProgressBar
    private val viewModelRegistro: RegistroViewModel by viewModels()

    companion object {
        fun newInstance() = RegistroFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.registro_fragment, container, false)

        registroButton = v.findViewById(R.id.RegistroButton)
        rootLayout = v.findViewById(R.id.frameLayout3)
        nombre2TxtLayout = v.findViewById(R.id.nombreInputLayOutTxt)
        nombre2TextInputEdit = v.findViewById(R.id.nombreRegistroTxt)
        apellido2TxtLayout = v.findViewById(R.id.apellidoInputLayOutTxt)
        apellido2TextInputEdit = v.findViewById(R.id.apellidoRegistroTxt)
        telefono2TxtLayout = v.findViewById(R.id.telefonoInputLayOutTxt)
        telefono2TextInputEdit = v.findViewById(R.id.telefonoRegistroTxt)
        email2TxtLayout = v.findViewById(R.id.emailInputLayOutTxt)
        email2TextInputEdit = v.findViewById(R.id.emailRegistroTxt)
        password2TxtLayout = v.findViewById(R.id.passInputLayOutTxt)
        password2TextInputEdit = v.findViewById(R.id.passRegistroTxt)
        progressBar = v.findViewById(R.id.progressBar2)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        progressBar.setVisibility(View.GONE)

        viewModelRegistro.registroExitoso.observe(viewLifecycleOwner, Observer { result ->
            if (result){
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Registro Exitoso", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()

                v.findNavController().popBackStack()
            }else{
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Error en el registro. Verifique sus datos e inténtelo nuevamente", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#E91E3C")).show()
            }
        })

        registroButton.setOnClickListener() {

            var nombre: String = nombre2TextInputEdit.text.toString().trim()
            var apellido: String = apellido2TextInputEdit.text.toString().trim()
            var telefono: String = telefono2TextInputEdit.text.toString().trim()
            var email: String = email2TextInputEdit.text.toString().trim()
            var password: String = password2TextInputEdit.text.toString().trim()

            var nombreValido = viewModelRegistro.validateNombreApellido(nombre)
            var apellidoValido = viewModelRegistro.validateNombreApellido(apellido)
            var telefonoValido = viewModelRegistro.validatePhone(telefono)
            var emailValido = viewModelRegistro.validateEmail(email)
            var passwordValida = viewModelRegistro.validatePassword(password)

            sacarErrores(nombreValido, apellidoValido, telefonoValido, emailValido, passwordValida)

            if (viewModelRegistro.validateForm(nombreValido, apellidoValido, telefonoValido, emailValido, passwordValida)){
                progressBar.setVisibility(View.VISIBLE)
                viewModelRegistro.registrar(nombre, apellido, telefono, email, password)
            }else{
                asignarErrores(nombreValido, apellidoValido, telefonoValido, emailValido, passwordValida)
                Snackbar.make(rootLayout, "Campos invalidos. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }
        }

    }

    fun asignarErrores (nombre: Boolean, apellido: Boolean, telefono: Boolean,email: Boolean, password: Boolean){
        if (!nombre) nombre2TxtLayout.error = viewModelRegistro.msgErrorNombreApellido
        if (!apellido) apellido2TxtLayout.error = viewModelRegistro.msgErrorGeneral
        if (!telefono) telefono2TxtLayout.error = viewModelRegistro.msgErrorPhone
        if (!email) email2TxtLayout.error = viewModelRegistro.msgErrorEmail
        if (!password) password2TxtLayout.error = viewModelRegistro.msgErrorPassword
    }

    fun sacarErrores (nombre: Boolean, apellido: Boolean, telefono: Boolean,email: Boolean, password: Boolean){
        if (nombre) nombre2TxtLayout.error = null
        if (apellido) apellido2TxtLayout.error = null
        if (telefono) telefono2TxtLayout.error = null
        if (email) email2TxtLayout.error = null
        if (password) password2TxtLayout.error = null
    }
}