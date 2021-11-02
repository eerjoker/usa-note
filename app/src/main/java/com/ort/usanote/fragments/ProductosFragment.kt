package com.ort.usanote.fragments

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
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.ProductForListProducts
import com.ort.usanote.viewModels.ProductosViewModel

class ProductosFragment : Fragment() {

    companion object {
        fun newInstance() = ProductosFragment()
    }

    private lateinit var viewModel: ProductosViewModel
    private lateinit var rootView : View
    private lateinit var recyclerView: RecyclerView
    var productList = ArrayList<ProductForListProducts>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.productos_fragment, container, false)

        recyclerView(rootView)
        itemProductList()
        return  rootView
    }

    private fun recyclerView(rootView: View){
        productList = ArrayList()
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        recyclerView.adapter = ProductAdapter(productList){
            onItemClick(it)
        }

    }
    private fun itemProductList(){
        productList.add(ProductForListProducts("test 1","description 1","1.0",R.drawable.combo,2))
        productList.add(ProductForListProducts("test 2","description 2","2.0",R.drawable.gabo_violeta,3))
        productList.add(ProductForListProducts("test 3","description 3","3.0",R.drawable.gaborgb,3))
        productList.add(ProductForListProducts("test 4","description 4","4.0",R.drawable.mouse,6))
        productList.add(ProductForListProducts("test 5","description 5","5.0",R.drawable.setup,5))
        productList.add(ProductForListProducts("test 6","description 6","6.0",R.drawable.combo,1))
    }

    private fun onItemClick(pos:Int){
        var title = productList[pos].title
        var description = productList[pos].description
        var price = productList[pos].price
        var image = productList[pos].imagen
        var cant = productList[pos].cantidad
        val action = ProductosFragmentDirections.actionProductosFragmentToProductDescriptionFragment(title,description,price,image,cant)
        rootView.findNavController().navigate(action)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}