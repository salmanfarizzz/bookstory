package com.example.storyapp.ui

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<Story> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Parcelize
data class Story(
    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("description")
    val description: String?,

    @field:SerializedName("photoUrl")
    val photoUrl: String?,

    @field:SerializedName("createdAt")
    val createdAt: String?,

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null
) : Parcelable