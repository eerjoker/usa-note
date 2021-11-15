package com.ort.usanote.viewModels.productos

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.ProductItemRepository
import com.ort.usanote.entities.Repo
import com.ort.usanote.fragments.productos.ProductDescriptionFragmentArgs


class ProductDescriptionViewModel : ViewModel() {
    private var repo = Repo()
    fun crearProducto(fromBundle: ProductDescriptionFragmentArgs): Product {
        return Product(
            fromBundle.idProducto,
            fromBundle.title,
            fromBundle.description,
            fromBundle.price,
            fromBundle.cantidad,
            fromBundle.categoria,
            fromBundle.marca,
            fromBundle.image,
            fromBundle.cantidadVisitas)
    }

    fun fetchStockFromDb(idProducto:String):MutableLiveData<Int>{
        var mutableLiveData = MutableLiveData<Int>()
        repo.getProductStock(idProducto).observeForever{
            mutableLiveData.value = it
        }
        return  mutableLiveData
    }

    fun decrementStock(idProducto: String,amount:Double){
        repo.decrementStock(idProducto,amount)
    }

    fun agregarItemsCarrito(producto:Product,selectedStock:Int,carrito:ProductItemRepository,totalStock:Int):Int{
        carrito.addProductItem(producto, selectedStock)
        var ret = totalStock.minus(selectedStock)
        if( ret < 0 ){
            ret = 0
        }
        return ret
    }
}