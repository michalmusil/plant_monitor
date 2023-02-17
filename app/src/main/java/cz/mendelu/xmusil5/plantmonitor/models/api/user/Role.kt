package cz.mendelu.xmusil5.plantmonitor.models.api.user

enum class Role(val roleNumber: Int) {
    USER(0),
    ADMIN(1);

    companion object {
        fun getByRoleNumber(roleNumber: Int): Role?{
            return values().firstOrNull { it.roleNumber == roleNumber }
        }
    }
}