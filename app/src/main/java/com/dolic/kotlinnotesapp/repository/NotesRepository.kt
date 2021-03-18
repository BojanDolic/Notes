package com.dolic.kotlinnotesapp.repository

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.dolic.kotlinnotesapp.NotesDatabase
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepository @Inject constructor(
    val notesDAO: NotesDAO
) {

    // Gets all notes
    var notes: LiveData<List<Note>> = notesDAO.getAllNotes()

    @WorkerThread
    suspend fun insertNote(note: Note) {
        notesDAO.insertNote(note)
    }

}