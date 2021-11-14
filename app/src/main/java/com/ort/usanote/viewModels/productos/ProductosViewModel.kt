package com.ort.usanote.viewModels.productos


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ort.usanote.entities.Parameters
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.Repo
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    private lateinit var listCategorias: Array<String>
    val repo = Repo()
    val parameters = Parameters()

    private fun categoryInit(){
        listCategorias = arrayOf("Periferico","Refrigeracion","Procesador","GPU","RAM","Monitor","Gabinete","Fuente")
    }
    fun getListCategorias():Array<String>{
        categoryInit()
        return this.listCategorias
    }

    fun fetchProductDataForList():MutableLiveData<MutableList<Product>>{
        val mutableData = MutableLiveData<MutableList<Product>>()
        viewModelScope.launch {
            mutableData.value = repo.getProductDataForList(
                parameters.getQuery(),
                parameters.getCategoryBy(),
                parameters.getField(),
                parameters.getOrder(),
                parameters.getStock(),
                parameters.getDesde(),
                parameters.getHasta()
            ).value
        }
        return mutableData
    }

    fun updateVisitas(idProducto:String){
        repo.updateVisitas(idProducto)
    }


    fun setCategoryBy (it:String){
        parameters.setCategoryBy(it)
    }
    fun setQuery (it:String){
        parameters.setQuery(it)
    }
    fun setField (it:String){
        parameters.setField(it)
    }
    fun setOrder (it:String){
        parameters.setOrder(it)
    }
    fun setStock (it:Int){
        parameters.setStock(it)
    }
    fun setDesde (it:Int){
        parameters.setDesde(it)
    }
    fun setHasta (it:Int){
        parameters.setHasta(it)
    }
    fun resetParameters(){
         parameters.reset()
    }


}




