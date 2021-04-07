package com.dolic.kotlinnotesapp.viewmodels

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.repository.NotesRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    val notesRepository: NotesRepository
) : ViewModel() {

    // Retrieving bottomsheet state
    lateinit var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>

    val noteColor: Int get() = note.noteColor

    var note: Note = Note()

    fun insertNote(note: Note) {
        notesRepository.insertNote(note)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch { notesRepository.deleteNote(note) }
    }
}