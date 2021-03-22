package com.dolic.kotlinnotesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dolic.kotlinnotesapp.adapters.NotesRecyclerAdapter
import com.dolic.kotlinnotesapp.databinding.ActivityMainBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.selectionUtils.NoteItemKeyProvider
import com.dolic.kotlinnotesapp.selectionUtils.NoteItemsDetailsLookup
import com.dolic.kotlinnotesapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Time
import java.sql.Timestamp
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainActivityViewModel

    private var tracker: SelectionTracker<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            tracker?.onRestoreInstanceState(savedInstanceState)
        }

        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //makeStatusBarTransparent()

        /*ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            binding.root.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }*/

        binding.appbarlayout.outlineProvider = null

        viewmodel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val adapter = NotesRecyclerAdapter()
        binding.notesRecycler.apply {
            this.adapter = adapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        setupTracker(adapter)

        viewmodel.getAllNotes().observe(this) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            viewmodel.insertNote(Note(noteTitle = "Test title", noteDesc = binding.searchview.query.toString()))
        }




    }

    fun setupTracker(adapter: NotesRecyclerAdapter) {
        tracker = SelectionTracker.Builder<Note>(
            "noteSelectionTracker",
            binding.notesRecycler,
            NoteItemKeyProvider(adapter),
            NoteItemsDetailsLookup(binding.notesRecycler),
            StorageStrategy.createParcelableStorage(Note::class.java))
            .withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()
            adapter.setTracker(tracker!!)

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Note>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()




            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }
}