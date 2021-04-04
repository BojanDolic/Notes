package com.dolic.kotlinnotesapp.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dolic.kotlinnotesapp.NotesDatabase
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.entities.Note
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "InsertNoteWork"

@HiltWorker
class InsertNoteWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    var notesDAO: NotesDAO
) : CoroutineWorker(
    appContext,
    workerParameters
) {
    

    override suspend fun doWork(): Result {
        return try {
            try {
                notesDAO.insertNote(Note(noteTitle = "Implementacija service-a"))
                Result.success()
            } catch (e: Exception) {
                Log.e(TAG, "doWork: ", e)
                Result.failure()
            }

        } catch (e: Exception) {
            Log.e(TAG, "doWork: ", e)
            Result.failure()
        }
    }
}