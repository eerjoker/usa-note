package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
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
    private lateinit var itemsCarrito : ProductItemRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.carrito_fragment, container, false)
        recProductItem = v.findViewById(R.id.recProductItem)
        itemsCarrito = (activity as MainActivity).itemsCarrito
        txtSubtotalValue = v.findViewById(R.id.txtSubtotalValue)
        checkoutButton = v.findViewById(R.id.btnCheckout)
        cstrLayoutGoToCheckout = v.findViewById(R.id.constraintLayoutGoToCheckout)
        return v
    }

    override fun onStart() {
        super.onStart()
        val productos = itemsCarrito
        if (productos!!.getProductItems().size > 0) {
            cart = Cart(productos.getProductItems()) { subtotal, size ->
                setSubtotalValue(subtotal)
                if (size == 0) {
                    clearCart()
                }
            }

            recProductItem.setHasFixedSize(true)
            recProductItem.layoutManager = LinearLayoutManager(context)
            recProductItem.adapter = ProductItemsAdapter(cart, requireContext())

            setSubtotalValue(cart.calculateSubtotal())
            checkoutButton.setOnClickListener {
                val action = CarritoFragmentDirections.actionCarritoFragmentToShipmentMethodFragment()
                v.findNavController().navigate(action)
            }
        } else {
            clearCart()
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

    fun clearCart() {
        recProductItem.removeAllViews()
        cstrLayoutGoToCheckout.removeAllViews()
        val simpleTextColor = ContextCompat.getColor(requireContext(), R.color.design_default_color_on_secondary)
        val txtEmptyCart = TextView(requireContext())
        txtEmptyCart.id = ViewCompat.generateViewId()
        txtEmptyCart.setPadding(50, 50, 50, 50)
        txtEmptyCart.setTextColor(simpleTextColor)
        txtEmptyCart.text = getString(R.string.empty_cart)

        val constraintSet = ConstraintSet()
        constraintSet.clone(cstrLayoutGoToCheckout)
        constraintSet.connect(R.id.constraintLayoutGoToCheckout, ConstraintSet.LEFT, txtEmptyCart.id, ConstraintSet.LEFT, 0)
        constraintSet.connect(R.id.constraintLayoutGoToCheckout, ConstraintSet.RIGHT, txtEmptyCart.id, ConstraintSet.RIGHT, 0)
        constraintSet.connect(R.id.constraintLayoutGoToCheckout, ConstraintSet.TOP, txtEmptyCart.id, ConstraintSet.TOP, 0)
        constraintSet.connect(R.id.constraintLayoutGoToCheckout, ConstraintSet.BOTTOM, txtEmptyCart.id, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(cstrLayoutGoToCheckout)

        cstrLayoutGoToCheckout.addView(txtEmptyCart)
    }
}