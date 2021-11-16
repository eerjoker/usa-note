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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.CheckoutAdapter
import com.ort.usanote.entities.*
import com.ort.usanote.fragments.direccion.DireccionFragmentDirections
import com.ort.usanote.viewModels.PurchaseConfirmationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class PurchaseConfirmationFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    private lateinit var productItems : ProductItemRepository
    private lateinit var recyclerView : RecyclerView
    private lateinit var itemsCarrito : ProductItemRepository
    private val COSTO_ENVIO = 300.0
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    private lateinit var envio: Envio

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

    @SuppressLint("SetTextI18n")
    fun setMetodoEnvio(metodoEnvio: String) {
        val metodoEnvioTxt : TextView = v.findViewById(R.id.textViewMetodoDeEnvio)
        metodoEnvioTxt.text = "$metodoEnvio"
    }

    @SuppressLint("SetTextI18n")
    fun setTiempoEstimado(tiempoEstimado: Int) {
        val tiempoEstimadoTxt : TextView = v.findViewById(R.id.textViewTiempoEstimado)
        tiempoEstimadoTxt.text = "$tiempoEstimado minutos"
    }

    @SuppressLint("SetTextI18n")
    fun setCosto(costo: Double) {
        val costoTxt : TextView = v.findViewById(R.id.textViewCosto)
        val costoEnvioTxt : TextView = v.findViewById(R.id.textViewEnvio)
        costoTxt.text = "$$costo"
        costoEnvioTxt.text = "$$costo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.purchase_confirmation_fragment, container, false)
        btnContinue = v.findViewById(R.id.buttonContinue)
        itemsCarrito = (activity as MainActivity).itemsCarrito
        setSubtotal(calculateSubtotalItemsCarrito(itemsCarrito).toString())
        envio = PurchaseConfirmationFragmentArgs.fromBundle(requireArguments()).envio!!
        setTotalAPagar(calculateTotalAPagar().toString())
        setMetodoEnvio(this.envio.tipoEnvio)
        setTiempoEstimado(this.envio.minutosEstimados)
        setCosto(this.envio.costoEnvio)
        return v
    }

    private  suspend fun updateDB () {
        val user = auth.currentUser
        val cantProductos = calculateCantProductos(itemsCarrito)
        val subtotal = calculateSubtotalItemsCarrito(itemsCarrito)
        val total = calculateTotalAPagar()
        var direccionEntregaCompleta : String = "Acordar con el vendedor"
        var entregadoA : String

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
                //if(this.envio.tipoEnvio == "")
                val usuario = fetchUser(user!!.uid)
                // con los datos del user me traigo las direcciones,mail
                entregadoA = usuario.email.toString()
                val direcciones = usuario.direcciones //lista
                if(direcciones != null){
                    if(direcciones!!.size > 0){
                        val direccion = direcciones[0]
                        //deberia buscar si encuentro esta direccion
                        val direccionDb = fetchDireccion(direccion)
                        direccionEntregaCompleta = direccionDb.calle.toString() + direccionDb.numero.toString()
                    }
                }
                //debo crear la orden
                //primero para saber el numero de id que ponerle me traigo la ultima orden con el id mas alto
                var ultimaOrden : Orden
                var idNuevaOrden = 0
                try{
                    ultimaOrden = fetchUltimaOrden()
                    idNuevaOrden = ultimaOrden.numeroOrden + 1
                }catch (e:Exception){
                    Log.d("Error", e.toString())
                }
                //me creo la nueva orden
                val nuevaOrden = Orden(
                    idNuevaOrden,
                    cantProductos,
                    subtotal,
                    total,
                    entregadoA,
                    envio,
                    direccionEntregaCompleta,
                    user!!.uid
                )
                //la agrego a la colecction
                val idOrden = addOrdenToColection(nuevaOrden)
                // me creo los detalles de ordenes
                val detalleOrdenes: MutableList<DetalleOrden> = mutableListOf()
                itemsCarrito.getProductItems().forEach{
                    val detalleOrden = DetalleOrden(
                        idOrden,
                        it.producto,
                        it.quantity
                    )
                    detalleOrdenes.add(detalleOrden)
                }
                //agrego las detalles de ordenes
                addDetalleOrden(detalleOrdenes)
                //le agrego al usuario el pedido
                updatePedidosUsuario(usuario.id!!,idOrden)
            }
    }

    private suspend fun updatePedidosUsuario(uid: String,orden:String){
        db.collection("usuarios").document(uid).update("pedidos",FieldValue.arrayUnion(orden)).await()

    }

    private suspend fun addDetalleOrden(detalleOrdenes: MutableList<DetalleOrden>){
        for (detalle in detalleOrdenes){
            db.collection("detalleDeOrdenes").add(detalle)
                .addOnSuccessListener { Log.d("Succes","Todo ok ") }
                .addOnFailureListener { Log.d("Error","Error al guardar la nueva orden") }
                .await()
        }
    }

    private suspend fun addOrdenToColection(nuevaOrden: Orden): String {
        lateinit var ret: String
        db.collection("ordenes").add(nuevaOrden)
            .addOnSuccessListener { ret = it.id }
            .addOnFailureListener { Log.d("Error","Error al guardar la nueva orden") }
            .await()
        return ret
    }

    private suspend fun fetchUltimaOrden(): Orden {
        val ordenDb = db.collection("ordenes").orderBy("numeroOrden",Query.Direction.DESCENDING).limit(1).get().await()
        var orden = Orden()
        if(orden != null){
            orden = ordenDb.documents[0].toObject(Orden::class.java)!!
        }else{
            throw Exception("no se pudo encontrar ordenes")
        }
        return orden
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
        val total = subtotal + envio.costoEnvio
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
        envio = PurchaseConfirmationFragmentArgs.fromBundle(requireArguments()).envio!!
        productItems = itemsCarrito
        recyclerView(v, requireContext())
        var navController = v.findNavController()
        btnContinue.setOnClickListener {
            val scope = CoroutineScope(Dispatchers.Main)
                .launch {
                    updateDB()

                    val action = PurchaseConfirmationFragmentDirections.actionPurchaseConfirmationFragmentToPurchaseFinishedFragment()
                    navController.navigate(action)
                }


        }
    }

}
//            { goToDireccion ->
//                if (goToDireccion) {
//                    var mainActivity = (activity as MainActivity)
//                    mainActivity.actionForRedirection = DireccionFragmentDirections.actionDireccionFragmentToPurchaseConfirmationFragment(envio)
//                    mainActivity.alertDangerMessage = getString(R.string.direccion_missing)
//                    val action = PurchaseConfirmationFragmentDirections.actionPurchaseConfirmationFragmentToDireccionFragment()
//                    v.findNavController().navigate(action)
//                } else {
//                    val action = PurchaseConfirmationFragmentDirections.actionPurchaseConfirmationFragmentToPurchaseFinishedFragment()
//                    navController.navigate(action)
//                }
//            }

