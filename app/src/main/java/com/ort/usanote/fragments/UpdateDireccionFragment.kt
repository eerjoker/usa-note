package com.ort.usanote.fragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
import com.ort.usanote.viewModels.UpdateDireccionViewModel

class UpdateDireccionFragment : Fragment() {

    lateinit var v: View
    lateinit var aliasDireccion: TextInputEditText
    lateinit var aliasDireccionLayout: TextInputLayout
    lateinit var nombreDireccion: TextInputEditText
    lateinit var nombreDireccionLayout: TextInputLayout
    lateinit var calleDireccion: TextInputEditText
    lateinit var calleDireccionLayout: TextInputLayout
    lateinit var localidadDireccion: TextInputEditText
    lateinit var localidadDireccionLayout: TextInputLayout
    lateinit var nroDireccion: TextInputEditText
    lateinit var nroDireccionLayout: TextInputLayout
    lateinit var pisoDireccion: TextInputEditText
    lateinit var pisoDireccionLayout: TextInputLayout
    lateinit var deptoDireccion: TextInputEditText
    lateinit var deptoDireccionLayout: TextInputLayout
    lateinit var provinciaDireccion: TextInputEditText
    lateinit var provinciaDireccionLayout: TextInputLayout
    lateinit var codigoPostalDireccion: TextInputEditText
    lateinit var codigoPostalDireccionLayout: TextInputLayout
    lateinit var btnActualizarDireccion: Button
    lateinit var btnEliminarDireccion: Button
    lateinit var rootLayout: ConstraintLayout
    lateinit var progressBar: ProgressBar
    private val viewModelDireccion: UpdateDireccionViewModel by viewModels()

    companion object {
        fun newInstance() = UpdateDireccionFragment()
    }

    private lateinit var viewModel: UpdateDireccionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.update_direccion_fragment, container, false)

        aliasDireccion = v.findViewById(R.id.aliasTxt)
        aliasDireccionLayout = v.findViewById(R.id.aliasInputLayOutTxt)
        nombreDireccion = v.findViewById(R.id.nombreTxt)
        nombreDireccionLayout = v.findViewById(R.id.nombreInputLayOutTxt)
        calleDireccion = v.findViewById(R.id.calleTxt)
        calleDireccionLayout = v.findViewById(R.id.calleInputLayOutTxt)
        localidadDireccion = v.findViewById(R.id.localidadTxt)
        localidadDireccionLayout = v.findViewById(R.id.localidadInputLayOutTxt)
        nroDireccion = v.findViewById(R.id.numeroTxt)
        nroDireccionLayout = v.findViewById(R.id.numeroInputLayOutTxt)
        pisoDireccion = v.findViewById(R.id.pisoTxt)
        pisoDireccionLayout = v.findViewById(R.id.pisoInputLayOutTxt)
        deptoDireccion = v.findViewById(R.id.deptoTxt)
        deptoDireccionLayout = v.findViewById(R.id.deptoInputLayOutTxt)
        provinciaDireccion = v.findViewById(R.id.provinciaTextTxt)
        provinciaDireccionLayout = v.findViewById(R.id.provinciaTextInputLayOutTxt)
        codigoPostalDireccion = v.findViewById(R.id.cpTxt)
        codigoPostalDireccionLayout = v.findViewById(R.id.cpInputLayOutTxt)
        btnActualizarDireccion = v.findViewById(R.id.btnActualizarDireccion)
        btnEliminarDireccion = v.findViewById(R.id.btnEliminarDireccion)
        rootLayout = v.findViewById(R.id.frameLayout7)
        progressBar = v.findViewById(R.id.progressBar2)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateDireccionViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        progressBar.setVisibility(View.GONE)

        var direccionAux = UpdateDireccionFragmentArgs.fromBundle(requireArguments()).direccion
        var idDireccion = UpdateDireccionFragmentArgs.fromBundle(requireArguments()).id

        Log.d("ID Direccion",idDireccion)

        aliasDireccion.setText(direccionAux.alias)
        nombreDireccion.setText(direccionAux.nombreCompleto)
        calleDireccion.setText(direccionAux.calle)
        localidadDireccion.setText(direccionAux.localidad)
        nroDireccion.setText(direccionAux.numero)
        pisoDireccion.setText(direccionAux.piso)
        deptoDireccion.setText(direccionAux.departamento)
        provinciaDireccion.setText(direccionAux.provincia)
        codigoPostalDireccion.setText(direccionAux.codigoPostal)

        viewModelDireccion.actualizacionExitosa.observe(viewLifecycleOwner, Observer { result ->
            if (result){
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Dirección actualizada", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()

                v.findNavController().popBackStack()

            }else{
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Ocurrio un error. Verifique sus datos e inténtelo nuevamente", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#E91E3C")).show()
            }
        })

        viewModelDireccion.eliminacionExitosa.observe(viewLifecycleOwner, Observer {
            if (it){
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Dirección eliminada con éxito", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#4CAF50")).show()

                v.findNavController().popBackStack()
            }else{
                progressBar.setVisibility(View.GONE)
                Snackbar.make(rootLayout, "Ocurrio un error. Verifique sus datos e inténtelo nuevamente", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                        Color.parseColor("#E91E3C")).show()
            }

        })

        btnEliminarDireccion.setOnClickListener(){
            viewModelDireccion.eliminarDireccion(idDireccion)
        }

        btnActualizarDireccion.setOnClickListener(){

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

            sacarErrores(aliasValido, nombreValido, calleValido, localidadValido, nroValido, pisoValido, deptoValido, provinciaValido, codigoPostalValido)

            if (viewModelDireccion.validateForm(aliasValido, nombreValido, calleValido, localidadValido, nroValido, pisoValido, deptoValido, provinciaValido, codigoPostalValido)){
                progressBar.setVisibility(View.VISIBLE)
                viewModelDireccion.actualizarDireccion(idDireccion, alias, nombre, calle, localidad, nro, piso, depto, provincia, codigoPostal)
            }else{
                asignarErrores(aliasValido, nombreValido, calleValido, localidadValido, nroValido, pisoValido, deptoValido, provinciaValido, codigoPostalValido)
                Snackbar.make(rootLayout, "Campos invalidos. Verifique sus datos", Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE).setBackgroundTint(
                    Color.parseColor("#E91E3C")).show()
            }
        }
    }

    fun asignarErrores (alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean){
        if (!alias) aliasDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!nombre) nombreDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!calle) calleDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!localidad) localidadDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!nro) nroDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!piso) pisoDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!depto) deptoDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!provincia) provinciaDireccionLayout.error = viewModelDireccion.msgErrorGeneral
        if (!codigoPostal) codigoPostalDireccionLayout.error = viewModelDireccion.msgErrorGeneral

    }

    fun sacarErrores (alias: Boolean, nombre: Boolean, calle: Boolean, localidad: Boolean, nro: Boolean, piso: Boolean, depto: Boolean, provincia: Boolean, codigoPostal: Boolean){
        aliasDireccionLayout.error = null
        nombreDireccionLayout.error = null
        calleDireccionLayout.error = null
        localidadDireccionLayout.error = null
        nroDireccionLayout.error = null
        pisoDireccionLayout.error = null
        deptoDireccionLayout.error = null
        provinciaDireccionLayout.error = null
        codigoPostalDireccionLayout.error = null
    }


}