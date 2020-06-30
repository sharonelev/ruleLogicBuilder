package com.appsbysha.rulebooleanlogic

import android.util.Log
import org.json.JSONObject

class ProductGroup() : JSONObject() {
    var productId: String? = null
    var amount: Int = 1
    var allocated: Int = 0
    var productGroupList: MutableList<ProductGroup>? = null
    var operator: LogicOperator = LogicOperator.AND


    private fun clone(): ProductGroup {
        val newProductGroup = ProductGroup()
        newProductGroup.productId = this.productId
        newProductGroup.allocated = this.allocated
        newProductGroup.amount = this.amount
        newProductGroup.operator = this.operator
        this.productGroupList?.let {
            newProductGroup.productGroupList = mutableListOf()
            for (item in it)
                newProductGroup.productGroupList?.add(item.clone())
        }

        return newProductGroup

    }

    private fun cloneToThis(productGroup: ProductGroup) {
        this.productId = productGroup.productId
        this.allocated = productGroup.allocated
        this.amount = productGroup.amount
        this.operator = productGroup.operator
        productGroup.productGroupList?.let {
            this.productGroupList = mutableListOf()
            for (item in it)
                this.productGroupList?.add(item.clone())
        }
    }


    enum class LogicOperator() {
        AND(),
        OR()
    }

