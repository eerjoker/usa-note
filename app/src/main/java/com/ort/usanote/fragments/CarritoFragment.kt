package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.adapters.ProductItemsAdapter
import com.ort.usanote.entities.Cart
import com.ort.usanote.entities.ProductItemRepository
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
    private lateinit var checkoutButton : Button
    private lateinit var cstrLayoutGoToCheckout : ConstraintLayout
    private lateinit var productItems : ProductItemRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.carrito_fragment, container, false)
        recProductItem = v.findViewById(R.id.recProductItem)
        txtSubtotalValue = v.findViewById(R.id.txtSubtotalValue)
        checkoutButton = v.findViewById(R.id.btnCheckout)
        cstrLayoutGoToCheckout = v.findViewById(R.id.constraintLayoutGoToCheckout)
        return v
    }

    override fun onStart() {
        super.onStart()
        productItems = CarritoFragmentArgs.fromBundle(requireArguments()).itemsCarrito

        if (productItems?.getProductItems().size > 0) {
            cart = Cart(productItems.getProductItems()) { subtotal ->
                setSubtotalValue(subtotal)
            }

            recProductItem.setHasFixedSize(true)
            recProductItem.layoutManager = LinearLayoutManager(context)
            recProductItem.adapter = ProductItemsAdapter(cart, requireContext())

            setSubtotalValue(cart.calculateSubtotal())
            checkoutButton.setOnClickListener {
                val action = CarritoFragmentDirections.actionCarritoFragmentToShipmentMethodFragment(productItems)
                v.findNavController().navigate(action)
            }
        } else {
            recProductItem.removeAllViews()
            cstrLayoutGoToCheckout.removeAllViews()
            val txtEmptyCart = TextView(requireContext())
            txtEmptyCart.text = getString(R.string.empty_cart)
            val frameLayoutCart = v.findViewById<FrameLayout>(R.id.frameLayoutCart)
            frameLayoutCart.addView(txtEmptyCart)
        }
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