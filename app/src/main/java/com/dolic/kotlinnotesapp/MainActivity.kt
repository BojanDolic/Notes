package com.dolic.kotlinnotesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.view.ActionMode
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
import android.view.View
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Time
import java.sql.Timestamp
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainActivityViewModel

    private var tracker: SelectionTracker<Note>? = null

    private var actionMode: ActionMode? = null

    var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        actionBar?.setDisplayShowTitleEnabled(false)


        //binding.appbarlayout.outlineProvider = null

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

        /*binding.fab.setOnClickListener {
            viewmodel.insertNote(Note(noteTitle = "Test title", noteDesc = binding.searchview.query.toString()))
        }*/

        binding.searchNotesEdittext.addTextChangedListener { text: Editable? ->
            text?.let {
                searchJob = GlobalScope.launch {
                    val notes = viewmodel.searchNotes(it.toString().trim())
                    adapter.submitList(notes)
                }
            }
        }



    }

    /**
     * Function used to create SelectionTracker
     * @param adapter Recyclerview adapter which is passed to NoteItemsDetailsLookup
     *
     * @see NoteItemsDetailsLookup
     */
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

            override fun onItemStateChanged(key: Note, selected: Boolean) {
                super.onItemStateChanged(key, selected)
            }

            override fun onSelectionRefresh() {
                super.onSelectionRefresh()
            }

            override fun onSelectionChanged() {
                super.onSelectionChanged()

                if(actionMode == null) {
                    if(tracker != null) {
                        if(tracker?.selection?.size()!! > 0) {
                            actionMode = setupActionMode(tracker)
                        } else if(tracker?.selection?.size()!! < 1)
                            actionMode?.finish()
                    }

                } else actionMode?.title = "Selected ${tracker?.selection?.size()!!}"


            }
            override fun onSelectionRestored() {
                super.onSelectionRestored()
            }
        })

    }

    override fun onPause() {
        super.onPause()
        searchJob?.cancel()
    }

    fun setupActionMode(tracker: SelectionTracker<Note>?): ActionMode? {

        val actionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.delete_toolbar_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                tracker?.clearSelection()
                actionMode = null
            }
        }
        return startSupportActionMode(actionModeCallback)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tracker?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }
}