    fun searchItemInRule(searchProductId: String): Boolean { //searches for the rule once. ignores future items
        val pg: ProductGroup = this
        if (pg.productId.isNullOrBlank()) {
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

    private fun hasReachedAmount(productGroup: ProductGroup): Boolean {
        return (productGroup.allocated >= productGroup.amount)
    }


    //step 1: allocate items
    //step 2: calculate parents allocation running on all combos with recursion


    fun allocateRules(searchProductId: String) {
        allocateProductsToRule(searchProductId)
        var maxPg = this.clone()
        var maxInt = 0
        val allCombos = getAllCombos(this)
        for (list in allCombos) {
            val newPg = this.clone()
            newPg.productGroupList = mutableListOf()
            for (item in list)
                newPg.productGroupList?.add(item.clone())
            val res = maxAllocator(newPg, hashMapOf())
            if (res > maxInt) {
                maxInt = res
                maxPg = newPg.clone()
            }
        }

        this.cloneToThis(maxPg)

    }

    private fun allocateProductsToRule(searchProductId: String) {
        val pg: ProductGroup = this

        if (pg.productId.isNullOrBlank()) {
            pg.productGroupList?.let {
                for (pgChild in it) {
                    pgChild.allocateProductsToRule(searchProductId)
                }
                pg.allocated = 0
            }
        } else if (pg.productId == searchProductId) {
            pg.allocated += 1
            Log.i("item in rule", "allocated" + pg.allocated.toString())
        }
    }

    private fun maxAllocator(pg: ProductGroup, productList: HashMap<String, Int>): Int {
        if (pg.productId.isNullOrBlank()) {
            for (pgChild in pg.productGroupList!!) {
                maxAllocator(pgChild, productList)
            }
            findMaxAllocation(pg, productList)
        }
        return pg.allocated

    }


    fun groupOfAllocatedReachedAmount(): Int {
        return this.allocated / (this.amount)
    }

    private fun parentReachedAmount(
        productGroup: ProductGroup,
        productList: HashMap<String, Int>
    ): Int {
        productGroup.allocated = 0
        when (productGroup.operator) {
            LogicOperator.AND -> {
                var isAllocating = true
                var i = 0
                val tempList: HashMap<String, Int> = hashMapOf()
                val subTempList: HashMap<String, Int> = hashMapOf()
                while (isAllocating) {
                    var allocator = 0
                    for (pgChild in productGroup.productGroupList!!) {
                        var temp = pgChild.allocated - pgChild.amount * i
                        pgChild.productId?.let {
                            temp -= (productList[it] ?: 0)
                        }
                        if (temp >= pgChild.amount) {
                            allocator += 1
                            pgChild.productId?.let {
                                tempList[it] = pgChild.amount
                            }
                        } else {
                            break // if one child doesn't meet condition, AND won't meet anymore
                        }
                    }
                    if (allocator == productGroup.amount) {
                        i++
                        productGroup.allocated = allocator * i
                        for (tempValue in tempList) {
                            subTempList[tempValue.key] =
                                (subTempList[tempValue.key] ?: 0) + tempValue.value
                        }
                    } else {
                        isAllocating = false
                        for (tempValue in subTempList) {
                            productList[tempValue.key] =
                                (productList[tempValue.key] ?: 0) + tempValue.value
                        }
                    }
                }
            }

            LogicOperator.OR -> {
                var allocator = 0
                for (pgChild in productGroup.productGroupList!!) {
                    var temp = pgChild.allocated
                    pgChild.productId?.let {
                        temp -= (productList[it] ?: 0)
                    }
                    while (temp >= pgChild.amount) {
                        allocator += 1
                        temp -= pgChild.amount
                        pgChild.productId?.let {
                            productList[it] = (productList[it] ?: 0) + pgChild.amount
                        }
                        //product was allocated and cannot be used in a different section of the rule ie (a+2b)||(2a+b)
                    }
                }
                productGroup.allocated = allocator
            }


        }
        return productGroup.allocated
    }


    fun permuteOriginal(a: CharArray, k: Int) {
        if (k == a.size) {
            for (i in 0 until a.size) {
                print(" [" + a[i] + "] ")
            }
            println()
        } else {
            for (i in k until a.size) {
                var temp = a[k]
                a[k] = a[i]
                a[i] = temp
                permuteOriginal(a, k + 1)
                temp = a[k]
                a[k] = a[i]
                a[i] = temp
            }
        }
    }


    private fun findMaxAllocation(
        pg: ProductGroup
        , productList: HashMap<String, Int> //receives a product list and returns
    ) {// : HashMap<String, Int>{  // product list could be filled from previous product groups
        val newPg = ProductGroup()

        var maxAllocated: Int
        var prevMaxAllocated = 0
        var maxProductList: HashMap<String, Int> = hashMapOf()// productList
        var maxPg = ProductGroup()
        val tempProductList: HashMap<String, Int> = HashMap(productList) //save the original


        for (list in getAllCombos(pg)) {
            newPg.amount = pg.amount
            newPg.productGroupList = list
            newPg.operator = pg.operator
            maxAllocated = parentReachedAmount(newPg, tempProductList)
            if (prevMaxAllocated < maxAllocated) {
                prevMaxAllocated = maxAllocated
                maxProductList = HashMap(tempProductList)
                maxPg = newPg.clone()
            }
        } //done iterating over list
        pg.allocated = maxPg.allocated
        for (item in maxProductList.keys)
            productList[item] = maxProductList[item] ?: 0

        // return maxProductList
    }


    private fun getAllCombos(pg: ProductGroup): MutableList<MutableList<ProductGroup>> {
        val comboList: MutableList<MutableList<ProductGroup>> = mutableListOf()
        pg.productGroupList?.let {
            permute(comboList, it, 0)
        }
        return comboList
    }

    private fun permute(
        comboList: MutableList<MutableList<ProductGroup>>,
        productGroupList: MutableList<ProductGroup>,
        k: Int
    ) {
        if (k == productGroupList.size) {
            val pgl: MutableList<ProductGroup> = mutableListOf()
            for (item in productGroupList)
                pgl.add(item.clone())
            comboList.add(pgl)

        } else {
            for (i in k until productGroupList.size) {
                var temp = productGroupList[k].clone()
                productGroupList[k] = productGroupList[i].clone()
                productGroupList[i] = temp.clone()
                permute(comboList, productGroupList, k + 1)
                temp = productGroupList[k].clone()
                productGroupList[k] = productGroupList[i].clone()
                productGroupList[i] = temp.clone()
            }
        }
    }
}
