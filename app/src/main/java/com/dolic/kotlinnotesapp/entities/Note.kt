package com.dolic.kotlinnotesapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "note_title") val noteTitle: String = "",
    @ColumnInfo(name = "note_desc") val noteDesc: String = "",
    @ColumnInfo(name = "date_created") val dateCreated: Long = 0,
    @ColumnInfo(name = "note_tag") val noteTag: String = ""
)
