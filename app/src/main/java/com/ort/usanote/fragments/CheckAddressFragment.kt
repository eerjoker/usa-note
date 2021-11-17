package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.R
import com.ort.usanote.adapters.DireccionUserAdapter
import com.ort.usanote.entities.Direccion
import com.ort.usanote.entities.Envio
import com.ort.usanote.viewModels.CheckAddressViewModel
import com.ort.usanote.viewModels.user.UserViewModel

class CheckAddressFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    lateinit var envio: Envio
    lateinit var checkBoxLoPasoABuscar: CheckBox
    lateinit var checkBoxEnvioPorMoto: CheckBox
    private val RETIRO_EN_LOCAL = "Retira en local"
    private val ENVIO_MOTO = "Envio por moto"
    lateinit var addressValueTxtView : TextView
    lateinit var addAddressBtn : FloatingActionButton
    lateinit var btnUpdateDireccion : FloatingActionButton
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    lateinit var direccion : String
    private lateinit var idDireccion : String

    companion object {
        fun newInstance() = CheckAddressFragment()
    }

    private lateinit var viewModel: CheckAddressViewModel
    private val viewModelUser: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.check_address_fragment, container, false)
        btnContinue = v.findViewById(R.id.buttonContinue)
        checkBoxLoPasoABuscar = v.findViewById(R.id.checkBoxPasoBuscar)
        checkBoxLoPasoABuscar.isClickable = false
        checkBoxEnvioPorMoto = v.findViewById(R.id.checkBoxMoto)
        checkBoxEnvioPorMoto.isClickable = false
        addAddressBtn = v.findViewById(R.id.floatingActionButton)
        addressValueTxtView = v.findViewById(R.id.adressValue)
        btnUpdateDireccion = v.findViewById(R.id.btnUpdateDireccion)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckAddressViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        envio = CheckAddressFragmentArgs.fromBundle(requireArguments()).envio!!
        direccion = CheckAddressFragmentArgs.fromBundle(requireArguments()).direccion
        idDireccion = CheckAddressFragmentArgs.fromBundle(requireArguments()).idDireccion

        addressValueTxtView.text = direccion
        if (envio.tipoEnvio == RETIRO_EN_LOCAL) {
            checkBoxLoPasoABuscar.isChecked = true
        }
        if (envio.tipoEnvio == ENVIO_MOTO) {
            checkBoxEnvioPorMoto.isChecked = true
        }

        btnContinue.setOnClickListener {
            val action = CheckAddressFragmentDirections.actionCheckAddressFragmentToPaymentMethodFragment(envio)
            v.findNavController().navigate(action)
        }
        addAddressBtn.setOnClickListener {
            val action = CheckAddressFragmentDirections.actionCheckAddressFragmentToDireccionFragment()
            v.findNavController().navigate(action)
        }

        btnUpdateDireccion.setOnClickListener {
            viewModelUser.getDirecciones()
            val action = CheckAddressFragmentDirections.actionCheckAddressFragmentToUpdateDireccionFragment(direccion, idDireccion)
        }
    }

}