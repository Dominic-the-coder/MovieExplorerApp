package com.example.movieexplorerapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieexplorerapp.databinding.FragmentDetailsBinding
import com.example.movieexplorerapp.ui.home.HomeViewModel
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getInt("movieId", -1)

        val movie = homeViewModel.movies.value.find {
            it.id == movieId
        }

        if (movie != null) {
            detailsViewModel.setMovie(movie)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            detailsViewModel.movie.collect { selectedMovie ->
                selectedMovie?.let {
                    binding.tvTitle.text = it.title
                    binding.tvRating.text = "Rating: ${it.rating}"
                    binding.tvDirector.text = "Director: ${it.director}"
                    binding.tvReleaseDate.text =
                        "Release Date: ${it.releaseDate}"
                    binding.tvDuration.text =
                        "Duration: ${it.length} minutes"
                    binding.tvOverview.text =
                        "Overview: ${it.overview}"
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}