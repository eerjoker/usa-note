package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.adapters.MasCompradosAdapter
import com.ort.usanote.entities.DetalleOrden
import com.ort.usanote.entities.Product
import com.ort.usanote.viewModels.ProductosMasVistosViewModel

class ProductosMasCompradosFragment : Fragment() {

    companion object {
        fun newInstance() = ProductosMasCompradosFragment()
    }

    private lateinit var viewModel: ProductosMasVistosViewModel
    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MasCompradosAdapter
    private var productosList: MutableList<DetalleOrden> = mutableListOf()
    private lateinit var db : FirebaseFirestore
    private lateinit var productoItem: DetalleOrden
    private lateinit var product: Product
    private lateinit var btn:Button
    private var vendidas:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.productos_mas_comprados_fragment, container, false)
        db = FirebaseFirestore.getInstance()


        recyclerView(v)
        return v
    }


    fun recyclerView(v:View){
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(v.context)
        myAdapter = MasCompradosAdapter(productosList,requireContext(),vendidas)
        recyclerView.adapter = myAdapter
        EventChangeListener()
    }
    private fun  EventChangeListener() {
        var i = 0
        db.collection("detalleDeOrdenes").orderBy("product.nombre")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.d("Firebase Error",error.message.toString())
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            productoItem= dc.document.toObject(DetalleOrden::class.java)
                            if(productosList.size == 0){
                                productosList.add(productoItem)
                            }else{
                                if(productosList[i].producto.nombre == productoItem.producto.nombre){
                                    productosList[i].quantity = productosList[i].quantity + productoItem.quantity
                                }else{
                                    i++
                                    productosList.add(productoItem)
                                }
                            }
                        }
                    }
                    productosList.sortByDescending { it.quantity }
                    i = 0
                    var aux = mutableListOf<DetalleOrden>()
                    while (i < 3 && i < productosList.size){
                        aux.add(productosList[i])
                        i++
                    }
                    productosList.retainAll(aux)


                    myAdapter.notifyDataSetChanged()
                }
            })

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductosMasVistosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}