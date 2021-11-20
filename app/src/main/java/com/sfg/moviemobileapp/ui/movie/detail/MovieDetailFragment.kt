package com.sfg.moviemobileapp.ui.movie.detail

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sfg.moviemobileapp.R
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.repository.MovieByPageRepository
import com.sfg.moviemobileapp.databinding.FragmentMovieDetailBinding
import kotlinx.coroutines.flow.collectLatest


class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null

    private val binding get() = _binding!!

    val progress: ProgressDialog by lazy {
        ProgressDialog(requireContext())
            .apply {
                setTitle("Loading")
                setCancelable(false) // disable dismiss by tapping outside of the dialog
            }
    }

    private val viewModel: MovieDetailViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                @Suppress("UNCHECKED_CAST")
                return arguments
                    ?.takeIf { it.containsKey(ARG_ITEM_ID) }
                    ?.getString(ARG_ITEM_ID)
                    ?.let {
                        MovieDetailViewModel(
                            it,
                            MovieByPageRepository(
                                MovieApi.create()
                            )
                        ) as T
                    }
                    ?: throw IllegalArgumentException("You have to pass ARG_ITEM_ID")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.movieDetail
                .collectLatest {
                    when (it) {
                        ViewState.Loading -> showLoadingDialog()
                        is ViewState.Error -> {
                            progress.dismiss()
                            showErrorInfo(it.info)
                        }
                        is ViewState.Success -> {
                            progress.dismiss()
                            bindingData(it.payload)
                        }
                    }
                }
        }
    }

    private fun showErrorInfo(info: String) {
        activity
            ?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.error_dialog_title)
                    .setMessage(info)
                    .create()
            }
            ?.show()
    }

    private fun showLoadingDialog() {
        progress.show()
    }

    private fun bindingData(movie: MovieInfo) {
        with(binding) {
            title.text = movie.title
            info.text = movie.mixedInfo
            overview.text = movie.overview
            movie.backdrop
                ?.run {
                    Glide.with(this@MovieDetailFragment)
                        .load(this)
                        .into(backdrop)
                }
        }
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}