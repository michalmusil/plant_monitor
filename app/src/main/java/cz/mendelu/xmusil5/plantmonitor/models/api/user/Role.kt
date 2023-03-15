package cz.mendelu.xmusil5.plantmonitor.models.api.user

import cz.mendelu.xmusil5.plantmonitor.R

enum class Role(val roleNumber: Int, val roleNameId: Int) {
    USER(0, R.string.commonUser),
    ADMIN(1, R.string.admin);

    companion object {
        fun getByRoleNumber(roleNumber: Int): Role?{
            return values().firstOrNull { it.roleNumber == roleNumber }
        }
    }
}