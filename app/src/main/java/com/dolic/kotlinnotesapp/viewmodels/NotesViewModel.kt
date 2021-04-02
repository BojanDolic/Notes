package com.dolic.kotlinnotesapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    val notesRepository: NotesRepository) : ViewModel() {

    var searchJob: Job? = null
    var deleteJob: Job? = null

    var searchQuery = ""

    var notesToDelete: MutableList<Note> = mutableListOf()

    fun getAllNotes(): LiveData<List<Note>> = notesRepository.notes

    suspend fun deleteNote(note: Note) = viewModelScope.async(Dispatchers.IO) {
        notesRepository.deleteNote(note)
    }



    suspend fun searchNotes(search: String): List<Note> = viewModelScope.async {
            notesRepository.searchNotes(search)
        }.await()
}