package com.ort.usanote.entities

class Parameters(
    field:String,
    order:String,
    categoryBy:String,
    queryS: String,
    desde: Int,
    hasta: Int,
    stock: Int,
) {

    private var field:String
    private var order:String
    private var categoryBy:String
    private var queryS: String
    private var desde: Int = -1
    private var hasta: Int = -1
    private var stock: Int = -1


    constructor():this("","","","",-1,-1,-1)
    init{
        this.field = ""
        this.order = ""
        this.categoryBy = ""
        this.queryS = ""
        this.desde = -1
        this.hasta = -1
        this.stock = -1
    }
    fun setCategoryBy (it:String){
        categoryBy = it
    }
    fun setQuery (it:String){
        queryS=it
    }
    fun setField (it:String){
        field = it
    }
    fun setOrder (it:String){
        order = it
    }
    fun setStock (it:Int){
        stock = it
    }
    fun setDesde (it:Int){
        desde  = it
    }
    fun setHasta (it:Int){
        hasta = it
    }

    fun getCategoryBy ():String{
        return this.categoryBy
    }
    fun getQuery ():String{
        return this.queryS
    }
    fun getField ():String{
        return this.field
    }
    fun getOrder ():String{
        return this.order
    }
    fun getStock ():Int{
        return this.stock
    }
    fun getDesde ():Int{
        return this.desde
    }
    fun getHasta ():Int{
        return this.hasta
    }

    fun reset(){
        this.field = ""
        this.order = ""
        this.categoryBy = ""
        this.queryS = ""
        this.desde = -1
        this.hasta = -1
        this.stock = -1
    }
}