package com.project.dailymemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.dailymemo.data.ListViewModel
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    private var viewModel: ListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentLayout, MemoListFragment())
        fragmentTransaction.commit()

        viewModel = application!!.let {
            ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(it)).get(
                ListViewModel::class.java
            )
        }

        fab.setOnClickListener { view ->
            val intent = Intent(applicationContext, DetailActivity::class.java)
            startActivity(intent)
        }
    }

}
