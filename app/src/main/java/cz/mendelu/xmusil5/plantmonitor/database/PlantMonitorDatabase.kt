package cz.mendelu.xmusil5.plantmonitor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.mendelu.xmusil5.plantmonitor.database.daos.PlantsDao
import cz.mendelu.xmusil5.plantmonitor.database.type_converters.*
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbDevice
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlantNote

@Database(entities = [
    DbPlant::class,
    DbDevice::class,
    DbMeasurement::class,
    DbPlantNote::class], version = 1, exportSchema = false)
@TypeConverters(
    CalendarTypeConverter::class,
    BitmapTypeConverter::class,
    MeasurementValuesTypeConverter::class,
    MeasurementValueLimitsTypeConverter::class,
    UserRoleTypeConverter::class
)
abstract class PlantMonitorDatabase : RoomDatabase() {

    abstract fun plantsDao(): PlantsDao

    companion object {

        private var INSTANCE: PlantMonitorDatabase? = null

        fun getDatabase(context: Context): PlantMonitorDatabase {
            if (INSTANCE == null) {
                synchronized(PlantMonitorDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PlantMonitorDatabase::class.java, "Plant_monitor_database"
                        ).fallbackToDestructiveMigration().build() // TODO Remove the fallbackToDestructiveMigration in production !!!
                    }
                }
            }
            return INSTANCE!!
        }
    }
}