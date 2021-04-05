package com.dolic.kotlinnotesapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.dolic.kotlinnotesapp.R
import com.dolic.kotlinnotesapp.databinding.NewNoteFragmentBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.util.Constants
import com.dolic.kotlinnotesapp.viewmodels.NewNoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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

    private val args by navArgs<NewNoteFragmentArgs>()

    var _binding: NewNoteFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    /**
     * Function used to parse date at which note was last edited
     */
    fun parseEditDate() {

        // If user entered fragment to edit the note, parse last edit text
        // Else hide last edit text
        if(args.actionType == Constants.EDIT_NOTE_NAV) {
            val simpleDateFormat = SimpleDateFormat("yyyyMMdd")

            val editedDate = viewModel.note.dateEdited
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = editedDate

            if (simpleDateFormat.format(editedDate).equals(simpleDateFormat.format(currentDate))) {
                binding.newNoteLastEditText.text =
                    resources.getString(
                        R.string.new_note_edited_today_date_text,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
            } else {
                val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH)
                binding.newNoteLastEditText.text =
                    resources.getString(
                        R.string.new_note_edited_date_text,
                        dateFormat.format(editedDate)
                    )
            }
        } else binding.newNoteLastEditText.isVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}