package com.dolic.kotlinnotesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dolic.kotlinnotesapp.databinding.NoteItemBinding
import com.dolic.kotlinnotesapp.entities.Note

class NotesRecyclerAdapter : ListAdapter<Note, NotesRecyclerAdapter.ViewHolder>(DIFF_CALLBACK) {

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


    var notes: List<Note> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(getItem(position))
    }


    class ViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(note: Note) {
            binding.noteItemDescriptionText.text = note.noteDesc
            binding.noteItemTitleText.text = note.noteTitle
        }

    }

}