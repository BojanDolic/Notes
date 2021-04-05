package com.dolic.kotlinnotesapp.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "notes_table")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "note_title") val noteTitle: String = "",
    @ColumnInfo(name = "note_desc") val noteDesc: String = "",
    @ColumnInfo(name = "date_created") val dateCreated: Date = Date(),
    @ColumnInfo(name = "date_edited") val dateEdited: Date = Date(),
    @ColumnInfo(name = "note_tag") val noteTag: String = "",
    @ColumnInfo(name = "note_color") val noteColor: Int = -1
) : Parcelable
