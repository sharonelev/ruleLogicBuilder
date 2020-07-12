package com.appsbysha.rulebooleanlogic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var productGroup: ProductGroup = ProductGroup()
 //   var productList: HashMap<String, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // editString.setText("((2a+b)||(a+2b))") //for testing
        doneButton.setOnClickListener { productGroup = LogicStringReader(editString.text.toString(), this).addProductGroup()
            editString.isEnabled = false
            val gson = Gson()
            val jsonList: String = gson.toJson(productGroup)
            Log.i("viewPG", jsonList )}

        clearButton.setOnClickListener { editString.setText("")
            editString.isEnabled = true
            resultTV.text = ""
            enteredProducts.text = ""

        }

        addButton.setOnClickListener {
            enteredProducts.text = ("${enteredProducts.text}${enterProductsEditText.text.toString()}")
            val itemList: List<Char> = enteredProducts.text?.toList()?: listOf()
            resultTV.text = productGroup.allocateWithItemPermutation(itemList,0).toString()
                enterProductsEditText.setText("")
        }

    }


}
