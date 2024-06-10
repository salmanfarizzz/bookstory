package com.example.storyapp.ui

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getStoriesWithLocation(token: String) = repository.getStoriesWithLocation(token)

    fun getUserToken(): Flow<String> = repository.getSession().map { it.token }
}


