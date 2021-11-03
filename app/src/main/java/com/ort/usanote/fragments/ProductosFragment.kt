package com.ort.usanote.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
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
    private var productList : MutableList<Product> = mutableListOf()
    private lateinit var itemsCarrito : ProductItemRepository



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.productos_fragment, container, false)
        itemProductList()
        itemsCarrito = ProductItemRepository()
        recyclerView(rootView,requireContext())

        return  rootView
    }

    private fun recyclerView(rootView: View, context: Context){
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        recyclerView.adapter = ProductAdapter(productList,context){
            onItemClick(it)
        }

    }
    private fun itemProductList(){
        var url = "https://edu-delitech2.odoo.com/web/image/product.template/1/image_1024?unique=fb5c381"
        productList = ProductsRepository().getProductItems()
    }

    private fun onItemClick(pos:Int){
        var title = productList[pos].title
        var description = productList[pos].description
        var price = productList[pos].price
        var image = productList[pos].imageUrl
        var cant = productList[pos].stock
        val action = ProductosFragmentDirections.actionProductosFragmentToProductDescriptionFragment(title,description,price,image,cant,itemsCarrito)
        rootView.findNavController().navigate(action)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}