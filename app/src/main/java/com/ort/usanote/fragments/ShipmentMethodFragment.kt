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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.entities.Direccion
import com.ort.usanote.entities.Envio
import com.ort.usanote.entities.Usuario
import com.ort.usanote.viewModels.ShipmentMethodViewModel
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
    lateinit var textViewAddressValue : TextView
    lateinit var textViewAddAddress : TextView
    lateinit var floatingActionButton : FloatingActionButton
    var envio: Envio? = null
    lateinit var rootLayout: ConstraintLayout
    private lateinit var theme : Resources.Theme
    private val COSTO_ENVIO = 300.00
    private val ENVIO_MOTO = "Envio por moto"
    private lateinit var direccion: String
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    private var tieneDomicilios: Boolean = false
    var direccionLiveData = MutableLiveData <Direccion> ()

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
        textViewAddress = v.findViewById(R.id.textViewAdress)
        textViewAddressValue = v.findViewById(R.id.adressValue)
        textViewAddAddress = v.findViewById(R.id.textViewAddAddress)
        floatingActionButton = v.findViewById(R.id.floatingActionButton)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShipmentMethodViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private  fun getAddress () {
        var direccionEntregaCompleta = ""
        val user = auth.currentUser
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val usuario = fetchUser(user!!.uid)
            val direcciones = usuario.direcciones //lista
            if(direcciones != null){
                if(direcciones!!.size > 0){
                    val direccion = direcciones[0]
                    val direccionDb = fetchDireccion(direccion)
                    direccionLiveData.value = direccionDb
                    tieneDomicilios = true
                } else {
                    tieneDomicilios = false
                }
            }
        }
    }

    private suspend fun fetchDireccion(direccion: String): Direccion {
        val dirDb = db.collection("direcciones").document(direccion).get().await()
        var direccion = Direccion()
        if(dirDb != null){
            direccion = dirDb.toObject(Direccion::class.java)!!
        }
        return  direccion
    }

    private suspend fun fetchUser(uid: String): Usuario {
        val userDb = db.collection("usuarios").document(uid).get().await()
        var usuario:Usuario = Usuario()
        if(userDb != null){
            usuario = userDb.toObject(Usuario::class.java)!!
        }
        return usuario
    }

    override fun onStart() {
        super.onStart()
        direccionLiveData.observe(viewLifecycleOwner, Observer {
            checkBoxLoPasoABuscar.isChecked = false
            textViewAddress.isVisible = true
            textViewAddressValue.text = direccionLiveData.value!!.calle + " " + direccionLiveData.value!!.numero
            this.direccion = direccionLiveData.value!!.calle + " " + direccionLiveData.value!!.numero
            textViewAddressValue.isVisible = true
            textViewAddAddress.isVisible = true
            floatingActionButton.isVisible = true
        })
        checkBoxLoPasoABuscar.setOnClickListener {
            if (checkBoxLoPasoABuscar.isChecked) {
                this.envio = Envio(0, "Retira en local", 0.00)
                checkBoxEnvioPorMoto.isChecked = false
            } else {
                this.envio = null
            }
        }

        checkBoxEnvioPorMoto.setOnClickListener {
            if (checkBoxEnvioPorMoto.isChecked) {
                this.envio = Envio(120, "Envio por moto", COSTO_ENVIO)
                getAddress()
            } else {
                this.envio = null
            }
        }

        btnContinue.setOnClickListener {
            if (this.envio == null) {
                Snackbar.make(rootLayout, getString(R.string.no_se_selecciono_metodo_envio), Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
                    .show()
            } else if (this.envio != null && envio!!.tipoEnvio == ENVIO_MOTO && !this.tieneDomicilios) {
                Snackbar.make(rootLayout, getString(R.string.debe_agregar_domicilio), Snackbar.LENGTH_LONG).setAnimationMode(
                    BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
                    .show()
            }
            else {
                if (this.envio!!.tipoEnvio == ENVIO_MOTO) {
                    val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToCheckAddressFragment(envio, direccion)
                    v.findNavController().navigate(action)
                } else {
                    val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToPaymentMethodFragment(envio!!)
                    v.findNavController().navigate(action)
                }

            }
        }
    }

}