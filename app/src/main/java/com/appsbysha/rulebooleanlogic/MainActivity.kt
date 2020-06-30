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
            productGroup.allocateRules(enterProductsEditText.text.toString())
            resultTV.text = productGroup.groupOfAllocatedReachedAmount().toString()
                enterProductsEditText.setText("")
        }

    }


}
