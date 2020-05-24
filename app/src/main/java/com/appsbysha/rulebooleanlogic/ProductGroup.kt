package com.appsbysha.rulebooleanlogic

import android.util.Log
import org.json.JSONObject

class ProductGroup : JSONObject {
    var productId: String? = "null"
    var amount: Int = 1
    var allocated: Int = 0
    var productGroupList: MutableList<ProductGroup>? = null
    constructor(){}

    constructor(json: String) : super(json) {

        productId = this.optString("productId")
        amount = this.optInt("amount")
        allocated = this.optInt("allocated")
        productGroupList = this.optJSONArray("productGroupList")
            ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
            ?.map { com.appsbysha.rulebooleanlogic.ProductGroup(it.toString()) } as MutableList<ProductGroup>? // transforms each JSONObject of the array into Foo
    }

    fun searchItemInRule(searchProductId: String): Boolean {
        var pg: ProductGroup = this
        if (pg.productId == "null") {
            for (pgChild in pg.productGroupList!!) {
                val res = pgChild.searchItemInRule(searchProductId)
                if (res) {
                    pg.allocated += 1
                    return hasReachedAmount(pg)
                }

            }
        } else if (pg.productId == searchProductId) {
            if (pg.allocated > pg.amount) return false
            pg.allocated += 1
            Log.i("item in rule", "allocated" + pg.allocated.toString())
            return hasReachedAmount(pg)
        }
        return false
    }

    fun hasReachedAmount(productGroup: ProductGroup): Boolean {
        return (productGroup.allocated >= productGroup.amount)
    }

}
