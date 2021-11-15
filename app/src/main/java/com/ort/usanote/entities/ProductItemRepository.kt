package com.ort.usanote.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.FirebaseFirestore

@Parcelize
class ProductItemRepository() : Parcelable {
    private var detalleOrdenList : MutableList<DetalleOrden> = mutableListOf()

    constructor(parcel: Parcel) : this() {

    }

    fun getThis():ProductItemRepository{
        return this
    }

    fun dropList(){
        detalleOrdenList.clear()
        print(detalleOrdenList)
    }
    fun getProductItem(id:String):DetalleOrden?{
        return detalleOrdenList.find { productItem -> productItem.producto.idProducto == id }
    }


    fun addProductItem(producto:Product, cant:Int){
        var i = detalleOrdenList.size - 1
        var found = false

        while(!found && i >= 0) {
            var currentProductItem = detalleOrdenList[i]
            found = currentProductItem.producto.idProducto == producto.idProducto
            if (found) {
                currentProductItem.quantity += cant
                currentProductItem.producto.stock -= cant
            }
            i--
        }
        if (!found) {
            detalleOrdenList.add(DetalleOrden("", producto, cant))
        }
    }

    fun size():Int{
        return detalleOrdenList.size
    }

    fun getProductItems () : MutableList<DetalleOrden> {
        return detalleOrdenList
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItemRepository> {
        override fun createFromParcel(parcel: Parcel): ProductItemRepository {
            return ProductItemRepository(parcel)
        }

        override fun newArray(size: Int): Array<ProductItemRepository?> {
            return arrayOfNulls(size)
        }
    }
}

annotation class Parcelize
