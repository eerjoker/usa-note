package com.ort.usanote.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.activities.SearchActivity
import com.ort.usanote.adapters.ProductAdapter
import com.ort.usanote.entities.*
import com.ort.usanote.viewModels.ProductosViewModel

class ProductosFragment : Fragment() {

    companion object {
        fun newInstance() = ProductosFragment()
    }

    private lateinit var viewModel: ProductosViewModel
    private lateinit var rootView : View
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemsCarrito : ProductItemRepository
    private lateinit var myAdapter : ProductAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var productList : MutableList<Product>
    private lateinit var swipeRefreshView : SwipeRefreshLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.productos_fragment, container, false)
        db = FirebaseFirestore.getInstance()
        itemsCarrito = (activity as SearchActivity).itemsCarrito
        productList = (activity as SearchActivity).productListActivity
        productList.clear()
        recyclerView(rootView,requireContext())
        swipeRefreshView(rootView,requireContext())
        return  rootView
    }

    private fun  EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        var producto:Product
        db.collection("productos")
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.d("Firebase Error",error.message.toString())
                    }
                    for(dc:DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            producto = dc.document.toObject(Product::class.java)
                            producto.idProducto = dc.document.id
                            productList.add(producto)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })
    }

    private fun swipeRefreshView(rootView: View, context: Context){
        swipeRefreshView = rootView.findViewById(R.id.refres_layout)
        swipeRefreshView.setOnRefreshListener {
            productList.clear()
            EventChangeListener()
            swipeRefreshView.isRefreshing = false
        }
    }
    private fun recyclerView(rootView: View, context: Context){
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        myAdapter = ProductAdapter(productList,context){
            onItemClick(it)
        }
        recyclerView.adapter = myAdapter
        EventChangeListener()


    }

    private fun onItemClick(pos:Int){
        var idProducto = productList[pos].idProducto
        var title = productList[pos].nombre
        var description = productList[pos].description
        var price = productList[pos].price
        var image = productList[pos].imageUrl
        var cant = productList[pos].stock
        var categoria = productList[pos].categoria
        var marca = productList[pos].marca
        val action = ProductosFragmentDirections.actionProductosFragmentToProductDescriptionFragment(title,description,price,image,cant,itemsCarrito,idProducto, categoria, marca)
        rootView.findNavController().navigate(action)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}


