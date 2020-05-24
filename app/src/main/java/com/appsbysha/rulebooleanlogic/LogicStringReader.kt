package com.appsbysha.rulebooleanlogic

import java.lang.Integer.max

class LogicStringReader (val str: String) {
    var i = 0
    fun addProductGroup(): ProductGroup { //invalid: X(a+b) . valid X(a||b) ... if 3(a+2b) MUST Reformat to 3a+6b // must be surrounded by brackets (validator can add if missing). only lower case letters.  same value can appear twice only with OR between them
        var pg = ProductGroup()
        pg.productGroupList = mutableListOf()

        while (i < str.length) {
            var char = str.get(i).toString()
            when {
                char == "|" -> {
                    pg.amount = max(1, pg.amount)
                    i++
                    i++
                    pg.productGroupList?.add(addProductGroup())

                }
                char == "+" -> {
                    pg.amount += 1
                    i++
                    pg.productGroupList?.add(addProductGroup())

                }
                char == "(" || char == "[" || char == "{" -> {
                    i++
                    pg.productGroupList?.add(addProductGroup())

                }
                char.get(0) in ('a'..'z') -> {
                    pg.productId = char
                    i++
                    return pg

                }

                char.get(0) in ('0'..'9') -> {
                    var num: String = ""
                    while (char.get(0) in ('0'..'9')) {
                        num += char
                        i++
                        char = str.get(i).toString()
                    }
                    pg.amount = num.toInt()
                }
                char == "}" || char == "]" || char == ")" -> {
                    i++
                    return pg

                }
            }
        }
        return pg
    }
}