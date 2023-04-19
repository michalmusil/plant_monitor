package cz.mendelu.xmusil5.plantmonitor.models.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import cz.mendelu.xmusil5.plantmonitor.models.enums.Role

@Entity(tableName = "Users")
data class DbUser(
    @PrimaryKey
    @ColumnInfo(name="id")
    val id: Long,
    @ColumnInfo(name="email")
    val email: String,
    @ColumnInfo(name="role")
    var role: Role,
    @Json(name="token")
    var lastToken: String
)
