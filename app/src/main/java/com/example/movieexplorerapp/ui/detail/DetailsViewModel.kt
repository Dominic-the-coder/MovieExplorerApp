package com.example.movieexplorerapp.ui.detail

import androidx.lifecycle.ViewModel
import com.example.movieexplorerapp.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel : ViewModel() {

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    fun setMovie(movie: Movie) {
        _movie.value = movie
    }
}
