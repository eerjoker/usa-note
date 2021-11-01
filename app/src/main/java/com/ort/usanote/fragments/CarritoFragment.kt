package com.ort.usanote.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.adapters.ProductItemsAdapter
import com.ort.usanote.entities.Cart
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.ProductItem
import com.ort.usanote.viewModels.CarritoViewModel

class CarritoFragment : Fragment() {

    companion object {
        fun newInstance() = CarritoFragment()
    }

    private lateinit var viewModel: CarritoViewModel
    private lateinit var v : View
    private lateinit var recProductItem : RecyclerView
    private lateinit var cart : Cart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.carrito_fragment, container, false)
        recProductItem = v.findViewById(R.id.recProductItem)
        return v
    }

    override fun onStart() {
        super.onStart()

        var preferences = requireContext().getSharedPreferences("cart", Context.MODE_PRIVATE)
        cart = Cart(preferences)

        recProductItem.setHasFixedSize(true)
        recProductItem.layoutManager = LinearLayoutManager(context)
        recProductItem.adapter = ProductItemsAdapter(cart.getProductItems(), requireContext()) { index ->
            onItemClick(index)
        }
    }

    fun onItemClick(pos : Int) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarritoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}