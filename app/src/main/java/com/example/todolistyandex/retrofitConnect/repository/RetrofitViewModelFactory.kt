package com.example.todolistyandex.retrofitConnect.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.testing.retrofitConnect.RetrofitViewModel
import com.bignerdranch.android.testing.retrofitConnect.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class RetrofitViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RetrofitViewModel(repository) as T
    }
}