package cz.mendelu.xmusil5.plantmonitor.models.database.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbMeasurementValueLimit
import java.util.Calendar

@Entity(tableName = "Plants")
data class DbPlant(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "userId")
    val userId: Long?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "created")
    val created: Calendar?,
    @ColumnInfo(name = "imageName")
    val imageName: String?,
    @ColumnInfo(name = "image")
    val image: Bitmap? = null,
    @ColumnInfo(name = "valueLimits")
    val valueLimits: List<DbMeasurementValueLimit>
)
