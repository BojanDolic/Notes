package com.dolic.kotlinnotesapp

import android.app.Application
import androidx.work.Configuration
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.workmanager.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var notesDAO: NotesDAO



    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .setWorkerFactory(WorkerFactory(notesDAO))
        .build()
}