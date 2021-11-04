package com.ort.usanote.fragments

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.activities.SearchActivity
import com.ort.usanote.adapters.CheckoutAdapter
import com.ort.usanote.entities.ProductItemRepository
import com.ort.usanote.viewModels.PurchaseConfirmationViewModel

class PurchaseConfirmationFragment : Fragment() {

    lateinit var btnContinue : Button
    lateinit var v: View
    private lateinit var productItems : ProductItemRepository
    private lateinit var recyclerView : RecyclerView
    private lateinit var itemsCarrito : ProductItemRepository
    private val COSTO_ENVIO = 300

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.purchase_confirmation_fragment, container, false)
        btnContinue = v.findViewById(R.id.buttonContinue)
        itemsCarrito = (activity as SearchActivity).itemsCarrito
        setSubtotal(calculateSubtotalItemsCarrito(itemsCarrito).toString())
        setTotalAPagar(calculateTotalAPagar().toString())
        return v
    }

    private fun calculateSubtotalItemsCarrito (productItems : ProductItemRepository) : Double {
        var total : Double = 0.0
        productItems.getProductItems().forEach {
            total += it.calculateSubtotal()
        }
        return total
    }

    private fun calculateTotalAPagar() : Double {
        val subtotal = this.calculateSubtotalItemsCarrito(itemsCarrito)
        val total = subtotal + COSTO_ENVIO
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

        productItems = PurchaseConfirmationFragmentArgs.fromBundle(requireArguments()).productItems
        recyclerView(v, requireContext())
        btnContinue.setOnClickListener {
            val action = PurchaseConfirmationFragmentDirections.actionPurchaseConfirmationFragmentToPurchaseFinishedFragment()
            v.findNavController().navigate(action)
        }
    }

}