package uz.apphub.fayzullo.utils




data class NetworkResult<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?): NetworkResult<T> =
            NetworkResult(Status.SUCCESS, data, null)

        fun <T> loading(): NetworkResult<T> {
            return NetworkResult(Status.LOADING, null, null)
        }

        fun <T> error(message: String?): NetworkResult<T> =
            NetworkResult(Status.ERROR, null, message)
    }
}