package com.ort.usanote.entities

import android.os.Parcel
import android.os.Parcelable

@Parcelize
class ProductItemRepository() : Parcelable {
    private var productItemList : MutableList<ProductItem> = mutableListOf()

    constructor(parcel: Parcel) : this() {

    }

    fun getThis():ProductItemRepository{
        return this
    }

    fun dropList(){
        productItemList.clear()
        print(productItemList)
    }

    fun addProductItem(product:Product,cant:Int){
        productItemList.add(ProductItem(product,cant))
    }

    fun size():Int{
        return productItemList.size
    }

    fun getProductItems () : MutableList<ProductItem> {
        return productItemList
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
