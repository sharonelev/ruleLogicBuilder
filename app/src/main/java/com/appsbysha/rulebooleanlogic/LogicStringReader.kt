package com.appsbysha.rulebooleanlogic

import android.content.Context
import android.widget.Toast
import java.lang.Integer.max

class LogicStringReader (private val str: String, private val context: Context) {
    private var i = 0
    fun addProductGroup(): ProductGroup { //invalid: X(a+b) . valid X(a||b) ... if 3(a+2b) MUST Reformat to 3a+6b // must be surrounded by brackets (validator can add if missing). only lower case letters.  same value can appear twice only with OR between them
        val pg = ProductGroup()
        pg.productGroupList = mutableListOf()

        while (i < str.length) {
            var char = str[i].toString()
            when {
                char == "|" -> {
                    pg.amount = max(1, pg.amount)
                    pg.operator = ProductGroup.LogicOperator.OR
                    i++
                    i++
                    pg.productGroupList?.add(addProductGroup())

                }
                char == "+" -> {
                    pg.amount += 1
                    pg.operator = ProductGroup.LogicOperator.AND
                    i++
                    pg.productGroupList?.add(addProductGroup())

                }
                char == "(" || char == "[" || char == "{" -> {
                    i++
                    pg.productGroupList?.add(addProductGroup())

                }
                char[0] in ('a'..'z') -> {
                    pg.productId = char
                    i++
                    return pg

                }

                char[0] in ('0'..'9') -> {
                    var num = ""
                    while (char[0] in ('0'..'9')) {
                        num += char
                        i++
                        char = str[i].toString()
                    }
                    pg.amount = num.toInt()
                }
                char == "}" || char == "]" || char == ")" -> {
                    i++
                    return pg

                }
                else->{Toast.makeText(context, "invalid input", Toast.LENGTH_SHORT).show()}
            }
        }
        return pg
    }
}