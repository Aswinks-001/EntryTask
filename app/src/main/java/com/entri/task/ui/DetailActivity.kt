package com.entri.task.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.entri.task.R
import com.entri.task.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailViewModel>()
    private var movie_id: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail)

        movie_id = intent.getIntExtra("movie_id",-1)

        val actionbar = supportActionBar
        actionbar!!.title = "Movie Detail"
        actionbar.setDisplayHomeAsUpEnabled(true)



        GlobalScope.launch(Dispatchers.Main) {
            try {
                val detail = viewModel.getDetail(movie_id!!)
                binding.moviedetail = detail
                Log.e("detail","${detail.id}")
            }catch (e :Exception){
                Log.e("Catch","${e.message}")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}