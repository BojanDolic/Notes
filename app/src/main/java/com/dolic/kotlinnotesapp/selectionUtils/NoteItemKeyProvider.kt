package com.dolic.kotlinnotesapp.selectionUtils

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.dolic.kotlinnotesapp.adapters.NotesRecyclerAdapter
import com.dolic.kotlinnotesapp.entities.Note
import java.io.Serializable

class NoteItemKeyProvider(val adapter: NotesRecyclerAdapter) : ItemKeyProvider<Note>(SCOPE_CACHED) {

    override fun getKey(position: Int): Note? = adapter.currentList[position]

    override fun getPosition(key: Note): Int = adapter.currentList.indexOf(key)
}