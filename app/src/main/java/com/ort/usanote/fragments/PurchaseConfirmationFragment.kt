package com.ort.usanote.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.CheckoutAdapter
import com.ort.usanote.entities.DetalleOrden
import com.ort.usanote.entities.Envio
import com.ort.usanote.entities.Orden
import com.ort.usanote.entities.ProductItemRepository
import com.ort.usanote.viewModels.PurchaseConfirmationViewModel

class PurchaseConfirmationFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    private lateinit var productItems : ProductItemRepository
    private lateinit var recyclerView : RecyclerView
    private lateinit var itemsCarrito : ProductItemRepository
    private val COSTO_ENVIO = 300.0
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    companion object {
        fun newInstance() = PurchaseConfirmationFragment()
    }

    private lateinit var viewModel: PurchaseConfirmationViewModel

    @SuppressLint("SetTextI18n")
    fun setSubtotal(subtotal: String) {
        val subtotalTxt : TextView = v.findViewById(R.id.subtotal)
        subtotalTxt.text = "$$subtotal"
    }

    @SuppressLint("SetTextI18n")
    fun setTotalAPagar(totalAPagar: String) {
        val totalAPagarTxt : TextView = v.findViewById(R.id.totalAPagar)
        totalAPagarTxt.text = "$$totalAPagar"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.purchase_confirmation_fragment, container, false)
        btnContinue = v.findViewById(R.id.buttonContinue)
        itemsCarrito = (activity as MainActivity).itemsCarrito
        setSubtotal(calculateSubtotalItemsCarrito(itemsCarrito).toString())
        setTotalAPagar(calculateTotalAPagar().toString())
        return v
    }

    private fun updateDB () {
        val user = auth.currentUser
        val cantProductos = calculateCantProductos(itemsCarrito)
        val subtotal = calculateSubtotalItemsCarrito(itemsCarrito)
        val total = calculateTotalAPagar()
        var idDireccionEntrega : String
        var direccionEntregaCompleta : String
        var entregadoA : String


        //  BUSCAR EMAIL DE USER y direccion de entrega segun idDireccion de dicho user ------------------------------------------------->
        val userRef = db.collection("usuarios").document(user!!.uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    entregadoA = document.data!!["email"] as String // Obteniendo mail
                    idDireccionEntrega = (document.data!!["direcciones"] as ArrayList<String>).get(0) //Obteniendo primer id en array de idsDirecciones
                    val envio: Envio = Envio(30, "Moto", COSTO_ENVIO) //Objeto Envio de momento hardcodeado, deberia ser recibido por parametro segun lo elegido en shipmentFragment
                    val direcRef = db.collection("direcciones").document(idDireccionEntrega)
                    if (direcRef != null) {
                        direcRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    direccionEntregaCompleta = document.data!!["calle"].toString() + document.data!!["numero"].toString() //Direccion concatenando campos segun idDirecciones
                                    val ordenesRef = db.collection("ordenes")
                                    val numeroOrdenMayor = ordenesRef.orderBy("numeroOrden", Query.Direction.DESCENDING).limit(1)
                                    numeroOrdenMayor.get()
                                        .addOnSuccessListener { document ->
                                            if (document != null) {
                                                val numeroDeOrden = (document.documents.get(0).get("numeroOrden") as Long).toInt() + 1 //Numero de orden mayor en tabla Orden + 1
                                                Log.d("Numero de Orden = ", "Este: " +  document.documents.get(0).get("numeroOrden"))
                                                val dbOrder: Orden = Orden(
                                                    numeroDeOrden,
                                                    cantProductos,
                                                    subtotal,
                                                    total,
                                                    entregadoA,
                                                    envio,
                                                    direccionEntregaCompleta,
                                                    user.uid
                                                )
                                                db.collection("ordenes").add(dbOrder).addOnCompleteListener() {
                                                    if (it.isSuccessful) {
                                                        Log.d("Orden", "Se pudo guardar en la BD de ordenes")
                                                        val idDetalleOrden = it.result?.id.toString()
                                                        itemsCarrito.getProductItems().forEach {
                                                            val detalleOrden: DetalleOrden = DetalleOrden(
                                                                idDetalleOrden,
                                                                it.product,
                                                                it.quantity
                                                                )
                                                            db.collection("detalleDeOrdenes").add(detalleOrden)
                                                        }
                                                    } else {
                                                        Log.d(
                                                            "Orden",
                                                            "No se ha podido guardar en la BD de ordenes"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                } else {
                                    Log.d(TAG, "No existe determinado document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "get failed with ", exception)
                            }

                    } else {
                        Log.d(TAG, "No such document")
                    }

                }
            }
    }

    private fun calculateCantProductos (productItems: ProductItemRepository) : Int {
        var cantTotal = 0
        productItems.getProductItems().forEach {
            cantTotal += it.quantity
        }
        return cantTotal
    }

    private fun calculateSubtotalItemsCarrito (productItems : ProductItemRepository) : Double {
        var total  = 0.0
        productItems.getProductItems().forEach {
            total += it.calculateSubtotal()
        }
        return total
    }

    private fun calculateTotalAPagar() : Double {
        val subtotal = this.calculateSubtotalItemsCarrito(itemsCarrito)
        val total = subtotal + COSTO_ENVIO
        return total
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseConfirmationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun recyclerView(rootView: View, context: Context) {
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recProductsCheckout)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        recyclerView.adapter = CheckoutAdapter(itemsCarrito, context)
    }

    override fun onStart() {
        super.onStart()

        productItems = itemsCarrito
        recyclerView(v, requireContext())
        btnContinue.setOnClickListener {
            updateDB()
            val action = PurchaseConfirmationFragmentDirections.actionPurchaseConfirmationFragmentToPurchaseFinishedFragment()
            v.findNavController().navigate(action)
        }
    }

}