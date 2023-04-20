package cz.mendelu.xmusil5.plantmonitor.models.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbMeasurementValue
import java.util.Calendar

@Entity(tableName = "Measurements")
data class DbMeasurement(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "taken")
    val datetime: Calendar?,
    @ColumnInfo(name = "plantId")
    val plantId: Long,
    @ColumnInfo(name = "deviceId")
    val deviceId: Long,
    @ColumnInfo(name = "values")
    val values: List<DbMeasurementValue>
)
