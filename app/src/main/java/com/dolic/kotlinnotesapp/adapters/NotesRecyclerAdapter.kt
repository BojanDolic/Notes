package com.dolic.kotlinnotesapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dolic.kotlinnotesapp.databinding.NoteItemBinding
import com.dolic.kotlinnotesapp.entities.Note
import com.dolic.kotlinnotesapp.setDateText

class NotesRecyclerAdapter : ListAdapter<Note, NotesRecyclerAdapter.ViewHolder>(DIFF_CALLBACK) {

    /*init {
        setHasStableIds(true)
    }*/

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.noteTitle == newItem.noteTitle && oldItem.noteDesc == newItem.noteDesc
                        && oldItem.noteTag == newItem.noteTag && oldItem.dateCreated == newItem.dateCreated
            }
        }
    }

    fun setTracker(tracker: SelectionTracker<Note>) {
        noteTracker = tracker
    }

    lateinit var noteTracker: SelectionTracker<Note>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = currentList[position]
        noteTracker.let {
            holder.bindViews(model, it.isSelected(model))
        }

    }

    inner class ViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(note: Note, isSelected: Boolean) = with(binding) {
            noteItemDescriptionText.isVisible = note.noteDesc.isNotEmpty()
            noteItemDescriptionText.text = note.noteDesc
            noteItemTitleText.text = note.noteTitle
            noteItemDateText.setDateText(note.dateCreated)

            noteItemDateContainer.isInvisible = isSelected

            if(isSelected) {
                noteCardview.setStrokeColor(ColorStateList.valueOf(Color.BLACK))
                noteCardview.strokeWidth = 6
                noteCardview.cardElevation = 12f
                noteCardview.translationZ = 12f

            } else {
                noteCardview.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#cccccc")))
                noteCardview.strokeWidth = 4
                noteCardview.cardElevation = 0f
                noteCardview.translationZ = 0f
            }

            Log.d("TAG", "bindViews: $isSelected")

        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Note> {
            return object : ItemDetailsLookup.ItemDetails<Note>() {

                override fun getPosition(): Int = adapterPosition

                override fun getSelectionKey(): Note? = currentList[adapterPosition]
            }
        }

    }

}