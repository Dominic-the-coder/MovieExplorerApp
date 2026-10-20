package com.example.movieexplorerapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieexplorerapp.databinding.MovieItemBinding
import com.example.movieexplorerapp.model.Movie

class MovieAdapter(
    private var movies: List<Movie> = emptyList(),
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.tvTitle.text = movie.title
            binding.tvRating.text = "Rating: ${movie.rating}"
            binding.tvDirector.text = "Director: ${movie.director}"
            binding.tvReleaseDate.text =
                "Release Date: ${movie.releaseDate}"

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val binding = MovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}