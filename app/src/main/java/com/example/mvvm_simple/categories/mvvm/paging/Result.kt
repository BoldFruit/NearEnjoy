package com.example.mvvm_simple.categories.mvvm.paging

/**
 * Time:2020/4/13 10:05
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */

sealed class Result<out T> {
    companion object {
        fun <T> success(result: T): Result<T> = Success(result)
        fun <T> idle(): Result<T> = Idle
        fun <T> loading(): Result<T> = Loading
        fun <T> failure(error: Throwable): Result<T> = Failure(error)
    }
    object Idle: Result<Nothing>()
    object Loading: Result<Nothing>()
    data class Failure(val error: Throwable): Result<Nothing>()
    data class Success<out T>(val data: T): Result<T>()
}