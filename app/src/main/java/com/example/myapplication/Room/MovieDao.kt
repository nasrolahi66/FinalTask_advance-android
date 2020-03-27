package com.example.myapplication.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.Pojo.MovieDetailResult

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(movieDetail:MovieDetailResult)

    @Query("select * from movie_detail")
    fun getAll():LiveData<List<MovieDetailResult>>

   @Query("select * from movie_detail where id = :movieId")
    fun getMovieById(movieId:Int):List<MovieDetailResult>
}