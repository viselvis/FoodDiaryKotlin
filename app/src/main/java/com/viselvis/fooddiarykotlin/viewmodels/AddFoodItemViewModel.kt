package com.viselvis.fooddiarykotlin.viewmodels

import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.launch

class AddFoodItemViewModel(private val repo: FoodItemRepository) : ViewModel() {
    private var _isDataInserted = MutableLiveData<Long>()
    val isDataInserted: LiveData<Long>
        get() = _isDataInserted

    fun saveFoodItem(foodItem: FoodItemModel) = viewModelScope.launch {
        _isDataInserted.value = repo.insertFoodItem(foodItem)
    }
}

class AddFoodItemViewModelFactory (private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFoodItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddFoodItemViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}