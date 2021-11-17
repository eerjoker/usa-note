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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.DireccionShipmentAdapter
import com.ort.usanote.entities.Direccion
import com.ort.usanote.entities.Envio
import com.ort.usanote.entities.Usuario
import com.ort.usanote.viewModels.ShipmentMethodViewModel
import com.ort.usanote.viewModels.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShipmentMethodFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    lateinit var checkBoxLoPasoABuscar: CheckBox
    lateinit var checkBoxEnvioPorMoto: CheckBox
    lateinit var textViewAddress : TextView
    lateinit var textViewAddAddress : TextView
    lateinit var btnAddAddress : FloatingActionButton
    var envio: Envio? = null
    lateinit var rootLayout: ConstraintLayout
    private lateinit var theme : Resources.Theme
    private val COSTO_ENVIO = 300.00
    private val ENVIO_MOTO = "Envio por moto"
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var direccionLiveData = MutableLiveData<Direccion>()
    private val userViewModel : UserViewModel by viewModels()
    private var direcciones: MutableList<Direccion> = mutableListOf()
    private lateinit var idsDirecciones: MutableList<String>
    private lateinit var recDirecciones: RecyclerView
    private var direccionesListPosition = MutableLiveData<Int>()
    private lateinit var direccionUserAdapter: DireccionShipmentAdapter

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
        textViewAddress = v.findViewById(R.id.textViewAddress)
        textViewAddAddress = v.findViewById(R.id.textViewAddAddress)
        btnAddAddress = v.findViewById(R.id.floatingActionButton)
        recDirecciones = v.findViewById(R.id.recDirecciones)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShipmentMethodViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private suspend fun getAddressList() {
        userViewModel.direccionesUser.observe(viewLifecycleOwner, Observer{
            if (it != null) {
                //progressBar.setVisibility(View.GONE)
                if(it.size >= 0){
                    direcciones = it as ArrayList<Direccion>
                    updateDireccionesAdapter(direcciones)
                    //envioPorMotoIsChecked()
                }
            }
        })

        userViewModel.idDireccionesUser.observe(viewLifecycleOwner, Observer {
            if (it != null){
                idsDirecciones = it
            }
        })
        userViewModel.getDirecciones()
    }

    private fun updateDireccionesAdapter(direcciones: MutableList<Direccion>) {
        direccionUserAdapter = DireccionShipmentAdapter(direcciones, direccionesListPosition.value!!, requireContext()) { position ->
            onDireccionClick(position)
        }
        recDirecciones.adapter = direccionUserAdapter
        direccionUserAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        recDirecciones.setHasFixedSize(true)
        recDirecciones.layoutManager = LinearLayoutManager(requireContext())
        direccionesListPosition.value = 0

        direccionesListPosition.observe(viewLifecycleOwner, Observer{
            updateDireccionesAdapter(direcciones)
        })

        checkIfEnvioPorMotoIsChecked()
        checkBoxLoPasoABuscar.setOnClickListener {
            if (checkBoxLoPasoABuscar.isChecked) {
                this.envio = Envio(0, "Retira en local", 0.00)
                retiroEnLocalIsChecked()
            } else {
                this.envio = null
            }
        }

        checkBoxEnvioPorMoto.setOnClickListener {
            checkIfEnvioPorMotoIsChecked()
        }

        btnAddAddress.setOnClickListener {
            val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToDireccionFragment()
            v.findNavController().navigate(action)
        }

        btnContinue.setOnClickListener {
            if (this.envio == null) {
                Snackbar.make(rootLayout, getString(R.string.no_se_selecciono_metodo_envio), Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
                    .show()
            } else if (this.envio!!.tipoEnvio == ENVIO_MOTO && direcciones.size == 0) {
                showSnackbarDebeAgregarDomicilio()
            } else {
                val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToPaymentMethodFragment(envio!!)
                v.findNavController().navigate(action)
            }
        }
    }

    private fun checkIfEnvioPorMotoIsChecked() {
        if (checkBoxEnvioPorMoto.isChecked) {
                this.envio = Envio(120, ENVIO_MOTO, COSTO_ENVIO)
                envioPorMotoIsChecked()
                //getAddress()
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    getAddressList()
                }
        } else {
            this.envio = null
        }
    }

    private fun showSnackbarDebeAgregarDomicilio() {
        Snackbar.make(rootLayout, getString(R.string.debe_agregar_domicilio), Snackbar.LENGTH_LONG).setAnimationMode(
            BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
            .show()
    }

    private fun onDireccionClick(direccionPosition: Int) {
        this.direccionesListPosition.value = direccionPosition
    }

    private fun retiroEnLocalIsChecked() {
        checkBoxEnvioPorMoto.isChecked = false
        checkBoxLoPasoABuscar.isChecked = true
        textViewAddress.isVisible = false
        textViewAddAddress.isVisible = false
        btnAddAddress.isVisible = false
        recDirecciones.isVisible = false
    }

    private fun envioPorMotoIsChecked() {
        checkBoxLoPasoABuscar.isChecked = false
        checkBoxEnvioPorMoto.isChecked = true
        textViewAddress.isVisible = true
        textViewAddAddress.isVisible = true
        btnAddAddress.isVisible = true
        recDirecciones.isVisible = true
    }
}