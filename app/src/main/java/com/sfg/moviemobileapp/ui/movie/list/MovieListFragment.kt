package com.sfg.moviemobileapp.ui.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.repository.MovieByPageRepository
import com.sfg.moviemobileapp.databinding.MovieListFragmentBinding
import com.sfg.moviemobileapp.ui.movie.list.adapter.MovieAdapter
import com.sfg.moviemobileapp.ui.movie.list.adapter.MoviesLoadStateAdapter
import com.sfg.moviemobileapp.ui.movie.list.adapter.asMergedLoadStates
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class MovieListFragment : Fragment() {

    companion object {
        fun newInstance() = MovieListFragment()
    }

    lateinit var binding: MovieListFragmentBinding
        private set
    private val model: MovieListViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(
                    MovieByPageRepository(
                        MovieApi.create()
                    )
                ) as T
            }
        }
    }

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSwipeToRefresh()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.loadEvent.tryEmit("androiddev")
    }

    private fun initAdapter() {
        adapter = MovieAdapter()

        binding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter(adapter),
            footer = MoviesLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .collect {
                    binding.swipeRefresh.isRefreshing =
                        it.mediator?.refresh is LoadState.Loading
                }
        }

        lifecycleScope.launchWhenCreated {
            model.posts.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .asMergedLoadStates()
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

}