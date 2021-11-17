package com.ort.usanote.fragments

import android.content.res.Resources
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.entities.Direccion
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
    private val ENVIO_MOTO = "Envio por moto"
    private val RETIRO_LOCAL = "Retira en local"
    private var direccion: Direccion? = null
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    private var tieneDomicilios: Boolean = false
    private lateinit var idDireccion: String

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

    private fun getAddress() {
        //var direccionCompleta = ""
        val user = auth.currentUser
        val userRef = db.collection("usuarios").document(user!!.uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val idsDireccionesList = document.data!!["direcciones"] as ArrayList<String>
                    if (idsDireccionesList.size > 0 ) {
                        idDireccion = idsDireccionesList.get(0)
                        val direcRef = db.collection("direcciones").document(idDireccion)
                        direcRef.get()
                            .addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                                if (task.isSuccessful) {
                                    val documentDireccion: DocumentSnapshot? = task.getResult()
                                    if (documentDireccion != null) {
                                        //direccionCompleta = documentDireccion.getString("calle") + " " + documentDireccion.getString("numero")
                                        this.direccion = documentDireccion.toObject<Direccion>()
                                        //this.direccion = direccionCompleta
                                        this.tieneDomicilios = true
                                    } else {
                                        Log.d("LOGGER", "No existe el document")
                                    }
                                } else {
                                    Log.d("LOGGER", "Fallo por la sig exc:  ", task.exception)
                                }
                            })
                    } else {
                        //direccionCompleta = "Este usuario aun no tiene domicilios guardados"
                        //this.direccion = direccionCompleta
                        this.tieneDomicilios = false
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        getAddress()
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
                checkBoxLoPasoABuscar.isChecked = false
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
                showSnackbarDebeAgregarDomicilio()
            } else {
                if (this.envio!!.tipoEnvio == ENVIO_MOTO) {
                    if (this.direccion != null && this.idDireccion != null) {
                        val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToCheckAddressFragment(envio, direccion!!, idDireccion)
                        v.findNavController().navigate(action)
                    } else {
                        showSnackbarDebeAgregarDomicilio()
                    }
                } else {
                    val action = ShipmentMethodFragmentDirections.actionShipmentMethodFragmentToPaymentMethodFragment(envio!!)
                    v.findNavController().navigate(action)
                }
            }
        }
    }

    private fun showSnackbarDebeAgregarDomicilio() {
        Snackbar.make(rootLayout, getString(R.string.debe_agregar_domicilio), Snackbar.LENGTH_LONG).setAnimationMode(
            BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .setBackgroundTint(resources.getColor(R.color.rojo_denied, theme))
            .show()
    }
}