package com.dolic.kotlinnotesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dolic.kotlinnotesapp.adapters.NotesRecyclerAdapter
import com.dolic.kotlinnotesapp.databinding.ActivityMainBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Time
import java.sql.Timestamp
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeStatusBarTransparent()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            binding.root.setMarginTop(insets.systemWindowInsetTop)
            //findViewById<FloatingActionButton>(R.id.fab2).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        viewmodel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val adapter = NotesRecyclerAdapter()
        binding.notesRecycler.apply {
            this.adapter = adapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        viewmodel.getAllNotes().observe(this) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            viewmodel.insertNote(Note(noteTitle = "Test title", noteDesc = binding.searchview.query.toString()))
        }



        //viewmodel.insertNote(Note(noteTitle = "Testni note", noteDesc = "Opis note", dateCreated = 0))




    }
}