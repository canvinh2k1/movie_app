package com.example.movieapp.databaseremmovie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "rem_movie_table_new", indices = [Index(value = ["poster","title","ratting"], unique = true)])
data class RemMovie (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,
    @ColumnInfo(name = "poster")
    var poster : String,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "time")
    var time : String,
    @ColumnInfo(name = "ratting")
    var ratting : Float
)