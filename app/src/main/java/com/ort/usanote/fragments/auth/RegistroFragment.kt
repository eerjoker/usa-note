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
import com.ort.usanote.viewModels.auth.RegistroViewModel

class RegistroFragment : Fragment() {

    lateinit var v: View
    lateinit var nombreRegistro: EditText
    lateinit var apellidoRegistro: EditText
    lateinit var telefonoRegistro: EditText
    lateinit var emailRegistro: EditText
    lateinit var passwordRegistro: EditText
    lateinit var registroButton: Button
    lateinit var rootLayout: ConstraintLayout
    private val viewModelRegistro: RegistroViewModel by viewModels()

    companion object {
        fun newInstance() = RegistroFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.registro_fragment, container, false)

        nombreRegistro = v.findViewById(R.id.nombreRegistroTxt)
        apellidoRegistro = v.findViewById(R.id.apellidoRegistroTxt)
        telefonoRegistro = v.findViewById(R.id.telefonoRegistroTxt)
        emailRegistro = v.findViewById(R.id.emailRegistroTxt)
        passwordRegistro = v.findViewById(R.id.passwordRegistroTxt)
        registroButton = v.findViewById(R.id.RegistroButton)
        rootLayout = v.findViewById(R.id.frameLayout3)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        viewModelRegistro.registroExitoso.observe(viewLifecycleOwner, Observer { result ->
            if (result){
                Snackbar.make(rootLayout, "Registro Exitoso", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()
                //Preguntar si sumamos action o no
                //val action = RegistroFragmentDirections.actionRegistroFragment2ToLoginFragment2()
                //v.findNavController().navigate(action)
                //v.findNavController().backQueue
                v.findNavController().popBackStack()
            }else{
                Snackbar.make(rootLayout, "Error en el registro. Verifique sus datos e inténtelo nuevamente", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#E91E3C")).show()
            }
        })

        registroButton.setOnClickListener() {
            var nombre: String = nombreRegistro.text.toString()
            var apellido: String = apellidoRegistro.text.toString()
            var telefono: String = telefonoRegistro.text.toString()
            var email: String = emailRegistro.text.toString()
            var password: String = passwordRegistro.text.toString()

            var nombreValido = viewModelRegistro.validateGenerales(nombre)
            var apellidoValido = viewModelRegistro.validateGenerales(apellido)
            var telefonoValido = viewModelRegistro.validateGenerales(telefono)
            var emailValido = viewModelRegistro.validateEmail(email)
            var passwordValida = viewModelRegistro.validatePassword(password)

            if (viewModelRegistro.validateForm(nombreValido, apellidoValido, telefonoValido, emailValido, passwordValida)){
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
        if (!nombre) nombreRegistro.error = viewModelRegistro.msgErrorGeneral
        if (!apellido) apellidoRegistro.error = viewModelRegistro.msgErrorGeneral
        if (!telefono) telefonoRegistro.error = viewModelRegistro.msgErrorGeneral
        if (!email) emailRegistro.error = viewModelRegistro.msgErrorEmail
        if (!password) passwordRegistro.error = viewModelRegistro.msgErrorPassword
    }

}