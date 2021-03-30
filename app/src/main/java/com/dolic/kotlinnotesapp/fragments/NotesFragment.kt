package com.dolic.kotlinnotesapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dolic.kotlinnotesapp.R
import com.dolic.kotlinnotesapp.adapters.NotesRecyclerAdapter
import com.dolic.kotlinnotesapp.databinding.FragmentNotesBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.selectionUtils.NoteItemKeyProvider
import com.dolic.kotlinnotesapp.selectionUtils.NoteItemsDetailsLookup
import com.dolic.kotlinnotesapp.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Fragment which shows all notes added to the app
 */

@AndroidEntryPoint
class NotesFragment : Fragment() {

    public interface FragmentActionModeStart {
        fun startActionMode(callback: ActionMode.Callback): ActionMode?
    }

    lateinit var actionModeInterface: FragmentActionModeStart

    var _binding: FragmentNotesBinding? = null
    val viewmodel by viewModels<NotesViewModel>()

    private var tracker: SelectionTracker<Note>? = null

    private var actionMode: ActionMode? = null
    private val notesAdapter = NotesRecyclerAdapter()

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.deleteJob?.let {
           if(it.isCancelled) {
               for(note in viewmodel.notesToDelete) {
                   viewmodel.deleteJob = lifecycleScope.launch {
                       viewmodel.deleteNote(note).await()
                       viewmodel.notesToDelete.remove(note)
                   }
               }
           }
        }

        setupRecycler()
        setupSelectionTracker()

        viewmodel.getAllNotes().observe(viewLifecycleOwner, { notes ->
            notesAdapter.submitList(notes)
        })

        /*binding.bottomAppBar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {

            override fun onMenuItemClick(item: MenuItem?): Boolean {
                TODO("Not yet implemented")
            }


        })*/

        /*binding.bottomAppBar.menu.findItem(R.id.menu_search).actionView.findViewById(R.id.)setOnClickListener {
            binding.searchEdittext.isEnabled = true
            binding.searchEdittext.requestFocus()
        }*/

    }

    /**
     * Function used to create SelectionTracker for recyclerview
     *
     * @see SelectionTracker
     */
    fun setupSelectionTracker() {

        tracker = SelectionTracker.Builder<Note>(
            "noteSelectionTracker",
            binding.notesRecycler,
            NoteItemKeyProvider(notesAdapter),
            NoteItemsDetailsLookup(binding.notesRecycler),
            StorageStrategy.createParcelableStorage(Note::class.java)
        )
            .withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()
        notesAdapter.setTracker(tracker!!)

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Note>() {

            override fun onItemStateChanged(key: Note, selected: Boolean) {
                super.onItemStateChanged(key, selected)
            }

            override fun onSelectionRefresh() {
                super.onSelectionRefresh()
            }

            override fun onSelectionChanged() {
                super.onSelectionChanged()

                val items = tracker?.selection!!.size()
                if(items > 0) {
                    if (actionMode == null) {
                        if (tracker != null) {
                            if (items > 0)
                                actionMode = setupActionMode(tracker)
                        }

                    } else actionMode?.title = "Selected ${tracker?.selection?.size()!!}"
                } else actionMode?.finish()

            }

            override fun onSelectionRestored() {
                super.onSelectionRestored()
            }
        })

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

                when(item?.itemId) {

                    R.id.delete_notes -> {
                        viewmodel.notesToDelete = tracker?.selection?.toList()?.toMutableList()!!
                        for(note in viewmodel.notesToDelete) {
                            viewmodel.deleteJob = lifecycleScope.launch {
                                viewmodel.deleteNote(note).await()
                                viewmodel.notesToDelete.remove(note)
                            }
                        }
                        tracker.clearSelection()
                        mode?.finish()
                        actionMode = null
                    }

                }

                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                tracker?.clearSelection()
                actionMode = null
            }
        }
        return actionModeInterface.startActionMode(actionModeCallback)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tracker?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionModeInterface = context as FragmentActionModeStart
    }

    fun setupRecycler() {
        binding.notesRecycler.apply {
            this.adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}