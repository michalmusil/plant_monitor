package cz.mendelu.xmusil5.plantmonitor.communication.utils

sealed class DataResult<out T : Any> {
    class Success<out T : Any>(val data: T) : DataResult<T>()
    class Error(val error: DataError) : DataResult<Nothing>()
    class Exception(val exception: Throwable) : DataResult<Nothing>()
}