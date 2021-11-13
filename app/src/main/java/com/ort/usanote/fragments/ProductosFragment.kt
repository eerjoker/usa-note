package com.ort.usanote.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.CategoryAdapter
import com.ort.usanote.adapters.ProductAdapter
import com.ort.usanote.entities.*
import com.ort.usanote.viewModels.ProductosViewModel

class ProductosFragment : Fragment() {

    companion object {
        fun newInstance() = ProductosFragment()
    }

    private lateinit var viewModel: ProductosViewModel
    private lateinit var rootView : View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCategorias: RecyclerView
    private lateinit var itemsCarrito : ProductItemRepository
    private lateinit var myAdapter : ProductAdapter
    private lateinit var db : FirebaseFirestore
    private  var productList : MutableList<Product> = mutableListOf()
    private lateinit var swipeRefreshView : SwipeRefreshLayout
    private lateinit var btnFiltro: Button
    private lateinit var producto: Product
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private lateinit var listCategorias: Array<String>
    private var categoryFilterOn = false
    private lateinit var categoryBy :String
    private lateinit var queryS: String

    private fun categoryInit(){
        listCategorias = arrayOf("Periferico","Refrigeracion","Procesador","GPU","RAM","Monitor","Gabinete","Fuente")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.productos_fragment, container, false)
        db = FirebaseFirestore.getInstance()
        categoryInit()
        if(ProductosFragmentArgs.fromBundle(requireArguments()).categoria != null){
            categoryBy = ProductosFragmentArgs.fromBundle(requireArguments()).categoria!!
            if(categoryBy != "null" && categoryBy != "busqueda"){
                categoryFilterOn = true
            }
        }else{
            categoryFilterOn = false
        }
        queryS = ProductosFragmentArgs.fromBundle(requireArguments()).searchQuery.toString().replaceFirstChar { it.uppercase() }
        recyclerView(rootView,requireContext())
        swipeRefreshView(rootView,requireContext())
        btnFiltro(rootView)
        recyclerViewCategorias(rootView,requireContext())
        return  rootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductosViewModel::class.java)
    }

    private fun btnFiltro(rootView: View){
        val filtros = arrayOf("Ordenar por" , "Filtrar por")
        btnFiltro = rootView.findViewById(R.id.btnFiltrar)
        btnFiltro.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Filtros")
                .setItems(filtros){ dialog, i ->
                    when(i){
                        0 -> ordenar()
                        1 -> filtrar()

                    }

                }

                .show()
//
        }

    }


    private fun  EventChangeListener() {
        if(queryS == "null" || queryS == "Null"){
            if(!categoryFilterOn){
                db.collection("productos")
                    .addSnapshotListener(object: EventListener<QuerySnapshot>{
                        override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                            if (error != null){
                                Log.d("Firebase Error",error.message.toString())
                            }
                            for(dc:DocumentChange in value?.documentChanges!!){
                                if(dc.type == DocumentChange.Type.ADDED){
                                    producto = dc.document.toObject(Product::class.java)
                                    producto.idProducto = dc.document.id
                                    productList.add(producto)
                                }
                            }
                            myAdapter.notifyDataSetChanged()
                        }
                    })
            }else{
                db.collection("productos").whereEqualTo("categoria",categoryBy)
                    .addSnapshotListener(object: EventListener<QuerySnapshot>{
                        override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                            if (error != null){
                                Log.d("Firebase Error",error.message.toString())
                            }
                            for(dc:DocumentChange in value?.documentChanges!!){
                                if(dc.type == DocumentChange.Type.ADDED){
                                    producto = dc.document.toObject(Product::class.java)
                                    producto.idProducto = dc.document.id
                                    productList.add(producto)
                                }
                            }
                            myAdapter.notifyDataSetChanged()
                        }
                    })
            }
        }else{
            db.collection("productos").whereGreaterThanOrEqualTo("nombre",queryS).whereLessThan("nombre",queryS+'z')
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                })
        }

    }
    private fun recyclerViewCategorias(rootView: View,context: Context){
        recyclerViewCategorias = rootView.findViewById<RecyclerView>(R.id.categorias)
        //recyclerViewCategorias.layoutManager = LinearLayoutManager(rootView.context)
        val HorizontalLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerViewCategorias.layoutManager = HorizontalLayout
        recyclerViewCategorias.adapter = CategoryAdapter(listCategorias){
            onCategoryClick(it)
        }
    }
    private fun swipeRefreshView(rootView: View, context: Context){
        swipeRefreshView = rootView.findViewById(R.id.refres_layout)
        swipeRefreshView.setOnRefreshListener {
            productList.clear()
            categoryFilterOn = false
            queryS = "null"
            EventChangeListener()
            swipeRefreshView.isRefreshing = false
        }
    }
    private fun recyclerView(rootView: View, context: Context){
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        myAdapter = ProductAdapter(productList,context){
            onItemClick(it)
        }

        recyclerView.adapter = myAdapter
        EventChangeListener()


    }


    private fun onItemClick(pos:Int){
        val idProducto = productList[pos].idProducto
        var title = productList[pos].nombre
        var description = productList[pos].description
        var price = productList[pos].price
        var image = productList[pos].imageUrl
        var cant = productList[pos].stock
        var categoria = productList[pos].categoria
        var marca = productList[pos].marca
        val action = ProductosFragmentDirections.actionProductosFragmentToProductDescriptionFragment(title,description,price,image,cant,idProducto, categoria, marca,1)
        rootView.findNavController().navigate(action)

    }
    private fun onCategoryClick(pos:Int){
        productList.clear()
        categoryFilterOn = true
        categoryBy = listCategorias[pos]
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereEqualTo("categoria",listCategorias[pos])
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereEqualTo("categoria",listCategorias[pos]).whereGreaterThanOrEqualTo("nombre",queryS).whereLessThan("nombre",queryS+'z')
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filtrar() {
        val filter = arrayOf("Stock","Rango de precios")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Filtros")
            .setItems(filter){ _, i ->
                when(i){
                    0->filterStock()
                    1-> filterByPriceRange()
                }
            }
            .show()
    }
    private fun ordenar(){
        val order = arrayOf("Ordenar de A-Z:" , "Ordenar de Z-A","Ordenar por precio mayor a menor","Ordenar por precio menor a mayor","Mas recientes")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ordenar")
            .setItems(order) { dialog, i ->
                when (i) {
                    0 -> orderByNameASCENDING("nombre",Query.Direction.ASCENDING)
                    1 -> orderByNameDECENDING("nombre",Query.Direction.DESCENDING)
                    2 -> orderByPriceDECENDING("price",Query.Direction.DESCENDING)
                    3 -> orderByPriceASCENDING("price",Query.Direction.ASCENDING)
                    4 -> orderByFechaDECENDING("created_at",Query.Direction.DESCENDING)

                }

            }
            .show()

    }
    //Funciones para ordenar y filtrar la lista
    private fun orderByNameASCENDING(field: String,query: Query.Direction){
        productList.clear()
        if(categoryFilterOn){
            orderByProductsWithCategory(field,query)
        }else{
            productList.clear()
            orderByProducts(field,query)
        }
    }
    private fun orderByNameDECENDING(field: String,query: Query.Direction){
        productList.clear()
        if(categoryFilterOn){
            orderByProductsWithCategory(field,query)
        }else{
            productList.clear()
            orderByProducts(field,query)
        }

    }
    private fun orderByPriceASCENDING(field: String,query: Query.Direction){
        productList.clear()
        if(categoryFilterOn){
            orderByProductsWithCategory(field,query)
        }else{
            productList.clear()
            orderByProducts(field,query)
        }

    }
    private fun orderByPriceDECENDING(field: String,query: Query.Direction){
        productList.clear()
        if(categoryFilterOn){
            orderByProductsWithCategory(field,query)
        }else{
            productList.clear()
            orderByProducts(field,query)
        }
    }
    private fun orderByFechaDECENDING(field: String,query: Query.Direction){
        productList.clear()
        if(categoryFilterOn){
            orderByProductsWithCategory(field,query)
        }else{
            orderByProducts(field,query)
        }
    }

    private fun filterStock(){
        productList.clear()
        if(categoryFilterOn){
            filterByStockWithCategory("stock",0)
        }else{
            filterByStock("stock",0)
        }

    }
    private fun filterByPriceRange() {
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        customAlertDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.filter_modal_card, null, false)

        var precio_desde = customAlertDialogView.findViewById<TextInputLayout>(R.id.desde_text_field)
        var precio_hasta = customAlertDialogView.findViewById<TextInputLayout>(R.id.hasta_text_field)
        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setPositiveButton("Apply") { dialog, _ ->
                var desde_incompleto = precio_desde.editText?.text.isNullOrBlank()
                var hasta_incompleto = precio_hasta.editText?.text.isNullOrBlank()
                var desde: Int
                var hasta : Int
                if(!desde_incompleto && !hasta_incompleto){
                    desde = Integer.parseInt(precio_desde.editText?.text.toString())
                    hasta = Integer.parseInt(precio_hasta.editText?.text.toString())
                    if(categoryFilterOn){
                        fullfilterWithCategory(desde,hasta)
                    }else{
                        fullfilter(desde,hasta)
                    }

                }else if(!desde_incompleto){
                    desde = Integer.parseInt(precio_desde.editText?.text.toString())
                    filterDesde(desde)
                }else if(!hasta_incompleto){
                    hasta = Integer.parseInt(precio_hasta.editText?.text.toString())
                    filterHasta(hasta)
                }

            }
            .show()
    }

    private fun filterDesde(desde:Int){
        productList.clear()
        if(categoryFilterOn){
            filterByProductsWhereGreaterThanWithCategory(desde,"price")
        }else{
            filterByProductsWhereGreaterThan(desde,"price")
        }

    }
    private fun filterHasta(hasta:Int){
        productList.clear()
        if(categoryFilterOn){
            filterByProductWhereLesserThanWithCategory(hasta,"price")
        }else{
            filterByProductWhereLesserThan(hasta,"price")
        }

    }


    //Querys a FireStore sin categoria
    private fun fullfilter(desde: Int,hasta: Int){
        if(queryS == "null" || queryS == "Null"){
            productList.clear()
            db.collection("productos").whereGreaterThan("price",desde).whereLessThan("price",hasta)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            productList.clear()
            db.collection("productos").whereGreaterThan("price",desde).whereLessThan("price",hasta)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun orderByProducts(field:String,query: Query.Direction){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").orderBy(field,query)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").orderBy(field,query)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filterByProductsWhereGreaterThan(value:Int,field: String){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereGreaterThan(field,value)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereGreaterThan(field,value)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filterByProductWhereLesserThan(value:Int,field: String){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereLessThan(field,value)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereLessThan(field,value)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filterByStock(field: String,value: Int){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereGreaterThan(field,value)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereGreaterThan(field,value)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }

    // Querys a Firestore con categoria
    private fun orderByProductsWithCategory(field:String,query: Query.Direction){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereEqualTo("categoria",categoryBy).orderBy(field,query)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereEqualTo("categoria",categoryBy).orderBy(field,query)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filterByProductsWhereGreaterThanWithCategory(value:Int,field: String){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereGreaterThan(field,value).whereEqualTo("categoria",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereGreaterThan(field,value).whereEqualTo("categoria",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filterByProductWhereLesserThanWithCategory(value:Int,field: String){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereLessThan(field,value).whereEqualTo("category",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos").whereLessThan(field,value).whereEqualTo("category",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun fullfilterWithCategory(desde: Int,hasta: Int){
        if(queryS == "null" || queryS == "Null"){
            productList.clear()
            db.collection("productos").whereGreaterThan("price",desde).whereLessThan("price",hasta).whereEqualTo("category",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            productList.clear()
            db.collection("productos").whereGreaterThan("price",desde).whereLessThan("price",hasta).whereEqualTo("category",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }

                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
    private fun filterByStockWithCategory(field: String,value: Int){
        if(queryS == "null" || queryS == "Null"){
            db.collection("productos").whereGreaterThan(field,value).whereEqualTo("categoria",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                productList.add(producto)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }else{
            db.collection("productos")
                .whereGreaterThan(field,value).whereEqualTo("categoria",categoryBy)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null){
                            Log.d("Firebase Error",error.message.toString())
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                producto = dc.document.toObject(Product::class.java)
                                producto.idProducto = dc.document.id
                                if(queryS in producto.nombre){
                                    productList.add(producto)
                                }

                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }

    }
}


