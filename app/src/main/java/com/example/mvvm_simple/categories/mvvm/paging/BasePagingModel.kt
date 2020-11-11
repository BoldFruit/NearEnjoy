package com.example.mvvm_simple.categories.mvvm.paging

/**
 * Time:2020/4/13 10:24
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
open class BasePagingModel<T: IRemoteSource, R: ILocalSource> (
        val remoteDataSource: IRemoteSource,
        val localDataSource: ILocalSource
)