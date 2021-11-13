package com.ort.usanote.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.Repo
import com.ort.usanote.fragments.ProductDescriptionFragmentArgs

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



}