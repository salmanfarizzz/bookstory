package com.example.storyapp.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Result
import com.example.storyapp.data.user.UserRepository
import com.example.storyapp.ui.Story
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _detailResult = MutableLiveData<Result<Story>>()
    val detailResult: LiveData<Result<Story>> = _detailResult

    fun getDetail(token: String, id: String) {
        viewModelScope.launch {
            repository.detail(token, id).collect{ result ->
                _detailResult.value = result
            }
        }
    }
}