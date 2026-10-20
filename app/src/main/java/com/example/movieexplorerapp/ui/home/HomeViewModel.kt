package com.example.movieexplorerapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorerapp.data.api.ApiClient
import com.example.movieexplorerapp.model.Movie
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var allMovies = listOf<Movie>()
    private var searchQuery = ""
    private var sortBy = "Title"
    private var ascending = true

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                allMovies = ApiClient.api.getAllMovies()
                updateMovies()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load movies"
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchMovies(query: String) {
        searchQuery = query
        updateMovies()
    }

    fun sortMovies(sort: String, isAscending: Boolean) {
        sortBy = sort
        ascending = isAscending
        updateMovies()
    }

    private fun updateMovies() {

        val filtered = allMovies.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }

        val sorted = when (sortBy) {

            "Rating" -> filtered.sortedBy {
                it.rating
            }

            "Release Date" -> filtered.sortedBy {
                it.releaseDate
            }

            "Director" -> filtered.sortedBy {
                it.director.lowercase()
            }

            "Duration" -> filtered.sortedBy {
                it.length
            }

            else -> filtered.sortedBy {
                it.title.lowercase()
            }
        }

        _movies.value = if (ascending) {
            sorted
        } else {
            sorted.reversed()
        }
    }
}