package com.dolic.kotlinnotesapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val notesRepository: NotesRepository) : ViewModel() {

    var selecting: Boolean = false


    fun getAllNotes(): LiveData<List<Note>> = notesRepository.notes

    fun insertNote(note: Note) {
        viewModelScope.launch { notesRepository.insertNote(note) }
    }

}