package com.ort.usanote.entities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

class Repo {
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var ret = mutableListOf<Product>()

    suspend fun getProductDataForList(queryS:String,categoria:String,field:String,order:String,stock:Int,desde:Int,hasta:Int):LiveData<MutableList<Product>>{
        val mutableDataRepo = MutableLiveData<MutableList<Product>>()
        var productsFromDB = mutableListOf<Product>()
        val ref = db.collection("productos")
        val query = ref
        try{
            val data =   query.get().await()
            for (dc in data){
                val prod = dc.toObject(Product::class.java)
                prod.idProducto = dc.id
                productsFromDB.add(prod)
            }

        }catch (e:Exception){
            Log.d("RepoDb","Error al traer la lista de productos")
        }

       if(queryS != "" && queryS.lowercase() != "null"){
           productsFromDB = porSearch(productsFromDB,queryS)
       }
       if(categoria != ""){
           //filtro por categoria
           productsFromDB = porCategoria(productsFromDB,categoria)
       }
       if(stock != -1){
           //filtro por stock
           productsFromDB = porStock(productsFromDB,stock)
       }
       if(desde != -1 && hasta != -1){
           // filtro completo
           productsFromDB = ambasCotas(productsFromDB,desde, hasta)
       }else if(desde != -1){
           //solo uso desde
           productsFromDB = desde(productsFromDB, desde)
       }else if(hasta != -1){
           productsFromDB = hasta(productsFromDB,hasta)
       }
       if(field != "" && order != ""){
           Ordenar(productsFromDB, field, order)
       }
       mutableDataRepo.value = productsFromDB
       return  mutableDataRepo
   }

    fun getProductStock(idProducto: String):LiveData<Int>{
        val mutableDataRepo = MutableLiveData<Int>()
        var productFromDB = Product()
        var stockFromDB = 0
        db.collection("productos").document(idProducto)
            .addSnapshotListener { value, error ->
                if(error!=null){
                     return@addSnapshotListener
                }
                if(value !=null){
                    productFromDB = value.toObject(Product::class.java)!!
                    stockFromDB = productFromDB.stock
                    mutableDataRepo.value = stockFromDB
                }

         }
        return mutableDataRepo
    }

    fun updateVisitas(idProducto:String){
        db.collection("productos").document(idProducto)
            .update("cantidadVisitas",FieldValue.increment(1))
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
    }
    fun decrementStock(idProducto: String,value:Double){
        db.collection("productos").document(idProducto)
            .update("stock",FieldValue.increment(-value))
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
    }
    fun updateStock(itemsCarrito: ProductItemRepository){
        for(pedido in itemsCarrito.getProductItems()){
            db.collection("productos").document(pedido.producto.idProducto)
                .update("stock",FieldValue.increment(-(pedido.quantity.toDouble())))
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
        }
    }



    private fun porCategoria(productsFromDB:MutableList<Product>,categoria: String):MutableList<Product>{

        if(categoria.lowercase() != "null" && categoria != "busqueda"){
            ret = productsFromDB.filter{
                it.categoria == categoria
            }.toMutableList()
        }
        return ret
    }

    private fun porSearch(productsFromDB:MutableList<Product>,query: String):MutableList<Product>{

        if(query.lowercase() != "null"){
            ret = productsFromDB.filter{
                query.lowercase() in it.nombre.lowercase()
            }.toMutableList()
        }
        return ret
    }

    private fun porStock(productsFromDB:MutableList<Product>,stock:Int):MutableList<Product>{

        if(stock!= -1){
            ret = productsFromDB.filter{
                it.stock > 0
            }.toMutableList()
        }
        return ret
    }

    private fun desde(productsFromDB:MutableList<Product>,desde: Int):MutableList<Product>{

        if(desde!= -1){
            ret = productsFromDB.filter{
                it.price > desde
            }.toMutableList()
        }
        return ret
    }

    private fun hasta(productsFromDB:MutableList<Product>,hasta: Int):MutableList<Product>{

        if(hasta!= -1){
            ret = productsFromDB.filter{
                it.price < hasta
            }.toMutableList()
        }
        return ret
    }

    private fun ambasCotas(productsFromDB: MutableList<Product>, desde: Int, hasta: Int):MutableList<Product>{

        ret = desde(productsFromDB,desde)
        ret = hasta(ret,hasta)
        return ret
    }

    private fun Ordenar(productsFromDB: MutableList<Product>, field:String, order: String){
        if(field.lowercase() == "nombre"){
            if(order == "Asc"){
                productsFromDB.sortBy { it.nombre }
            }else{
                productsFromDB.sortByDescending { it.nombre }
            }
        }else if(field.lowercase() == "price") {
            if(order == "Asc"){
                productsFromDB.sortBy { it.price }
            }else{
                productsFromDB.sortByDescending { it.price }
            }
        }else{
            productsFromDB.sortByDescending { it.created_at }
        }
    }
}