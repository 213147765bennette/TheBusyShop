package com.ikhokha.techcheck.presentation.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import com.ikhokha.techcheck.presentation.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel@Inject constructor(
    private val itemLocalRepository: ItemsDataRepository

    ) : ViewModel() {

    companion object{
        private var TAG = "DashboardViewModel"
    }

    private lateinit var cartItems: List<CartEntity>
    private var items = MutableLiveData<List<CartEntity>>()
    //using compositeDisposable so that i avoid multiple threads making may calls while others are still active
    private val compositeDisposable = CompositeDisposable()
    //Helpers for removing cart items
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var errorMessage = MutableLiveData<String>()
    val isDeleted: MutableLiveData<Boolean> = MutableLiveData()
    var loading = MutableLiveData<Boolean>()
    val cartId : MutableLiveData<Long> = MutableLiveData()
    val description : MutableLiveData<String> = MutableLiveData()
    val image : MutableLiveData<String> = MutableLiveData()
    val price : MutableLiveData<Double> = MutableLiveData()
    val quantity : MutableLiveData<Int> = MutableLiveData()
    val cartItemsList : MutableLiveData<List<CartEntity>> = MutableLiveData()

    init {
        getLocalCartItems()

    }

    private fun getLocalCartItems(){
        //call the progressbar first
        isLoading.value = true

        compositeDisposable.add(
            itemLocalRepository.getAllCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(TAG,"Get all Cart Items is called: $it")
                        cartItemsList.value = it

                        isLoading.value = false
                    },
                    {
                        Log.d(TAG,"Delete is called: $it")
                        isLoading.value = false

                        //here show the error message
                        onError(it.message.toString())
                    }
                )
        )
    }

    fun delete(){
        //call the progressbar first
        isLoading.value = true

        compositeDisposable.add(
            itemLocalRepository.deleteCartItem(createDeleteCartEntity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(TAG,"Delete is called: $it")
                        //kill progressbar here
                        isLoading.value = false

                        //here call the success respond of delete
                        isDeleted.value = true
                    },
                    {
                        Log.e(TAG,"onError is called: $it")
                        isDeleted.value = false

                        //here show the error message
                        onError(it.message.toString())
                    }
                )
        )
    }

    private fun createDeleteCartEntity(): CartEntity{
        return CartEntity(
            id = cartId.value!!,
            description = description.value.toString(),
            image = image.value.toString(),
            price = price.value?.toDouble()!!,
            quantity = quantity.value?.toInt()!!,
        )
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

class DashboardViewModelFactory(
    private var itemLocalRepository: ItemsDataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(itemLocalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}