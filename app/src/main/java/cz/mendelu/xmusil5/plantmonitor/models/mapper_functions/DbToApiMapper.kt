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

fun DbDevice.toApiDevice(): Device{
    return Device(
        id = this.id,
        active = this.active,
        userId = this.userId,
        plantId = this.plantId
    )
}

fun DbMeasurementValue.toApiMeasurementValue(): MeasurementValue{
    return MeasurementValue(
        measurementType = this.measurementType,
        value = this.value
    )
}

fun DbMeasurementValueLimit.toApiMeasurementValueLimit(): MeasurementValueLimit{
    return MeasurementValueLimit(
        type = this.type,
        upperLimit = this.upperLimit,
        lowerLimit = this.lowerLimit
    )
}

fun DbMeasurement.toApiMeasurement(): Measurement{
    val measurementValues = this.values.map {
        it.toApiMeasurementValue()
    }
    val dateTime = if (this.datetime != null) DateTimeFromApi("", this.datetime) else null
    return Measurement(
        id = this.id,
        datetime = dateTime,
        plantId = this.plantId,
        deviceId = this.deviceId,
        values = measurementValues
    )
}

fun DbPlant.toApiPlant(): Plant{
    val valueLimits = this.valueLimits.map {
        it.toApiMeasurementValueLimit()
    }
    val created = if (this.created != null) DateTimeFromApi("", this.created) else null
    return Plant(
        id = this.id,
        userId = this.userId,
        name = this.name,
        species = this.species,
        description = this.description,
        hasTitleImage = this.imageName != null && this.imageName.isNotBlank(),
        created = created,
        valueLimits = valueLimits
    )
}

fun DbPlantNote.toApiPlantNote(): PlantNote{
    val created = if (this.created != null) DateTimeFromApi("", this.created) else null
    return PlantNote(
        id = this.id,
        text = this.text,
        plantId = this.plantId,
        created = created
    )
}

fun DbUser.toApiUser(): User{
    return User(
        userId = this.id,
        email = this.email,
        role = this.role,
        token = this.lastToken
    )
}




