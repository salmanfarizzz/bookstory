package com.example.storyapp.data.di

import android.content.Context
import com.example.storyapp.data.user.UserPref
import com.example.storyapp.data.user.UserRepository
import com.example.storyapp.data.user.dataStore

import com.example.storyapp.data.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPref.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}