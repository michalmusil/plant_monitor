package cz.mendelu.xmusil5.plantmonitor.models.mapper_functions

import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.*
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbMeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbMeasurementValueLimit

fun Device.toDbDevice(): DbDevice {
    return DbDevice(
        id = this.id,
        active = this.active,
        userId = this.userId,
        plantId = this.plantId
    )
}

fun MeasurementValue.toDbMeasurementValue(): DbMeasurementValue {
    return DbMeasurementValue(
        measurementType = this.measurementType,
        value = this.value
    )
}

fun MeasurementValueLimit.toDbMeasurementValueLimit(): DbMeasurementValueLimit {
    return DbMeasurementValueLimit(
        type = this.type,
        upperLimit = this.upperLimit,
        lowerLimit = this.lowerLimit
    )
}

fun Measurement.toDbMeasurement(): DbMeasurement {
    val measurementValues = this.values.map {
        it.toDbMeasurementValue()
    }
    return DbMeasurement(
        id = this.id,
        datetime = this.datetime?.calendarInUTC0,
        plantId = this.plantId,
        deviceId = this.deviceId,
        values = measurementValues
    )
}

fun Plant.toDbPlant(): DbPlant {
    val valueLimits = this.valueLimits.map {
        it.toDbMeasurementValueLimit()
    }
    return DbPlant(
        id = this.id,
        userId = this.userId,
        name = this.name,
        species = this.species,
        description = this.description,
        created = this.created?.calendarInUTC0,
        valueLimits = valueLimits,
        imageName = "",// TODO - must be added in API DTO
        image = this.titleImageBitmap
    )
}

fun PlantNote.toDbPlantNote(): DbPlantNote {
    return DbPlantNote(
        id = this.id,
        text = this.text,
        plantId = this.plantId,
        created = this.created?.calendarInUTC0
    )
}

fun User.toDbUser(): DbUser {
    return DbUser(
        id = this.userId,
        email = this.email,
        role = this.role,
        lastToken = this.token
    )
}