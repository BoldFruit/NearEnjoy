package com.example.network.errorHandler;

import android.util.MalformedJsonException;

import com.example.network.exception.ApiException;
import com.example.network.exception.ExceptionType;
import com.example.network.exception.ServerException;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 11:40.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class ExceptionHandler {

    // 未授权 请求要求身份验证
    private static final int UNAUTHORIZED = 401;
    // 禁止 服务器拒绝请求
    private static final int FORBIDDEN = 403;
    // 资源未找到
    private static final int NOT_FOUND = 404;
    // 请求超时
    private static final int REQUEST_TIMEOUT = 408;
    // 服务器内部错误
    private static final int INTERNAL_SERVER_ERROR = 500;
    // 网关错误
    private static final int BAD_GATEWAY = 502;
    // 服务器目前无法使用
    private static final int SERVICE_UNAVAILABLE = 503;
    // 网关超时
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable throwable) {
        throwable.printStackTrace();
        ApiException apiException;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            apiException = new ApiException(throwable, ExceptionType.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    apiException.setMsg("网络错误");
                    break;
            }
            return apiException;
        } else if (throwable instanceof ServerException) {
            ServerException serverException = (ServerException) throwable;
            apiException = new ApiException(throwable, serverException.getCode());
            apiException.setMsg(serverException.getMsg());
            return apiException;
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException
                || throwable instanceof MalformedJsonException) {
            apiException = new ApiException(throwable, ExceptionType.PARSE_SERVER_DATA_ERROR);
            apiException.setMsg("服务器数据解析错误");
            return apiException;
        } else if (throwable instanceof ConnectException) {
            apiException = new ApiException(throwable, ExceptionType.NETWORK_ERROR);
            apiException.setMsg("网络连接失败");
            return apiException;
        } else if (throwable instanceof SSLHandshakeException) {
            apiException = new ApiException(throwable, ExceptionType.SSL_ERROR);
            apiException.setMsg("证书验证失败");
            return apiException;
        } else if (throwable instanceof ConnectTimeoutException
                || throwable instanceof SocketTimeoutException) {
            apiException = new ApiException(throwable, ExceptionType.TIMEOUT_ERROR);
            apiException.setMsg("网络连接超时");
            return apiException;
        } else {
            apiException = new ApiException(throwable, ExceptionType.UNKNOWN);
            apiException.setMsg("未知错误");
            return apiException;
        }
    }
}
