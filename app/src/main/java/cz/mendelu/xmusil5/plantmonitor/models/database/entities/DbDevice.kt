package cz.mendelu.xmusil5.plantmonitor.models.database.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xmusil5.plantmonitor.R

@Entity(tableName = "Devices")
data class DbDevice(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "isActive")
    val active: Boolean,
    @ColumnInfo(name = "userId")
    val userId: Long?,
    @ColumnInfo(name = "plantId")
    val plantId: Long?
){
    fun getDisplayName(context: Context): String{
        return "${context.getString(R.string.measuringDevice)} $id"
    }
}
