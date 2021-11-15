package com.ort.usanote.fragments

import android.content.res.Resources
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.entities.Envio
import com.ort.usanote.viewModels.ShipmentMethodViewModel

class ShipmentMethodFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    lateinit var checkBoxLoPasoABuscar: CheckBox
    lateinit var checkBoxEnvioPorMoto: CheckBox
    var envio: Envio? = null
    lateinit var rootLayout: ConstraintLayout
    private lateinit var theme : Resources.Theme
    private val COSTO_ENVIO = 300.00

    companion object {
        fun newInstance() = ShipmentMethodFragment()
    }

    private lateinit var viewModel: ShipmentMethodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.shipment_method_fragment, container, false)
        btnContinue = v.findViewById(R.id.buttonContinue)
        checkBoxLoPasoABuscar = v.findViewById(R.id.checkBoxLoPasoABuscar)
        checkBoxEnvioPorMoto = v.findViewById(R.id.checkBoxEnvioXMoto)
        rootLayout = v.findViewById(R.id.shipmentMethodConstraintLayout)
        theme = (activity as MainActivity).theme
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShipmentMethodViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        checkBoxLoPasoABuscar.setOnClickListener {
            this.envio = Envio(0, "Retira en local", 0.00)
        }

        checkBoxEnvioPorMoto.setOnClickListener {
            this.envio = Envio(120, "Envio por moto", COSTO_ENVIO)
        }

        btnContinue.setOnClickListener {
            val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToCheckAddressFragment(envio)
            if (envio == null) {
                Snackbar.make(rootLayout, getString(R.string.no_se_selecciono_metodo_envio), Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
                    .show()
            } else {
                v.findNavController().navigate(action)
            }
        }
    }

}