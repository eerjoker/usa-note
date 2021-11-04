package com.ort.usanote.entities

class Product (
    idProducto:String,
    nombre : String,
    description: String,
    price : Double,
    stock: Int,
    categoria:String,
    marca:String,
    imageUrl : String
) {
    lateinit var idProducto:String
    lateinit var nombre : String
    lateinit var description: String
    var price : Double = 0.0
    var stock: Int = 0
    lateinit var categoria:String
    lateinit var marca:String
    lateinit var imageUrl : String
    constructor():this("","","",0.0,0,"","","")
    init {
        this.idProducto = idProducto!!
        this.nombre = nombre!!
        this.description = description!!
        this.price = price!!
        this.stock = stock!!
        this.categoria = categoria!!
        this.marca = marca!!
        this.imageUrl = imageUrl!!
    }
}