package com.dolic.kotlinnotesapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dolic.kotlinnotesapp.R
import com.dolic.kotlinnotesapp.databinding.FragmentNotesBinding
import com.dolic.kotlinnotesapp.databinding.NewNoteFragmentBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.services.InsertService
import com.dolic.kotlinnotesapp.services.NoteInsertService
import com.dolic.kotlinnotesapp.setDateText
import com.dolic.kotlinnotesapp.viewmodels.NewNoteViewModel
import com.dolic.kotlinnotesapp.workmanager.InsertNoteWork
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment which creates new note
 * It is also used to update the note according to the enter parameter
 */

@AndroidEntryPoint
class NewNoteFragment : Fragment() {


    private val viewModel by viewModels<NewNoteViewModel>()

    var _binding: NewNoteFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewNoteFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationUI.setupWithNavController(
            binding.newNoteToolbar,
            findNavController()
        )

        binding.newNoteBottomappbar.replaceMenu(R.menu.toolbar_add_note_menu)
        setBottomAppBarMenuClickListener()

        parseEditDate()

    }

    /**
     * Function used to set menu items click listener
     */
    fun setBottomAppBarMenuClickListener() {
        binding.newNoteBottomappbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {

                    R.id.menu_note_add_btn -> {
                        val title = binding.addNoteTitleEdittext.text.toString().trim()
                        val desc = binding.addNoteDescEdittext.text.toString().trim()

                        if(title.isNotEmpty() && desc.isNotEmpty()) {
                            viewModel.note = Note(noteTitle = title, noteDesc = desc)
                            viewModel.insertNote(viewModel.note)
                            findNavController().navigateUp()
                        } else Snackbar.make(
                            binding.root,
                            resources.getString(R.string.new_note_error_text),
                            Snackbar.LENGTH_SHORT).show()
                    }

                }
                return true
            }
        })
    }

    fun parseEditDate() {

        val simpleDateFormat = SimpleDateFormat("yyyyMMdd")

        val editedDate = viewModel.note.dateEdited
        val currentDate = Date()

        if(simpleDateFormat.format(editedDate).equals(simpleDateFormat.format(currentDate))) {
            binding.newNoteLastEditText.text = "Last edit: Today at ${editedDate.hours}:${editedDate.minutes}"
        } else {
            val format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH)
            binding.newNoteLastEditText.text = "Last edit: ${format.format(editedDate)}"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.menu_note_add_btn -> {


            }

        }
        return super.onOptionsItemSelected(item)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}