package com.dolic.kotlinnotesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.entities.Note

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDAO

    companion object {

        @Volatile
        var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "notes_database"
                )
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }

}