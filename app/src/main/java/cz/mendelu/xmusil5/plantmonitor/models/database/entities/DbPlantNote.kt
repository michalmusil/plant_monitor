package cz.mendelu.xmusil5.plantmonitor.models.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "PlantNotes")
data class DbPlantNote(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "plantId")
    val plantId: Long,
    @ColumnInfo(name = "created")
    val created: Calendar?,
)
