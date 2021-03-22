package com.dolic.kotlinnotesapp.selectionUtils

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.dolic.kotlinnotesapp.adapters.NotesRecyclerAdapter
import com.dolic.kotlinnotesapp.entities.Note

class NoteItemsDetailsLookup(val recyclerView: RecyclerView) : ItemDetailsLookup<Note>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Note>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if(view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)
            if(viewHolder is NotesRecyclerAdapter.ViewHolder) {
                return viewHolder.getItemDetails()
            }
        }
        return null
    }
}