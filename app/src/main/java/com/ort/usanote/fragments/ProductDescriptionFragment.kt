package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.activities.SearchActivity
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.ProductItemRepository

class ProductDescriptionFragment : Fragment() {
    lateinit var v : View
    companion object {
        fun newInstance() = ProductDescriptionFragment()
    }

    private lateinit var viewModel: ProductDescriptionViewModel
    private lateinit var btnToCart:Button
    private lateinit var btnAddCart: Button
    private lateinit var itemsCarrito : ProductItemRepository
    private lateinit var db : FirebaseFirestore
    private var cant_imported = 0
    private lateinit var idProducto: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.product_description_fragment, container, false)
        db = FirebaseFirestore.getInstance()
        btnToCartInit(v)
        btnAddCart(v)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductDescriptionViewModel::class.java)
    }
    private fun btnAddCart(v:View){
        btnAddCart = v.findViewById(R.id.add_cart)
        btnAddCart.setOnClickListener{
            itemsCarrito = addToCart(v)
        }
    }
    private fun btnToCartInit(v:View){
        btnToCart = v.findViewById(R.id.comprar)
        btnToCart.setOnClickListener{
            itemsCarrito = (activity as SearchActivity).itemsCarrito
            val action = ProductDescriptionFragmentDirections.actionProductDescriptionFragmentToCarritoFragment(itemsCarrito)
            v.findNavController().navigate(action)
        }
    }

    fun addToCart(v:View):ProductItemRepository{
        val spinner = v.findViewById<Spinner>(R.id.spinner_cantidad)
        idProducto = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).idProducto
        val title = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).title
        val desc = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).description
        val price = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).price
        val image = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).image
        val stock = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad
        val categoria = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).categoria
        val marca = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).marca
        var ret = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).itemsCarrito
        ret.addProductItem(Product(idProducto,title,desc,price,stock,categoria,marca,image), spinner.selectedItem as Int)
        var nuevoStock = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad - spinner.selectedItem as Int
        if (nuevoStock < 0){
            nuevoStock = 0
        }
        val productoActualizar = db.collection("productos").document(idProducto)
        productoActualizar.update("stock",nuevoStock)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
        //
        cant_imported = nuevoStock
        val array = arrayOfNulls<Number>(cant_imported)
        for (i in array.indices){
            array[i] = i +1
        }
        val adaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,array)
        spinner.adapter = adaptador
        //

        return ret
    }

    override fun onStart() {
        super.onStart()

        var nombre_producto = v.findViewById<TextView>(R.id.item_title)
        var desc = v.findViewById<TextView>(R.id.item_desc)
        var price = v.findViewById<TextView>(R.id.item_price)
        var spinner = v.findViewById<Spinner>(R.id.spinner_cantidad)
        if (cant_imported == 0){
            cant_imported = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad
        }
        val array = arrayOfNulls<Number>(cant_imported)
        for (i in array.indices){
            array[i] = i +1
        }
        val adaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,array)
        spinner.adapter = adaptador

        var imgProductItem : ImageView = v.findViewById(R.id.item_image)
        Glide
            .with(requireContext())
            .load(ProductDescriptionFragmentArgs.fromBundle(requireArguments()).image)
            .centerInside()
            .into(imgProductItem)
        nombre_producto.text = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).title
        desc.text = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).description
        price.text = "$" + ProductDescriptionFragmentArgs.fromBundle(requireArguments()).price

    }

}