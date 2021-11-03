package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.adapters.ProductItemsAdapter
import com.ort.usanote.entities.Cart
import com.ort.usanote.viewModels.CarritoViewModel

class CarritoFragment : Fragment() {

    companion object {
        fun newInstance() = CarritoFragment()
    }

    private lateinit var viewModel: CarritoViewModel
    private lateinit var v : View
    private lateinit var recProductItem : RecyclerView
    private lateinit var txtSubtotalValue : TextView
    private lateinit var cart : Cart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.carrito_fragment, container, false)
        recProductItem = v.findViewById(R.id.recProductItem)
        txtSubtotalValue = v.findViewById(R.id.txtSubtotalValue)
        return v
    }

    override fun onStart() {
        super.onStart()
        val productos = CarritoFragmentArgs.fromBundle(requireArguments()).itemsCarrito
        print(productos)
        cart = Cart(productos.getProductItems()) { subtotal ->
            setSubtotalValue(subtotal)
        }

        recProductItem.setHasFixedSize(true)
        recProductItem.layoutManager = LinearLayoutManager(context)
        recProductItem.adapter = ProductItemsAdapter(cart, requireContext())

        setSubtotalValue(cart.calculateSubtotal())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarritoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun setSubtotalValue(subtotal : Double) {
        txtSubtotalValue.text = "$" + subtotal.toString()
    }
}