package com.ort.usanote.fragments.productos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.ort.usanote.R
import com.ort.usanote.adapters.CategoryAdapter
import com.ort.usanote.adapters.ProductAdapter
import com.ort.usanote.entities.*
import com.ort.usanote.viewModels.productos.ProductosViewModel
class ProductosFragment : Fragment() {

    companion object {
        fun newInstance() = ProductosFragment()
    }

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCategorias: RecyclerView
    private lateinit var myAdapter: ProductAdapter
    private var productList: MutableList<Product> = mutableListOf()
    private lateinit var swipeRefreshView: SwipeRefreshLayout
    private lateinit var btnFiltro: Button
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private val viewModelProductos: ProductosViewModel by viewModels()
    private var ignoreArgs = false
    private lateinit var shimmer:ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.productos_fragment, container, false)
        if(ProductosFragmentArgs.fromBundle(requireArguments()).searchQuery!!.lowercase() != "null"){
            viewModelProductos.setQuery(
                ProductosFragmentArgs.fromBundle(
                    requireArguments()
                ).searchQuery!!.replaceFirstChar {it.uppercase()})
        }
        if(ProductosFragmentArgs.fromBundle(requireArguments()).categoria != "null" && ProductosFragmentArgs.fromBundle(
                requireArguments()
            ).categoria != "busqueda" ){
            viewModelProductos.setCategoryBy(
                ProductosFragmentArgs.fromBundle(
                    requireArguments()
                ).categoria!!)
        }
        shimmer  = rootView.findViewById(R.id.shimmer_view_container)
        recyclerView(rootView, requireContext())
        swipeRefreshView(rootView, requireContext())
        btnFiltro(rootView)
        recyclerViewCategorias(rootView, requireContext())


        return rootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun btnFiltro(rootView: View) {
        val filtros = arrayOf("Ordenar por", "Filtrar por")
        btnFiltro = rootView.findViewById(R.id.btnFiltrar)
        btnFiltro.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Filtros")
                .setItems(filtros) { dialog, i ->
                    when (i) {
                        0 -> ordenar()
                        1 -> filtrar()
                    }
                }
                .show()
        }
    }

    private fun recyclerViewCategorias(rootView: View, context: Context) {
        recyclerViewCategorias = rootView.findViewById(R.id.categorias)
        recyclerViewCategorias.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewCategorias.adapter = CategoryAdapter(viewModelProductos.getListCategorias()) {
            onCategoryClick(it)
        }

    }

    private fun observerProducts() {
        shimmer.startShimmer()

        viewModelProductos.fetchProductDataForList().observe(viewLifecycleOwner, Observer {
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            productList = it
            myAdapter.setListData(it)
            myAdapter.notifyDataSetChanged()
        })

    }

    private fun swipeRefreshView(rootView: View, context: Context) {
        swipeRefreshView = rootView.findViewById(R.id.refres_layout)
        swipeRefreshView.setOnRefreshListener {
            productList.clear()
            ignoreArgs = true
            viewModelProductos.resetParameters()
            observerProducts()
            swipeRefreshView.isRefreshing = false
        }
    }

    private fun recyclerView(rootView: View, context: Context) {
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        if(ignoreArgs){
            viewModelProductos.resetParameters()
        }
        myAdapter = ProductAdapter(context) {
            onItemClick(it)
        }
        recyclerView.adapter = myAdapter

        observerProducts()
    }
    private fun onItemClick(pos: Int) {
        viewModelProductos.updateVisitas(productList[pos].idProducto)
        val action =
            ProductosFragmentDirections.actionProductosFragmentToProductDescriptionFragment(
                productList[pos].nombre,
                productList[pos].description,
                productList[pos].price,
                productList[pos].imageUrl,
                productList[pos].stock,
                productList[pos].idProducto,
                productList[pos].categoria,
                productList[pos].marca,
                productList[pos].cantidadVisitas
            )
        rootView.findNavController().navigate(action)

    }

    private fun onCategoryClick(pos: Int) {
        productList.clear()
        viewModelProductos.setCategoryBy(viewModelProductos.getListCategorias()[pos])
        observerProducts()
    }

    private fun filtrar() {
        val filter = arrayOf("Stock", "Rango de precios")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Filtros")
            .setItems(filter) { _, i ->
                when (i) {
                    0 -> filterStock()
                    1 -> filterByPriceRange()
                }
            }
            .show()
    }
    private fun ordenar() {
        val order = arrayOf(
            "Ordenar de A-Z",
            "Ordenar de Z-A",
            "Ordenar por precio mayor a menor",
            "Ordenar por precio menor a mayor",
            "Mas recientes"
        )
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ordenar")
            .setItems(order) { dialog, i ->
                when (i) {
                    0 -> orderBy("nombre", "Asc")
                    1 -> orderBy("nombre", "Des")
                    2 -> orderBy("price","Des")
                    3 -> orderBy("price","Asc")
                    4 -> orderBy("created_at","Des")
                }
            }
            .show()
    }
    private fun filterStock(){
        viewModelProductos.setStock(0)
        observerProducts()
    }
    private fun filterByPriceRange(){
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        customAlertDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.filter_modal_card, null, false)
        var precio_desde = customAlertDialogView.findViewById<TextInputLayout>(R.id.desde_text_field)
        var precio_hasta = customAlertDialogView.findViewById<TextInputLayout>(R.id.hasta_text_field)
        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setPositiveButton("Aplicar") { dialog, _ ->
                var desde_incompleto = precio_desde.editText?.text.isNullOrBlank()
                var hasta_incompleto = precio_hasta.editText?.text.isNullOrBlank()

                if(!desde_incompleto && !hasta_incompleto){
                    viewModelProductos.setDesde(Integer.parseInt(precio_desde.editText?.text.toString()))
                    viewModelProductos.setHasta(Integer.parseInt(precio_hasta.editText?.text.toString()))

                }else if(!desde_incompleto){
                    viewModelProductos.setDesde(Integer.parseInt(precio_desde.editText?.text.toString()))

                }else if(!hasta_incompleto){
                    viewModelProductos.setHasta(Integer.parseInt(precio_hasta.editText?.text.toString()))
                }
                observerProducts()
            }
            .show()

    }

    private fun orderBy(field:String,order:String){
        viewModelProductos.setField(field)
        viewModelProductos.setOrder(order)
        observerProducts()
    }
}
