package com.appsbysha.rulebooleanlogic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var productGroup: ProductGroup = ProductGroup()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        doneButton.setOnClickListener(View.OnClickListener { productGroup = LogicStringReader(editString.text.toString()).addProductGroup()
            editString.isEnabled = false
            Log.i("viewPG", "" )})

        clearButton.setOnClickListener(View.OnClickListener { editString.setText("")
            editString.isEnabled = true
            resultTV.setText("")
            enteredProducts.setText("")

        })

        addButton.setOnClickListener(View.OnClickListener {
            enteredProducts.text = ("${enteredProducts.text}${enterProductsEditText.text.toString()}")
           resultTV.text = productGroup.searchItemInRule(enterProductsEditText.text.toString()).toString()
        enterProductsEditText.setText("")
        })

/*
//A+(3B||2C)
        val pgs =
            ProductGroup("""{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":"A","amount":1,"allocated":0,"productGroupList":null},{"productId":null,"amount":1,"allocated":0,"productGroupList":[{"productId":"B","amount":3,"allocated":0,"productGroupList":null},{"productId":"C","amount":2,"allocated":0,"productGroupList":null}]}]}""")

        pgs.searchItemInRule("B")
        Log.i("reached?", pgs.hasReachedAmount(pgs).toString() )
        pgs.searchItemInRule("B")
        Log.i("reached?", pgs.hasReachedAmount(pgs).toString() )
        pgs.searchItemInRule("A")
        Log.i("reached?", pgs.hasReachedAmount(pgs).toString() )
        pgs.searchItemInRule("C")
        Log.i("reached?", pgs.hasReachedAmount(pgs).toString() )
        pgs.searchItemInRule("C")
        Log.i("reached?", pgs.hasReachedAmount(pgs).toString() )

        //((A+B)||(C+D))+ ((E+F)||(G+H))
        val pgs2 = ProductGroup("""{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":null,"amount":1,"allocated":0,"productGroupList":[{"productId":null,"amount":2,"allocated":2,"productGroupList":[{"productId":"A","amount":1,"allocated":0,"productGroupList":null},{"productId":"B","amount":1,"allocated":0,"productGroupList":null}]},{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":"C","amount":1,"allocated":0,"productGroupList":null},{"productId":"D","amount":1,"allocated":0,"productGroupList":null}]}]},{"productId":null,"amount":1,"allocated":0,"productGroupList":[{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":"E","amount":1,"allocated":0,"productGroupList":null},{"productId":"F","amount":1,"allocated":0,"productGroupList":null}]},{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":"G","amount":1,"allocated":0,"productGroupList":null},{"productId":"H","amount":1,"allocated":0,"productGroupList":null}]}]}]}""")

        pgs2.searchItemInRule("A")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )
        pgs2.searchItemInRule("B")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )
        pgs2.searchItemInRule("C")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )
        pgs2.searchItemInRule("D")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )
        pgs2.searchItemInRule("E")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )
        pgs2.searchItemInRule("G")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )
        pgs2.searchItemInRule("F")
        Log.i("reached?", pgs2.hasReachedAmount(pgs2).toString() )


        //(4A+3B)||(3A+4B)


        val pgs3 = ProductGroup("""{"productId":null,"amount":1,"allocated":0,"productGroupList":[{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":"A","amount":3,"allocated":0,"productGroupList":null},{"productId":"B","amount":4,"allocated":0,"productGroupList":null}]},{"productId":null,"amount":2,"allocated":0,"productGroupList":[{"productId":"A","amount":4,"allocated":0,"productGroupList":null},{"productId":"B","amount":3,"allocated":0,"productGroupList":null}]}]}""")
        pgs3.searchItemInRule("A")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
        pgs3.searchItemInRule("A")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
        pgs3.searchItemInRule("A")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
        pgs3.searchItemInRule("B")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
        pgs3.searchItemInRule("B")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
        pgs3.searchItemInRule("B")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
        pgs3.searchItemInRule("B")
        Log.i("reached?", pgs3.hasReachedAmount(pgs3).toString() )
*/

    }


}
