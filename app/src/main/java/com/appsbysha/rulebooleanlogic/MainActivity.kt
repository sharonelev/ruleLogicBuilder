package com.appsbysha.rulebooleanlogic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var productGroup: ProductGroup = ProductGroup()
 //   var productList: HashMap<String, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //  productGroup.allCombos(mutableListOf("A","B","C","D"),4)

    //    productGroup.permute("ABC".toCharArray(),0)
     //   productGroup.permute("ABCD".toCharArray(),0)

        editString.setText("((a+2b)||(2a+b))")

        doneButton.setOnClickListener { productGroup = LogicStringReader(editString.text.toString(), this).addProductGroup()
            editString.isEnabled = false
            Log.i("viewPG", "" )}

        clearButton.setOnClickListener { editString.setText("")
            editString.isEnabled = true
            resultTV.text = ""
            enteredProducts.text = ""

        }

        addButton.setOnClickListener {
            enteredProducts.text = ("${enteredProducts.text}${enterProductsEditText.text.toString()}")
            //resultTV.text = productGroup.searchItemInRule(enterProductsEditText.text.toString()).toString()

            //productGroup.multipleMeetsRule(enterProductsEditText.text.toString(), hashMapOf())
            productGroup.allocateRules(enterProductsEditText.text.toString())
            resultTV.text = productGroup.groupOfAllocatedReachedAmount().toString()
                enterProductsEditText.setText("")
        }

    }


}
