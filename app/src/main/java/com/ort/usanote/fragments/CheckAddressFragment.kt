package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.navigation.findNavController
import com.ort.usanote.R
import com.ort.usanote.entities.Envio
import com.ort.usanote.viewModels.CheckAddressViewModel

class CheckAddressFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    lateinit var envio: Envio
    lateinit var checkBoxLoPasoABuscar: CheckBox
    lateinit var checkBoxEnvioPorMoto: CheckBox
    private val RETIRO_EN_LOCAL = "Retira en local"
    private val ENVIO_MOTO = "Envio por moto"

    companion object {
        fun newInstance() = CheckAddressFragment()
    }

    private lateinit var viewModel: CheckAddressViewModel

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
    }

}