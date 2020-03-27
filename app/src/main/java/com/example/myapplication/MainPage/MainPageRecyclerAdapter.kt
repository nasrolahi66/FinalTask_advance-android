package com.example.myapplication.MainPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Movie
import com.example.myapplication.R
import com.example.myapplication.setImage
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MainPageRecyclerAdapter(var movies:List<Movie>, val itemClicked:(id:Int)->Unit, val likeClicked:(id:Int)->Unit,val isLocal:Boolean): RecyclerView.Adapter<MainPageRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
        return ViewHolder(v,itemClicked,likeClicked,isLocal)
    }

    override fun getItemCount(): Int {
       return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(movies[position])
    }

    class ViewHolder( val item: View,val itemClicked: (id: Int) -> Unit, val likeClicked: (id: Int) -> Unit,val isLocal: Boolean) : RecyclerView.ViewHolder(item)
    {
        fun onBind(movie: Movie)
        {
            if(isLocal){
                item.btnFavorite.text = "Remove"
            }
            item.txtMovieName.text=movie.name
            item.txtMoviePopularity.text= movie.vote_count.toString() + " / 10"
            item.imgMovie.setImage(movie.poster)
    //        item.setOnClickListener {
    //            itemClicked(movie.id)
    //
    //        }
            item.btnFavorite.setOnClickListener {
                likeClicked(movie.id)
            }
            item.btnDetai.setOnClickListener {
                itemClicked(movie.id)
            }

        }


    }
}