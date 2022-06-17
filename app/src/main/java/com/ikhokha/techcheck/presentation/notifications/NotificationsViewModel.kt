package com.ikhokha.techcheck.presentation.notifications

import androidx.lifecycle.*
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import com.ikhokha.techcheck.presentation.dashboard.DashboardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel@Inject constructor(
    private val itemLocalRepository: ItemsDataRepository
    ) : ViewModel() {

    companion object{
        private var TAG = "NotificationsViewModel"
    }

    fun deleteAll():Int{
        var deleted = 0
        viewModelScope.launch {
            deleted =  itemLocalRepository.deleteAll()
        }
        return deleted
    }
}

class NotificationsViewModelFactory(
    private var itemLocalRepository: ItemsDataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(itemLocalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}