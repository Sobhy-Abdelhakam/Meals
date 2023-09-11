package dev.sobhy.meals.domain.model.area

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class Area(
    @PrimaryKey
    val strArea: String
)