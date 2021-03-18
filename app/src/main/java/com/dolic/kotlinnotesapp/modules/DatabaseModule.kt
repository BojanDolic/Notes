package com.dolic.kotlinnotesapp.modules

import android.content.Context
import androidx.room.Room
import com.dolic.kotlinnotesapp.MyApplication
import com.dolic.kotlinnotesapp.NotesDatabase
import com.dolic.kotlinnotesapp.dao.NotesDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "notes_database"
        )
            .build()
    }

    @Provides
    fun provideNotesDAO(database: NotesDatabase): NotesDAO {
        return database.notesDao()
    }


}