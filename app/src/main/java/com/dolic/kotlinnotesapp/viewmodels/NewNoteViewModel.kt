package com.dolic.kotlinnotesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    val notesRepository: NotesRepository
) : ViewModel() {

    var note: Note = Note()

    fun insertNote(note: Note) {
        notesRepository.insertNote(note)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch { notesRepository.deleteNote(note) }
    }
}