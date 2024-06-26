package com.akmal.slide_wordpress.model

data class Article(
    val id: Int,
    val title: Title,
    val categories: List<Int>,
    val featured_media: Int
)

data class Title(
    val rendered: String
)

data class Media(
    val source_url: String
)

data class Category(
    val id: Int,
    val name: String
)

