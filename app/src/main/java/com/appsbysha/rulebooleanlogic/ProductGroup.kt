package com.appsbysha.rulebooleanlogic


import org.json.JSONObject
import kotlin.math.max

class ProductGroup() : JSONObject() {
    var productId: String? = null
    var subProductIdAllocated: HashMap<Int, Boolean> = hashMapOf()
    var amount: Int = 1
    var allocated: Int = 0
    var productGroupList: MutableList<ProductGroup>? = null
    var operator: LogicOperator = LogicOperator.AND


    enum class LogicOperator() {
        AND(),
        OR()
    }


    var maxAllocated = 0


    fun allocateWithItemPermutation(items: List<Char>, position: Int): Int {

        while (allocateProductsToConditions(items[position].toString(), position)) { //allocate item to the next condition it hasn't been allocated to. position is used as the specific item id (a0, a1 etc)
            if (position == items.size - 1) //if all allocated
            {
                allocateParents() //test all parents (brackets) and allocate if true
                maxAllocated = max(this.allocated / this.amount, maxAllocated) //save max rewards eligible
            } else
                allocateWithItemPermutation(items, position + 1) //same for next item

            removeAllocations(items[position].toString(), position, false) //reduces the allocation counter, but saves list of items that were previously allocated there
        }
        var i = position
        while (i < items.size) {
            removeAllocations(items[position].toString(), position, true) //removes list of items that were previously allocated there
            i++

        }
        return maxAllocated

    }


    private fun removeAllocations(
        item: String,
        productGuid: Int,
        initAllocatedList: Boolean
    ): Boolean {

        val pg: ProductGroup = this
        pg.productId?.let {
            if (item == it) {

                if (subProductIdAllocated[productGuid] == true && !initAllocatedList) {
                    allocated -= 1
                    subProductIdAllocated[productGuid] = false
                    return true
                }
                if (initAllocatedList) {
                    subProductIdAllocated.remove(productGuid)
                }

            }
        } ?: run {
            for (pgChild in pg.productGroupList!!) {
                if (pgChild.removeAllocations(item, productGuid, initAllocatedList))
                    return true
            }
        }
        return false
    }


    private fun allocateParents() {
        val pg: ProductGroup = this
        if (pg.productId.isNullOrBlank()) {
            for (pgChild in pg.productGroupList!!) {
                pgChild.allocateParents()
            }
            parentReachedAmountSimple(pg)
        }
    }


    private fun parentReachedAmountSimple(
        productGroup: ProductGroup
    ) {

        productGroup.allocated = 0
        when (productGroup.operator) {
            LogicOperator.AND -> {
                var isAllocating = true
                var i = 0

                while (isAllocating) {
                    var allocator = 0
                    for (pgChild in productGroup.productGroupList!!) {
                        val temp = pgChild.allocated - pgChild.amount * i
                        pgChild.productId?.let {
                        }
                        if (temp >= pgChild.amount) {
                            allocator += 1

                        } else {
                            break // if one child doesn't meet condition, AND won't meet anymore
                        }
                    }
                    if (allocator == productGroup.amount) {
                        i++
                        productGroup.allocated = allocator * i

                    } else {
                        isAllocating = false

                    }
                }
            }

            LogicOperator.OR -> {
                var allocator = 0
                for (pgChild in productGroup.productGroupList!!) {
                    var temp = pgChild.allocated

                    while (temp >= pgChild.amount) {
                        allocator += 1
                        temp -= pgChild.amount
                        //product was allocated and cannot be used in a different section of the rule ie (a+2b)||(2a+b)
                    }
                }
                productGroup.allocated = allocator
            }
        }
    }


    private fun allocateProductsToConditions(searchProductId: String, productGuid: Int): Boolean {
        val pg: ProductGroup = this

        if (pg.productId.isNullOrBlank()) {
            pg.productGroupList?.let {
                for (pgChild in it) {
                    if (pgChild.allocateProductsToConditions(searchProductId, productGuid))
                        return true
                    //if false go to next child
                }
                pg.allocated = 0
            }
        } else if (pg.productId == searchProductId) {
            if (pg.subProductIdAllocated[productGuid] != null) //item already allocated here whether true or false
                return false

            pg.allocated += 1
            pg.subProductIdAllocated[productGuid] = true
            return true

        }
        return false
    }


    private fun permuteOriginal(a: CharArray, k: Int) {
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


}
