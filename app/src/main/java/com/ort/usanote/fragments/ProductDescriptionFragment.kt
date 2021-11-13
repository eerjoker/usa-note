package com.ort.usanote.fragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.ProductItemRepository
import com.ort.usanote.viewModels.ProductDescriptionViewModel

class ProductDescriptionFragment : Fragment() {
    lateinit var v : View
    companion object {
        fun newInstance() = ProductDescriptionFragment()
    }
    private lateinit var viewModel: ProductDescriptionViewModel
    private lateinit var spinner: Spinner
    private lateinit var btnToCart:Button
    private lateinit var btnAddCart: Button
    private lateinit var db : FirebaseFirestore
    private var cant_imported:Int  = 0
    private lateinit var idProducto: String
    private var sinStock = false
    private var current_stock = MutableLiveData<Int>(0)
    private var changedSpinner = MutableLiveData<Spinner>()


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
            addToCart(v)
        }
    }
    private fun btnToCartInit(v:View){
        btnToCart = v.findViewById(R.id.comprar)
        btnToCart.setOnClickListener{
            val action = ProductDescriptionFragmentDirections.actionProductDescriptionFragmentToCarritoFragment()
            v.findNavController().navigate(action)
        }
    }

    fun addToCart(v:View){
        val spinner = v.findViewById<Spinner>(R.id.spinner_cantidad)
        val producto = crearProducto()
        (activity as MainActivity).itemsCarrito.addProductItem(producto, spinner.selectedItem as Int)
        //***Actualizar al nuevo stock

        var nuevoStock = cant_imported - spinner.selectedItem as Int
        if(nuevoStock == 0){
            bloquearBtnYSpiner()
        }
        val productoActualizar = db.collection("productos").document(idProducto)
        productoActualizar.update("stock",nuevoStock)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
        current_stock.value = cant_imported

        if (cant_imported !=0){
//            val array = arrayOfNulls<Number>(cant_imported)
//            for (i in array.indices){
//                array[i] = i +1
//            }
//            val adaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,array)
//            spinner.adapter = adaptador

        }else{
            bloquearBtnYSpiner()
            sinStock = true
        }

    }
    private fun actualizarStock(){

    }
    private fun crearProducto(): Product {

        idProducto = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).idProducto
        val title = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).title
        val desc = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).description
        val price = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).price
        val image = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).image
        val stock = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad
        val categoria = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).categoria
        val marca = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).marca
        val cantidadVisitas = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidadVisitas
        return Product(idProducto,title,desc,price,stock,categoria,marca,image, cantidadVisitas)
    }

    override fun onStart() {
        super.onStart()

        current_stock.observe(this,Observer{
            val array = arrayOfNulls<Number>(it)
            for (i in array.indices){
                array[i] = i +1
            }
            val adaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,array)
            spinner.adapter = adaptador
            cant_imported = it

            if(cant_imported > 0){
                habilitarBtnYSpinner()
            }else if(cant_imported == 0){
                bloquearBtnYSpiner()
            }
        })
        idProducto = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).idProducto
        var nombre_producto = v.findViewById<TextView>(R.id.item_title)
        var desc = v.findViewById<TextView>(R.id.item_desc)
        var price = v.findViewById<TextView>(R.id.item_price)
        sinStock = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad == 0
        spinner = v.findViewById(R.id.spinner_cantidad)
        val producto = db.collection("productos").document(idProducto)
        producto.addSnapshotListener{
                snapshot,e ->
            if(e!=null){
                Log.d("Error","Listen failed",e)
                return@addSnapshotListener
            }
            if(snapshot != null){
                val producto = snapshot.toObject(Product::class.java)!!
                current_stock.value = producto.stock
            }
        }

        if (!sinStock && cant_imported == 0){
            cant_imported = ProductDescriptionFragmentArgs.fromBundle(requireArguments()).cantidad
        }else{
            if(cant_imported == 0){
                bloquearBtnYSpiner()
            }
        }

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

    fun bloquearBtnYSpiner(){
        btnAddCart.isEnabled = false
        btnAddCart.isClickable = false
        btnAddCart.text = "No hay stock"
        btnAddCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.greyStrong))
        spinner.isEnabled = false
        spinner.isClickable = false
    }

    fun habilitarBtnYSpinner(){
        btnAddCart.isEnabled = true
        btnAddCart.isClickable = true
        btnAddCart.text = "Agregar al carrito"
        btnAddCart.setBackgroundColor(Color.parseColor("#2DDC53"))
        spinner.isEnabled = true
        spinner.isClickable = true
    }

}