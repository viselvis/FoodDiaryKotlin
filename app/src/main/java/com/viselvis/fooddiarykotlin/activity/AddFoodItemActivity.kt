package com.viselvis.fooddiarykotlin.activity

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.databinding.ActivityAddFoodItemBinding
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory
import java.util.*
import kotlin.collections.ArrayList


class AddFoodItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFoodItemBinding
    private val addFoodItemViewModel: AddFoodItemViewModel by viewModels {
        AddFoodItemViewModelFactory((application as FoodItemListApplication).repository)
    }
    private var ingredientsArrayList=  arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFoodItemBinding.inflate(layoutInflater)
        val view = binding.root

        addFoodItemViewModel.isDataInserted.observe(this, Observer { isSuccess ->
            if (!isSuccess.equals(-1)) {
                // go back to the allnotes fragment
                Toast.makeText(this, "Insertion is successful!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Insertion is successful",  Toast.LENGTH_LONG).show()
            }
        })

        binding.mbtnSaveItem.setOnClickListener {
            if (!TextUtils.isEmpty(binding.edtFoodItemName.text) && !TextUtils.isEmpty(binding.edtFoodItemDetails.text) ) {
                val foodItemName = binding.edtFoodItemName.text.toString()
                val foodItemDetails = binding.edtFoodItemDetails.text.toString()

                insertFoodItemOnDb(foodItemName, foodItemDetails, )
            } else {
                Toast.makeText(this, "One/both of the fields are still empty!", Toast.LENGTH_LONG).show()
            }
        }

        binding.edtIngredient.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val input = binding.edtIngredient.text.toString()
                    if (input.isNotEmpty()) {
                        insertIngredient(input)
                    }
                    binding.edtIngredient.text.clear()
                    return true
                }
                return false
            }
        })

        setContentView(view)
    }

    private fun insertFoodItemOnDb(name: String, details: String) {
        val dateCreated = Calendar.getInstance().time
        val dateModified = Calendar.getInstance().time
        val newFoodItem = FoodItemModel(name, details, dateCreated, dateModified, ingredientsArrayList)

        addFoodItemViewModel.saveFoodItem(newFoodItem)
    }

    private fun insertIngredient(ingredientName: String) {
        // var newInputChip = layoutInflater.inflate(R.layout.chip_item, binding.cgpMain,false) as Chip
        ingredientsArrayList.add(ingredientName)

        val newInputChip = LayoutInflater.from(this).inflate(R.layout.chip_item, binding.fblChipgroup,  false) as Chip
        newInputChip.text = ingredientName
        // add to ChipGroup
        binding.fblChipgroup.addView(newInputChip as View, binding.fblChipgroup.childCount - 1)
        newInputChip.setOnCloseIconClickListener {
            removeItemFromArrayList(newInputChip.text.toString())
            binding.fblChipgroup.removeView(newInputChip as View)
        }
    }

    private fun removeItemFromArrayList(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ingredientsArrayList.removeIf {
                it == text
            }
        } else {
            ingredientsArrayList.remove(text)
        }
    }

//    private fun addNewChip(person: String, chipGroup: FlexboxLayout) {
//        val chip = Chip(context)
//        chip.text = person
//        chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.mipmap.ic_launcher_round)
//        chip.isCloseIconEnabled = true
//        chip.isClickable = true
//        chip.isCheckable = false
//        chipGroup.addView(chip as View, chipGroup.childCount - 1)
//        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
//    }
}