package com.example.myapplication.MainPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.DetailPage.DetailActivivity
import com.example.myapplication.Movie
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import okhttp3.internal.wait
import okhttp3.internal.waitMillis
import java.text.NumberFormat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var viewModel:MainPageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val factory=ViewModelFactory(this)
        viewModel=ViewModelProvider(this,factory).get(MainPageViewModel::class.java)

        //region Online
        var recyclerLikeCliched:(movieID:Int)->Unit ={
            viewModel.getMovieDetailForLike(it)
        }
        var recyclerDetailClicked:(movieID:Int)->Unit ={
            viewModel.getMovieDetail(it)
        }

        viewModel.getMovieDetailLiveData().observe(this, Observer {
            val intent= Intent(this,DetailActivivity::class.java)
            intent.putExtra("posterPath",it.poster_path)
            intent.putExtra("title",it.title)
            intent.putExtra("genre",it.genres())
            intent.putExtra("budget",NumberFormat.getIntegerInstance().format( it.budget ) + " $" )
            intent.putExtra("releaseDate",it.release_date)
            intent.putExtra("language",it.languages())
            intent.putExtra("voteAverage",it.vote_average)
            intent.putExtra("country",it.countries())
            intent.putExtra("company",it.companies())
            intent.putExtra("overView",it.overview)

            startActivity(intent)
        })
        viewModel.getMovieDetailForLike().observe(this, Observer {
            viewModel.saveLikedMovieToDb(it)
            viewModel.ShowToastForSuccessfulFavoriteOperation(this@MainActivity)
        })
        viewModel.getMovieLiveData().observe(this, Observer {
            val adapter=MainPageRecyclerAdapter(it,recyclerDetailClicked,recyclerLikeCliched,false)
            recycler.adapter=adapter
        })

        btnSearch.setOnClickListener {
            viewModel.getMovieListByName(edtSearch.text.toString())

        }
        //endregion

        //region Local
        var recyclerLocalLikeCliched:(movieID:Int)->Unit ={
            viewModel.removeMovieFromFavorites(it)
        }

        var recyclerLocalDetailClicked:(movieID:Int)->Unit ={
            viewModel.getLocalMovieDetail(it)
        }
        viewModel.getlocalMovieList().observe(this, Observer {
            var movieList=ArrayList<Movie>()
            it.forEach {
                var movie=Movie(it.id,it.title,it.posterPath,it.voteAverage.toInt())
                movieList.add(movie)
            }
            var adapter=MainPageRecyclerAdapter(movieList,recyclerLocalDetailClicked,recyclerLocalLikeCliched,true)
            recycler.adapter=adapter
        })

        viewModel.getmovieDetailFromDb().observe(this, Observer {
            val intent= Intent(this,DetailActivivity::class.java)
            intent.putExtra("posterPath",it.posterPath)
            intent.putExtra("title",it.title)
            intent.putExtra("genre",it.genres)
            intent.putExtra("budget",NumberFormat.getIntegerInstance().format( it.budget ) + " $" )
            intent.putExtra("releaseDate",it.releaseDate)
            intent.putExtra("language",it.language)
            intent.putExtra("voteAverage",it.voteAverage)
            intent.putExtra("country",it.country)
            intent.putExtra("company",it.company)
            intent.putExtra("overView",it.overView)

            startActivity(intent)
        })

        btnSearchLocal.setOnClickListener {
            viewModel.getLocalMovieList(edtSearch.text.toString())
        }
        //endregion
    }
}
