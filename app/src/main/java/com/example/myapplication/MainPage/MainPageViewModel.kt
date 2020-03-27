package com.example.myapplication.MainPage

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Movie
import com.example.myapplication.Pojo.MovieDetailResponseModel
import com.example.myapplication.Pojo.MovieDetailResult
import com.example.myapplication.Room.MovieDataBase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.wait
import okhttp3.internal.waitMillis
import kotlin.concurrent.thread


class MainPageViewModel(context: Context):ViewModel() {

    var db:MovieDataBase
    private var movieDetail=MutableLiveData<MovieDetailResponseModel>()
    private var movieDetailFromDb=MutableLiveData<MovieDetailResult>()
    private var movieDetailForLike=MutableLiveData<MovieDetailResponseModel>()
    private var localMovieListLiveData=MutableLiveData<List<MovieDetailResult>>()
    private var movies= MutableLiveData<ArrayList<Movie>>()
    var model=MainPageModel()

    init {
        db=MovieDataBase.getInstance(context)
    }

    //region Online
    fun getMovieListByName(movieName:String) {
        model.SearchMoviesByName(movieName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val mv= ArrayList<Movie>()
                for (i in 0 .. it.results.size -1) {
                    var path = "";
                    if(it.results[i].posterPath != null){
                        path = it.results[i].posterPath!!;
                    }
                    var movie = Movie(it.results[i].id, it.results[i].title,path,it.results[i].voteAverage.toInt())
                    mv.add(movie)
                }
                movies.value =mv
            },{
                Log.d("TAG MAIN",it.message)
            })
    }
    fun getMovieLiveData():LiveData<ArrayList<Movie>> {
        return movies
    }
    fun getMovieDetailLiveData():LiveData<MovieDetailResponseModel> {
        return movieDetail
    }
    fun getMovieDetailForLike():LiveData<MovieDetailResponseModel> {
        return movieDetailForLike
    }
    fun getMovieDetail(movieId:Int) {
        model.SearchMovieDetailById(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({  var detaile=MovieDetailResponseModel(it.budget,
                it.genres,
                it.homepage,
                it.id,
                it.original_language,
                it.overview,
                it.poster_path,
                it.production_companies,
                it.production_countries,
                it.release_date,
                it.spoken_languages,
                it.title,
                it.vote_average)
                movieDetail.value=detaile
            }
                ,{
                    Log.d("TAG MAIN",it.message)})
    }
    fun getMovieDetailForLike(movieId:Int) {
        model.SearchMovieDetailById(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                var detaile=MovieDetailResponseModel(it.budget,
                    it.genres,
                    it.homepage,
                    it.id,
                    it.original_language,
                    it.overview,
                    it.poster_path,
                    it.production_companies,
                    it.production_countries,
                    it.release_date,
                    it.spoken_languages,
                    it.title,
                    it.vote_average)
                movieDetailForLike.value=detaile
            }
                ,{
                    Log.d("TAG MAIN",it.message)})

    }
    fun ShowToastForSuccessfulFavoriteOperation(context:Context) {
      Toast.makeText(context,"Movie Inserted SuccessFully",Toast.LENGTH_LONG).show()
    }
    //endregion

    //region Local
    fun getLocalMovieList(movieName:String) {
       db.movieDao()
           .getAll()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe ({
                      localMovieListLiveData.value=it
            }, {
                Log.d("TAG",it.message)
           })
    }

    fun saveLikedMovieToDb(movieDetailResponseModel: MovieDetailResponseModel) {
        val movieDetailResult= MovieDetailResult(movieDetailResponseModel.id,
            movieDetailResponseModel.poster_path,
            movieDetailResponseModel.title,
            movieDetailResponseModel.genres(),
            movieDetailResponseModel.budget,
            movieDetailResponseModel.release_date,
            movieDetailResponseModel.languages(),
            movieDetailResponseModel.vote_average,
            movieDetailResponseModel.countries(),
            movieDetailResponseModel.companies(),
            movieDetailResponseModel.overview)

        Thread{
            db.movieDao().upsert(movieDetailResult)

        }.start()

    }

    fun getLocalMovieDetail(movieId:Int) {
        db.movieDao()
            .getMovieById(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                movieDetailFromDb.value= MovieDetailResult(it.id,it.posterPath,it.title,it.genres,it.budget,it.releaseDate,it.language,it.voteAverage,it.country,it.company,it.overView)
            },
                {
                    Log.d("TAG",it.message)
                })
    }

    fun getmovieDetailFromDb():LiveData<MovieDetailResult>{
        return movieDetailFromDb
    }
    fun getlocalMovieList():LiveData<List<MovieDetailResult>> {
        return localMovieListLiveData
    }
    fun removeMovieFromFavorites(movieId: Int) {
        Thread {
            db.movieDao().delete(movieId)
        }.start()
    }
    //endregion
}