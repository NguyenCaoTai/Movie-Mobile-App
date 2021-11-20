package com.sfg.moviemobileapp.ui.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.sfg.moviemobileapp.R
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.repository.model.MovieType
import com.sfg.moviemobileapp.databinding.FragmentMovieListCollectionBinding

class MovieListCollectionFragment : Fragment() {

    companion object {
        fun newInstance() = MovieListCollectionFragment()
    }

    lateinit var binding: FragmentMovieListCollectionBinding
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListCollectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpagerMovie.adapter = MovieCollectionPagerAdapter(this)

        TabLayoutMediator(binding.tabLayoutMovie, binding.viewpagerMovie) { tab, position ->
            tab.text =
                when (position) {
                    0 -> getString(R.string.title_now_playing)
                    else -> getString(R.string.title_top_rated)
                }
        }.attach()
    }
}

class MovieCollectionPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MovieListByTypeFragment.newInstance(MovieType.NowPlaying)
            else -> MovieListByTypeFragment.newInstance(MovieType.TopRated)
        }
}
