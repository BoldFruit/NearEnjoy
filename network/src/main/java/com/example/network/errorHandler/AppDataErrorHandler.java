package com.example.network.errorHandler;

import com.example.network.exception.ApiException;
import com.example.network.response.ApiResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


/**
 * @author YangZhaoxin.
 * @since 2020/2/7 12:44.
 * email yangzhaoxin@hrsoft.net.
 * Function 处理服务器有返回时的数据错误。数据错误时抛出 RuntimeException
 */

public class AppDataErrorHandler<T> implements Function<ApiResponse<T>, ObservableSource<T>> {

    private ArrayList<Integer> mNetCorrectCode;

    public AppDataErrorHandler(ArrayList<Integer> netCorrectCode) {
        this.mNetCorrectCode = netCorrectCode;
    }

    @Override
    public ObservableSource<T> apply(ApiResponse<T> response) throws Exception {
        if (response == null) {
            throw new ApiException(-1, "null response");
        }
        if (mNetCorrectCode != null) {
            for (Integer code : mNetCorrectCode) {
                if (response.getCode() == code) {
                    return Observable.just(response.getData());
                }
            }
        }
        throw new ApiException(response.getCode(), response.getMsg());
    }
}
