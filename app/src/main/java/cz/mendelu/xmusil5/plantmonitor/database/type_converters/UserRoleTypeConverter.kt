package cz.mendelu.xmusil5.plantmonitor.database.type_converters

import android.graphics.Bitmap
import androidx.room.TypeConverter
import cz.mendelu.xmusil5.plantmonitor.models.enums.Role
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils

class UserRoleTypeConverter {
    @TypeConverter
    fun saveUserRoleToDb(role: Role): Int {
        return role.roleNumber
    }

    @TypeConverter
    fun getUserRoleFromDb(roleInDb: Int): Role {
        return Role.getByRoleNumber(roleInDb)!!
    }
}