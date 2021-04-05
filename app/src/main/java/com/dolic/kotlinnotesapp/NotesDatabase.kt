package com.dolic.kotlinnotesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.entities.Note

@Database(entities = arrayOf(Note::class), version = 6, exportSchema = false)
@TypeConverters(Converters::class)
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
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }

}