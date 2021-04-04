package com.dolic.kotlinnotesapp.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.dolic.kotlinnotesapp.NotesDatabase
import com.dolic.kotlinnotesapp.dao.NotesDAO
import com.dolic.kotlinnotesapp.entities.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class InsertService : JobIntentService() {

    @Inject
    lateinit var dao: NotesDAO

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun enqueWork(context: Context, work: Intent) {
        enqueueWork(context, InsertService::class.java, 1, work)
    }

    override fun onHandleWork(intent: Intent) {
        val title = intent.getStringExtra("noteTitle")
        val desc = intent.getStringExtra("noteDesc")

        if(title != null && desc != null) {
            if(title.isNotEmpty() && desc.isNotEmpty()) {
                Log.d("TAG", "onHandleIntent: PROŠLO PROVJERU SERVICE")
                //val database = NotesDatabase.getInstance(applicationContext)
                scope.launch {
                    dao.insertNote(Note(noteTitle = title, noteDesc = desc))
                    Log.d("TAG", "onHandleIntent: UBAČEN NOTES")
                }


            }
        }
        Log.d("TAG", "onHandleIntent: NIJE PROŠLO PROVJERU")
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("TAG", "onCreate: SERVICE KREIRAN")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d("TAG", "onCreate: SERVICE UNIŠTEN")
    }
}