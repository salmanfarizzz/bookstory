package com.example.storyapp.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.user.User
import com.example.storyapp.data.user.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun getUpload(token: String, file: MultipartBody.Part, description: RequestBody) =
        repository.getUpload(token, file, description)


}
