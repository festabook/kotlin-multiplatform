package com.daedan.festabook.data.datasource.remote

import de.jensklingenberg.ktorfit.Response
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(
        val data: T,
    ) : ApiResult<T>()

    data class ClientError(
        val code: Int,
        val message: String?,
        val errorBody: String?,
    ) : ApiResult<Nothing>()

    data class ServerError(
        val code: Int,
        val message: String?,
        val errorBody: String?,
    ) : ApiResult<Nothing>()

    data class NetworkError(
        val throwable: Throwable,
    ) : ApiResult<Nothing>()

    data class UnknownError(
        val message: String,
    ) : ApiResult<Nothing>()

    companion object {
        suspend fun <T> toApiResult(apiCall: suspend () -> Response<T>): ApiResult<T> =
            runCatching { apiCall() }
                .mapCatching { response ->
                    if (response.isSuccessful) {
                        val body = response.body()
                        when {
                            body != null -> {
//                                Timber.d("RES 200 $requestMethod : $requestUrl \n $body")
                                Success(body)
                            }

                            response.code == 204 -> {
//                                Timber.d("RES 204 $requestMethod : $requestUrl")
                                @Suppress("UNCHECKED_CAST")
                                Success(Unit as T)
                            }

                            else -> {
//                                Timber.e("ERR ${response.code()} $requestMethod $requestUrl - Unknown success case")
                                UnknownError("response=${response.code} Unknown success case")
                            }
                        }
                    } else {
                        val errorBody = response.errorBody()?.toString()
//                        Timber.e("ERR ${response.code()} $requestMethod $requestUrl \n $errorBody")
                        when (response.code) {
                            in 400..499 -> {
                                ClientError(
                                    response.code,
                                    response.message,
                                    errorBody,
                                )
                            }

                            in 500..599 -> {
                                ServerError(
                                    response.code,
                                    response.message,
                                    errorBody,
                                )
                            }

                            else -> {
                                UnknownError("code = ${response.code} message = ${response.message} errorBody = $errorBody")
                            }
                        }
                    }
                }.getOrElse { error ->
                    return when (error) {
                        is CancellationException -> throw error
                        is IOException -> NetworkError(error)
                        else -> UnknownError(error.stackTraceToString())
                    }
                }
    }
}
