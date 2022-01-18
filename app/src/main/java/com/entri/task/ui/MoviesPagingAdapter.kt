package com.entri.task.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.entri.task.R
import com.entri.task.data.Movies
import com.entri.task.databinding.MoviesItemBinding

class MoviesPagingAdapter(private val listner:Onitemclick) : PagingDataAdapter<Movies,MoviesPagingAdapter.MoviesViewHolder>(MOVIE_COMPARE) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MoviesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bindUi(currentItem)
        }
    }

    inner class MoviesViewHolder(private val binding : MoviesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if (item != null){
                        listner.onItemClickListner(item)
                    }
                }

            }
        }
        private val url = "https://image.tmdb.org/t/p/w342"
        fun bindUi(movie: Movies) {
            binding.apply {
                Glide.with(itemView)
                    .load(url+movie.poster_path)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(moviePoster)

                movieName.text = movie.original_title
            }
        }

        }

    companion object {
        private val MOVIE_COMPARE = object : DiffUtil.ItemCallback<Movies>() {
            override fun areItemsTheSame(oldItem: Movies, newItem: Movies) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movies, newItem: Movies) =
                oldItem == newItem
        }
    }

    interface Onitemclick{
        fun onItemClickListner(movie: Movies)
    }


}