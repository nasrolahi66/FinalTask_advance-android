package com.example.myapplication.MainPage

import com.example.myapplication.Pojo.MovieDetailResponseModel
import com.example.myapplication.Pojo.SearchResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface GetMoviesListInterface {
    @GET("search/movie")
    fun getMovieList(@Query("api_key") apiKey:String,@Query("query") query:String)
    : Observable<SearchResult>


    @GET("/movie/{movie_id}")
    fun getMovieDatail(@Query("api_Key") apiKey:String)
    :Observable<MovieDetailResponseModel>
}

