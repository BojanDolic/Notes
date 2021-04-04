package com.dolic.kotlinnotesapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dolic.kotlinnotesapp.entities.Note

@Dao
interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY date_created")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE note_tag = :tag ORDER BY date_created")
    suspend fun getNotesByTag(tag: String): List<Note>

    @Query("SELECT * FROM notes_table WHERE note_title LIKE '%' || :search || '%' OR note_desc LIKE '%' || :search || '%'")
    suspend fun searchNotes(search: String): List<Note>

}