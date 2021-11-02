package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.ort.usanote.R

class ProductDescriptionFragment : Fragment() {
    lateinit var v : View
    companion object {
        fun newInstance() = ProductDescriptionFragment()
    }

    private lateinit var viewModel: ProductDescriptionViewModel
    private lateinit var btnToCart:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.product_description_fragment, container, false)
        btnToCartInit(v)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductDescriptionViewModel::class.java)
    }
    private fun btnToCartInit(v:View){
        btnToCart = v.findViewById(R.id.comprar)
        btnToCart.setOnClickListener{
            val action = ProductDescriptionFragmentDirections.actionProductDescriptionFragmentToCarritoFragment()
            v.findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()

        var imagen = v.findViewById<ImageView>(R.id.item_image)
        var nombre_producto = v.findViewById<TextView>(R.id.item_title)
        var desc = v.findViewById<TextView>(R.id.item_desc)
        var price = v.findViewById<TextView>(R.id.item_price)
        var spinner = v.findViewById<Spinner>(R.id.spinner_cantidad)
        val cant_imported = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad
        val array = arrayOfNulls<Number>(cant_imported)
        for (i in array.indices){
            array[i] = i +1
        }
        val adaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,array)
        spinner.adapter = adaptador

        imagen.setImageResource(ProductDescriptionFragmentArgs.fromBundle(requireArguments()).image)
        nombre_producto.text = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).title
        desc.text = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).description
        price.text = "$" + ProductDescriptionFragmentArgs.fromBundle(requireArguments()).price

    }

}