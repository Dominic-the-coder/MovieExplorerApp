package com.example.movieexplorerapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieexplorerapp.R
import com.example.movieexplorerapp.databinding.FragmentHomeBinding
import com.example.movieexplorerapp.ui.adapter.MovieAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private val movieAdapter = MovieAdapter { movie ->

        val bundle = Bundle().apply {
            putInt("movieId", movie.id)
        }

        findNavController().navigate(
            R.id.action_homeFragment_to_detailsFragment,
            bundle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
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

        setupRecyclerView()
        setupSearch()
        setupSort()
        observeMovies()
    }

    private fun setupRecyclerView() {
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
        }
    }

    private fun setupSearch() {
        binding.ivSearch.setOnClickListener {
            viewModel.searchMovies(
                binding.etSearch.text.toString().trim()
            )
        }
    }

    private fun setupSort() {

        val listener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sortBy =
                    binding.spSortBy.selectedItem.toString()

                val ascending =
                    binding.spSortOrder.selectedItemPosition == 0

                viewModel.sortMovies(sortBy, ascending)
            }

            override fun onNothingSelected(
                parent: AdapterView<*>?
            ) {}
        }

        binding.spSortBy.onItemSelectedListener = listener
        binding.spSortOrder.onItemSelectedListener = listener
    }

    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                launch {
                    viewModel.movies.collect { movies ->
                        movieAdapter.updateMovies(movies)
                        updateUI()
                    }
                }

                launch {
                    viewModel.loading.collect {
                        updateUI()
                    }
                }

                launch {
                    viewModel.error.collect {
                        updateUI()
                    }
                }
            }
        }
    }

    private fun updateUI() {
        val isLoading = viewModel.loading.value
        val movies = viewModel.movies.value
        val error = viewModel.error.value

        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.rvMovies.visibility =
            if (!isLoading && movies.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

        binding.layoutEmpty.visibility =
            if (!isLoading && movies.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

        binding.tvEmpty.text =
            error ?: "No movies found"
    }

    override fun onDestroyView() {
        binding.rvMovies.adapter = null
        _binding = null
        super.onDestroyView()
    }
}