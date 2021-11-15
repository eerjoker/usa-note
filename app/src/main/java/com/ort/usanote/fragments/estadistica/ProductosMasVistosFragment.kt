package com.ort.usanote.fragments.estadistica

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.adapters.MasVistosAdapter
import com.ort.usanote.entities.Product
import com.ort.usanote.viewModels.estadistica.ProductosMasVistosViewModel

class ProductosMasVistosFragment : Fragment() {

    companion object {
        fun newInstance() = ProductosMasVistosFragment()
    }

    private lateinit var viewModel: ProductosMasVistosViewModel
    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MasVistosAdapter
    private var productosList: MutableList<Product> = mutableListOf()
    private lateinit var db : FirebaseFirestore
    private lateinit var producto: Product
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        v = inflater.inflate(R.layout.productos_mas_vistos_fragment, container, false)
        recyclerView(v)
        return v
    }
    fun recyclerView(v:View){
        recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(v.context)
        myAdapter = MasVistosAdapter(productosList,requireContext())
        recyclerView.adapter = myAdapter
        EventChangeListener()
    }
    private fun  EventChangeListener() {
        db.collection("productos").orderBy("cantidadVisitas", Query.Direction.DESCENDING).limit(5)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.d("Firebase Error",error.message.toString())
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            producto = dc.document.toObject(Product::class.java)
                            producto.idProducto = dc.document.id
                            productosList.add(producto)
                        }
                    }
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