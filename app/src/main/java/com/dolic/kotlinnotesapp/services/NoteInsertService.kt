package com.dolic.kotlinnotesapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.dolic.kotlinnotesapp.NotesDatabase
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.entities.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "NoteInsertService"

@AndroidEntryPoint
class NoteInsertService : Service() {

    /*@Inject
    lateinit var notesdao: NotesDAO*/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        GlobalScope.launch {
            val job = launch {
                NotesDatabase.getInstance(this@NoteInsertService).notesDao().insertNote(Note(noteTitle = "Implementacija service-a"))
            }
            job.join()
            Log.d(TAG, "onCreate: INSERTOVAN NOTE")
            stopSelf()
        }


        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: SERVICE KREIRAN")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}