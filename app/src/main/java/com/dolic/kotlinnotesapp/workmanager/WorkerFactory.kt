package com.dolic.kotlinnotesapp.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.dolic.kotlinnotesapp.dao.NotesDAO
import javax.inject.Inject

class WorkerFactory(
    var notesDAO: NotesDAO) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            InsertNoteWork::class.java.name -> {
                InsertNoteWork(appContext, workerParameters, notesDAO)
            }
            else -> null
        }
    }
}