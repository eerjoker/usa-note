package com.ort.usanote.fragments

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
import com.ort.usanote.viewModels.DireccionViewModel

class DireccionFragment : Fragment() {


    lateinit var v: View
    lateinit var aliasDireccion: EditText
    lateinit var nombreDireccion: EditText
    lateinit var calleDireccion: EditText
    lateinit var localidadDireccion: EditText
    lateinit var nroDireccion: EditText
    lateinit var pisoDireccion: EditText
    lateinit var deptoDireccion: EditText
    lateinit var provinciaDireccion: EditText
    lateinit var codigoPostalDireccion: EditText
    lateinit var btnDireccion: Button
    lateinit var rootLayout: ConstraintLayout
    private val viewModelDireccion: DireccionViewModel by viewModels()


    companion object {
        fun newInstance() = DireccionFragment()
    }

    private lateinit var viewModel: DireccionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.direccion_fragment, container, false)

        aliasDireccion = v.findViewById(R.id.aliasText)
        nombreDireccion = v.findViewById(R.id.nombreText)
        calleDireccion = v.findViewById(R.id.calleText)
        localidadDireccion = v.findViewById(R.id.localidadText)
        nroDireccion = v.findViewById(R.id.numeroText)
        pisoDireccion = v.findViewById(R.id.pisoText)
        deptoDireccion = v.findViewById(R.id.deptoText)
        provinciaDireccion = v.findViewById(R.id.provinciaText)
        codigoPostalDireccion = v.findViewById(R.id.cpText)
        btnDireccion = v.findViewById(R.id.btnDireccion)
        rootLayout = v.findViewById(R.id.frameLayout3)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        viewModelDireccion.direccionExitosa.observe(viewLifecycleOwner, Observer { result ->
            if (result){
                Snackbar.make(rootLayout, "Dirección agregada", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()

                v.findNavController().popBackStack()

            }else{
                Snackbar.make(rootLayout, "Ocurrio un error. Verifique sus datos e inténtelo nuevamente", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#E91E3C")).show()
            }
        })

        btnDireccion.setOnClickListener(){

            var alias: String = aliasDireccion.text.toString()
            var nombre: String = nombreDireccion.text.toString()
            var calle: String = calleDireccion.text.toString()
            var localidad: String = localidadDireccion.text.toString()
            var nro: String = nroDireccion.text.toString()
            var piso: String = pisoDireccion.text.toString()
            var depto: String = deptoDireccion.text.toString()
            var provincia: String = provinciaDireccion.text.toString()
            var codigoPostal: String = codigoPostalDireccion.text.toString()

            var aliasValido: Boolean = viewModelDireccion.validateGenerales(alias)
            var nombreValido: Boolean = viewModelDireccion.validateGenerales(nombre)
            var calleValido: Boolean = viewModelDireccion.validateGenerales(calle)
            var localidadValido: Boolean = viewModelDireccion.validateGenerales(localidad)
            var nroValido: Boolean = viewModelDireccion.validateGenerales(nro)
            var pisoValido: Boolean = viewModelDireccion.validateGenerales(piso)
            var deptoValido: Boolean = viewModelDireccion.validateGenerales(depto)
            var provinciaValido: Boolean = viewModelDireccion.validateGenerales(provincia)
            var codigoPostalValido: Boolean = viewModelDireccion.validateGenerales(codigoPostal)

            if (viewModelDireccion.validateForm(aliasValido, nombreValido, calleValido, localidadValido, nroValido, pisoValido, deptoValido, provinciaValido, codigoPostalValido)){
                viewModelDireccion.agregarDireccion(alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)
            }else{
                asignarErrores(aliasValido, nombreValido, calleValido, localidadValido, nroValido, pisoValido, deptoValido, provinciaValido, codigoPostalValido)
                Snackbar.make(rootLayout, "Campos invalidos. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }
        }
    }

    fun asignarErrores (alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean){
        if (!alias) aliasDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!nombre) nombreDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!calle) calleDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!localidad) localidadDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!nro) nroDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!piso) pisoDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!depto) deptoDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!provincia) provinciaDireccion.error = viewModelDireccion.msgErrorGeneral
        if (!codigoPostal) codigoPostalDireccion.error = viewModelDireccion.msgErrorGeneral

    }

}