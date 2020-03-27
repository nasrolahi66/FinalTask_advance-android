package com.example.myapplication.MainPage

import com.example.myapplication.Pojo.MovieDetailResponseModel
import com.example.myapplication.Pojo.SearchResult
import com.example.myapplication.Retrofit.RetrofitBuilder
import com.example.myapplication.Retrofit.RetrofitInterface
import io.reactivex.Observable
import retrofit2.create


class MainPageModel {


    fun SearchMoviesByName(name:String):Observable<SearchResult>{
       val retrofit=RetrofitBuilder.buildRetrofit()
            val retrofitInterface=retrofit.create<RetrofitInterface>()
       return retrofitInterface.getMovieList(RetrofitBuilder.API_KEY,name)
    }
    fun SearchMovieDetailById(id:Int):Observable<MovieDetailResponseModel>
    {
        val retrofit=RetrofitBuilder.buildRetrofit()
        val retrofitInterface=retrofit.create<RetrofitInterface>()
        return retrofitInterface.getMovieDatail(id,RetrofitBuilder.API_KEY)

    }
}