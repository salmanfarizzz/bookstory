package com.example.storyapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.user.User
import com.example.storyapp.data.user.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun story(token: String) : LiveData<PagingData<Story>> = repository.getPaging(token).cachedIn(viewModelScope)

    // Remove the static quote initialization
    val quote: LiveData<PagingData<Story>> = MutableLiveData()
}
