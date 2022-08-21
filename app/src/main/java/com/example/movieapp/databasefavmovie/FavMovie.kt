package com.example.movieapp.databasefavmovie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "fav_movie_table_new_new", indices = [Index(value = ["backdrop","title"], unique = true)])
data class FavMovie (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,
    @ColumnInfo(name = "backdrop")
    var backdrop : String,
    @ColumnInfo(name = "title")
    var title : String
        )