package com.ort.usanote.fragments.productos

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.ModalAdapter
import com.ort.usanote.viewModels.auth.LoginViewModel
import com.ort.usanote.viewModels.productos.ProductDescriptionViewModel


class ProductDescriptionFragment : Fragment() {
    lateinit var v : View
    companion object {
        fun newInstance() = ProductDescriptionFragment()
    }
    private val viewModelPD: ProductDescriptionViewModel by viewModels()
    private val viewModelLogin: LoginViewModel by activityViewModels()
    private lateinit var btnToCart:Button
    private lateinit var btnAddCart: Button
    private lateinit var idProducto: String
    private lateinit var btnStock:Button
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private var current_stock = MutableLiveData<Int>(0)
    private var selectedStock = 1
    private var nuevo_stock:Int  = 0
    private var stockDB = 0
    private var test  = 0
    private lateinit var adapterStock: ModalAdapter
    private lateinit var bottom: Dialog
    private var estaComprandoStock = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.product_description_fragment, container, false)
        btnToCartInit(v)
        btnAddCart(v)
        btnStock(v)

        return v
    }


    fun addToCart(v:View){
        val producto = viewModelPD.crearProducto(
        ProductDescriptionFragmentArgs.fromBundle(
                requireArguments()
            )
        )
        val carrito = (activity as MainActivity).itemsCarrito
        if(carrito.size() > 0){
             val productoCarrito = carrito.getProductItem(idProducto)
            if(productoCarrito != null){

                if( test - (productoCarrito.quantity + selectedStock) <= 0){

                    if(test - (productoCarrito.quantity + selectedStock) != 0){
                        makeSnackError("No puede agregar mas este producto por falta de stock")

                        if(stockDB.minus(productoCarrito.quantity) < 0){
                            stockDB = 0
                            selectedStock = 1
                            current_stock.value  = 0
                        }else{
                            stockDB = stockDB.minus(productoCarrito.quantity)
                            selectedStock = 1
                            current_stock.value =  stockDB
                        }


                    }else{
                        viewModelPD.agregarItemsCarrito(producto,selectedStock,carrito,0)
                        estaComprandoStock = true
                        stockDB = 0
                        selectedStock = 1
                        current_stock.value = 0
                    }



                }else{
                    var nuevoStock = viewModelPD.agregarItemsCarrito(producto,selectedStock,carrito,stockDB)
                    if(nuevoStock == 0){
                        estaComprandoStock = true
                        stockDB = stockDB.minus(nuevoStock)
                        if(stockDB < 0){
                            stockDB = 0
                        }
                    }
                    stockDB = nuevoStock
                    selectedStock = 1
                    current_stock.value = nuevoStock
                }
            }else{
                var nuevoStock = viewModelPD.agregarItemsCarrito(producto,selectedStock,carrito,stockDB)
                if(nuevoStock == 0){
                    estaComprandoStock = true
                }
                stockDB = nuevoStock
                selectedStock = 1
                current_stock.value = nuevoStock
            }
        }else{
            var nuevoStock = viewModelPD.agregarItemsCarrito(producto,selectedStock,carrito,stockDB)
            if(nuevoStock == 0){
                estaComprandoStock = true
            }
            stockDB = nuevoStock
            selectedStock = 1
            current_stock.value = nuevoStock

        }

    }


    fun observerStock(){
        viewModelPD.fetchStockFromDb(
            ProductDescriptionFragmentArgs.fromBundle(
                requireArguments()
            ).idProducto)
            .observe(viewLifecycleOwner, Observer {
                current_stock.value = it
                stockDB = it
                test = it
            })
    }
    fun observeCurrentStock(){
        current_stock.observe(this,Observer{
            val array = MutableList(test){index -> index + 1}
            val aux = array.filter { it < 8 }.toMutableList()
            adapterStock = ModalAdapter(aux){
                onClickStock(it)
            }
            adapterStock.notifyDataSetChanged()
            nuevo_stock = it
            if(nuevo_stock <= 0){
                nuevo_stock = 0
            }
            if(nuevo_stock > 0){
                habilitarBtnYModal()
            }else if(nuevo_stock == 0){
                if(estaComprandoStock){
                    bloquearBtnYModal()
                }else{
                    habilitarBtnYModal()
                }

            }
            if(nuevo_stock == test && stockDB == 0 &&  test == stockDB){
                bloquearBtnYModal()
            }
        })
    }

    fun observeIsAdmin() {
        viewModelLogin.esAdmin.observe(viewLifecycleOwner, Observer {
            btnAddCart.isInvisible = it
            btnToCart.isInvisible = it
        })
    }

    override fun onStart() {
        super.onStart()
        observerStock()
        observeCurrentStock()
        observeIsAdmin()
        initView()


    }
    private fun initView(){
        idProducto = ProductDescriptionFragmentArgs.fromBundle(
            requireArguments()
        ).idProducto
        var nombre_producto = v.findViewById<TextView>(R.id.item_title)
        var desc = v.findViewById<TextView>(R.id.item_desc)
        var price = v.findViewById<TextView>(R.id.item_price)
        var imgProductItem : ImageView = v.findViewById(R.id.item_image)
        Glide
            .with(requireContext())
            .load(
                ProductDescriptionFragmentArgs.fromBundle(
                    requireArguments()
                ).image)
            .centerInside()
            .into(imgProductItem)
        nombre_producto.text = ProductDescriptionFragmentArgs.fromBundle(
            requireArguments()
        ).title
        desc.text = ProductDescriptionFragmentArgs.fromBundle(
            requireArguments()
        ).description
        price.text = "$" + ProductDescriptionFragmentArgs.fromBundle(
            requireArguments()
        ).price
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
            val action =
                ProductDescriptionFragmentDirections.actionProductDescriptionFragmentToCarritoFragment()
            v.findNavController().navigate(action)
        }
    }

    fun btnStock(v:View){
        btnStock = v.findViewById(R.id.btnmodal)
        btnStock.setOnClickListener {
            showDialog(v)
        }
    }

    fun showDialog(v: View){
        bottom = Dialog(requireContext())
        bottom.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottom.setContentView(R.layout.bottomsheetlayout)
        val recyclerview = bottom.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview!!.layoutManager = LinearLayoutManager(v.context)
        val array = MutableList<Int>(stockDB){index -> index + 1}
        val aux = array.filter { it < 8 }.toMutableList()
        adapterStock = ModalAdapter(aux){
            onClickStock(it)
        }
        recyclerview.adapter = adapterStock
        bottom.show()
        bottom.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        bottom.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottom.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun onClickStock(it:Int){
        if(it < 7){
            selectedStock = it
            //current_stock.value = stockDB
            current_stock.value = stockDB.minus(selectedStock)
        }else{
            newModalStock()
        }
        bottom.dismiss()
    }

    fun newModalStock(){
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        customAlertDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.modal_mas_de_6_items, null, false)
        var stockModal = customAlertDialogView.findViewById<TextInputLayout>(R.id.stock_field)
        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Elegí cantidad")
            .setMessage("$stockDB disponibles")
            .setPositiveButton("Aplicar"){ dialog,_ ->
                try{
                    if(Integer.parseInt(stockModal.editText?.text.toString()) <= stockDB){
                        selectedStock = Integer.parseInt(stockModal.editText?.text.toString())

                        current_stock.value = stockDB.minus(selectedStock)
                    }else{
                        dialog.dismiss()
                        makeSnackError("No puede ingresar mas de la cantidad disponible")

                    }
                }catch(e:Exception){
                    Log.d("Error","Error valor ingresado")
                    makeSnackError("Stock invalido")
                }

            }
            .show()
    }

    fun bloquearBtnYModal(){
        btnAddCart.isEnabled = false
        btnAddCart.isClickable = false
        btnAddCart.text = "No hay stock"
        btnAddCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.greyStrong))
        btnStock.isEnabled = false
        btnStock.isClickable = false
        btnStock.text = "Cantidad: 0"


    }

    fun habilitarBtnYModal(){
        btnAddCart.isEnabled = true
        btnAddCart.isClickable = true
        btnAddCart.text = "Agregar al carrito"
        btnAddCart.setBackgroundColor(Color.parseColor("#2DDC53"))
        btnStock.isEnabled = true
        btnStock.isClickable = true
        btnStock.text = "Cantidad: ${selectedStock.toString()}  (${current_stock.value} disponibles)"

    }

    fun makeSnackError(error:String){
        Snackbar.make(
            v,
            "$error",
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(getColor(requireContext(), R.color.alert_danger))
            .show()
    }

}



