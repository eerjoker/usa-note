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
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ort.usanote.R
import com.ort.usanote.viewModels.auth.UpdatePasswordViewModel
import androidx.lifecycle.Observer

class UpdatePasswordFragment : Fragment() {

    lateinit var v: View
    lateinit var email2TxtLayout: TextInputLayout
    lateinit var email2TextInputEdit: TextInputEditText
    lateinit var updateButton: Button
    lateinit var rootLayout: ConstraintLayout
    lateinit var progressBar: ProgressBar
    private val viewModelUpdatePassword: UpdatePasswordViewModel by viewModels()

    companion object {
        fun newInstance() = UpdatePasswordFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.update_password_fragment, container, false)
        email2TxtLayout = v.findViewById(R.id.emailInputLayOutTxt)
        email2TextInputEdit = v.findViewById(R.id.emailRegistroTxt)
        updateButton = v.findViewById(R.id.updateButton)
        progressBar = v.findViewById(R.id.progressBar)
        rootLayout = v.findViewById(R.id.frameLayout4)


        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        progressBar.setVisibility(View.GONE)

        viewModelUpdatePassword.updateExitoso.observe(viewLifecycleOwner, Observer { result ->
            if (result){
                Snackbar.make(rootLayout, "Email enviado", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()
                progressBar.setVisibility(View.GONE)
                v.findNavController().popBackStack()
            }else{
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Hubo un problema. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }
        })

        updateButton.setOnClickListener(){

            var email: String = email2TextInputEdit.text.toString()
            var emailValido = viewModelUpdatePassword.validateEmail(email)

            sacarErrores(emailValido)

            if (viewModelUpdatePassword.validateUpdate(emailValido)){
                progressBar.setVisibility(View.VISIBLE)
                viewModelUpdatePassword.recuperar(email)
            }else{
                asignarErrores(emailValido)
                Snackbar.make(rootLayout, "Email invalido. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }
        }

    }

    fun asignarErrores (email: Boolean){
        if (!email) email2TxtLayout.error = viewModelUpdatePassword.msgErrorEmail

    }

    fun sacarErrores (email: Boolean){
        if (email) email2TxtLayout.error = null

    }

}