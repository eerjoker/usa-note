package com.ort.usanote.entities

class ProductItem (
    product : Product,
    quantity : Int
) {
    var product: Product
    var  quantity: Int = 0
    constructor():this(Product(),0)
    init{
        this.product = product
        this.quantity = quantity
    }
    fun calculateSubtotal() : Double {
        return this.product.price * this.quantity.toDouble()
    }
}