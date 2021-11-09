package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.ort.usanote.R
import com.ort.usanote.viewModels.ShipmentMethodViewModel

class ShipmentMethodFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View

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

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShipmentMethodViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()


        btnContinue.setOnClickListener {
            val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToCheckAddressFragment()
            v.findNavController().navigate(action)
        }
    }

}