package com.example.storyapp

import com.example.storyapp.ui.Story


object DataDummy {

    fun generateDummyQuoteResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val quote = Story(
                i.toString(),
                "Tiara",
                "test",
                "",
                "2023-05-07T22:10:15.213Z",
                37.422092,
                -122.08392
            )
            items.add(quote)
        }
        return items
    }
}