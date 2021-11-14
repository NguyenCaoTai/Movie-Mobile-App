package com.sfg.moviemobileapp.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sfg.moviemobileapp.R
import com.sfg.moviemobileapp.movie.list.MovieListFragment

class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MovieListFragment.newInstance())
                .commitNow()
        }
    }
}