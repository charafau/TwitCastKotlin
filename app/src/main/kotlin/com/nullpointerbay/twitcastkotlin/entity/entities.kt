package com.nullpointerbay.twitcastkotlin.entity

data class AccessToken(val tokenType: String, val expiresIn: Long, val accessToken: String)

data class Movie(val id: String, val user_id: String, val title: String, val subtitle: String?,
                 val category: String?, val link: String, val isLive: Boolean,
                 val isRecorded: Boolean, val commentCount: Int, val large_thumbnail: String,
                 val small_thumbnail: String, val country: String, val duration: Int,
                 val created: Int, val isCollabo: Boolean, val isProtected: Boolean,
                 val maxViewCount: Int, val currentViewCount: Int, val totalViewCount: Int,
                 val hlsUrl: String)

data class User(val id: String, val screenId: String, val name: String, val image: String,
                val profile: String, val level: Int, val lastMovieId: String?, val isLive: Boolean,
                val created: Int)

data class UserMovies(val totalCount: Int, val movies: Array<Movie>)

data class UserCurrentLive(val movie: Movie, val broadcaster: User, val tags: List<String>)

data class Comment(val id: String, val message: String, val fromUser: User, val created: Long)

data class CommentList(val movieId: String, val allCount: Int, val comments: List<Comment>)

data class SupporterUser(val id: String, val screenId: String, val name: String, val image: String,
                         val profile: String, val lastMovieId: String?, val isLive: Boolean,
                         val created: Int, val point: Int, val totalPoint: Int)

data class SupporterList(val total: Int, val supporters: List<SupporterUser>)

data class Category(val id: String, val name: String, val subCategories: List<SubCategory>)

data class SubCategory(val id: String, val name: String, val count: Int)