//val userRef = db.collection("usuarios").document(user!!.uid)
//userRef.get()
//.addOnSuccessListener { document ->
//    if (document != null) {
//        entregadoA = document.data!!["email"] as String // Obteniendo mail
//        val direcciones = document.data!!["direcciones"] as ArrayList<String>
//        if (direcciones.size == 0) {
//            cb(true)
//        } else {
//            idDireccionEntrega = direcciones.get(0) //Obteniendo primer id en array de idsDirecciones
//            val envio = this.envio //Objeto Envio de momento hardcodeado, deberia ser recibido por parametro segun lo elegido en shipmentFragment
//            val direcRef = db.collection("direcciones").document(idDireccionEntrega)
//            if (direcRef != null) {
//                direcRef.get()
//                    .addOnSuccessListener { document ->
//                        if (document != null) {
//                            Log.d("Test", document.data.toString())
//                            if(document.data.toString() != null){
//                                direccionEntregaCompleta = ""
//                            }else{
//                                direccionEntregaCompleta = document.data!!["calle"].toString() + document.data!!["numero"].toString()
//                            }
//                            //Direccion concatenando campos segun idDirecciones
//                            val ordenesRef = db.collection("ordenes")
//                            val numeroOrdenMayor = ordenesRef.orderBy("numeroOrden", Query.Direction.DESCENDING).limit(1)
//                            numeroOrdenMayor.get()
//                                .addOnSuccessListener { document ->
//                                    if (document != null) {
//                                        val numeroDeOrden = (document.documents.get(0).get("numeroOrden") as Long).toInt() + 1 //Numero de orden mayor en tabla Orden + 1
//                                        Log.d("Numero de Orden = ", "Este: " +  document.documents.get(0).get("numeroOrden"))
//                                        val dbOrder: Orden = Orden(
//                                            numeroDeOrden,
//                                            cantProductos,
//                                            subtotal,
//                                            total,
//                                            entregadoA,
//                                            envio,
//                                            direccionEntregaCompleta,
//                                            user.uid
//                                        )
//                                        db.collection("ordenes").add(dbOrder).addOnCompleteListener() {
//                                            if (it.isSuccessful) {
//                                                Log.d("Orden", "Se pudo guardar en la BD de ordenes")
//                                                val idDetalleOrden = it.result?.id.toString()
//                                                itemsCarrito.getProductItems().forEach {
//                                                    val detalleOrden = DetalleOrden(
//                                                        idDetalleOrden,
//                                                        it.producto,
//                                                        it.quantity
//                                                    )
//                                                    db.collection("detalleDeOrdenes").add(detalleOrden)
//                                                        .addOnSuccessListener {
//                                                            cb(false)
//                                                        }
//                                                }
//                                                val pedidosUserAActualizar = db.collection("usuarios").document(user.uid)
//                                                pedidosUserAActualizar.update("pedidos", FieldValue.arrayUnion(idDetalleOrden))
//                                            } else {
//                                                Log.d(
//                                                    "Orden",
//                                                    "No se ha podido guardar en la BD de ordenes"
//                                                )
//                                            }
//                                            db.collection("envios").add(this.envio)
//                                        }
//                                    }
//                                }
//                        } else {
//                            Log.d(TAG, "No existe determinado document")
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.d(TAG, "get failed with ", exception)
//                    }
//
//            } else {
//                Log.d(TAG, "No such document")
//            }
//        }
//    }
//}