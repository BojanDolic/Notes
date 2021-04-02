package com.dolic.kotlinnotesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dolic.kotlinnotesapp.R
import com.dolic.kotlinnotesapp.databinding.FragmentNotesBinding
import com.dolic.kotlinnotesapp.databinding.NewNoteFragmentBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.viewmodels.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        binding.addNoteBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.insertNote(Note(noteTitle = binding.addNoteTitleEdittext.text.toString()))
            }
            findNavController().navigateUp()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}