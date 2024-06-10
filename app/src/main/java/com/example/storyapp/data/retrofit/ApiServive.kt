package com.example.storyapp.data.retrofit

import com.example.storyapp.detailstory.DetailResponse
import com.example.storyapp.addstory.AddStoryResponse
import com.example.storyapp.login.LoginResponse
import com.example.storyapp.signup.SignUpResponse
import com.example.storyapp.ui.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): SignUpResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") auth: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") auth: String,
        @Query("location") location: Int = 1,
    ): StoryResponse
}