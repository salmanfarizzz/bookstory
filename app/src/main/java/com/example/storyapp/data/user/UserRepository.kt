package com.example.storyapp.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.ErrorResponse
import com.example.storyapp.data.Result
import com.example.storyapp.ui.Story
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.data.StoryPagingSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPref
) {

    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun signup(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun story(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStory("Bearer $token")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    suspend fun detail(token: String, id: String): Flow<Result<Story>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDetail("Bearer $token", id)
            val story = response.story
            emit(Result.Success(story))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    fun getUpload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<Any>> = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.postStory("Bearer $token", file, description)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun getStoriesWithLocation(token: String): LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation("Bearer $token")
            emit(Result.Success(response.listStory))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    fun getPaging(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPref
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}