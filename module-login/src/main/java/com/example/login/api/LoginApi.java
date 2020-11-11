package com.example.login.api;


import com.example.lib_common.linkage.multi_link.thr_link.SchoolData;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.login.api.requestbody.LoginByPWDRequestBody;
import com.example.login.api.requestbody.LoginBySMSRequestBody;
import com.example.login.api.requestbody.UpdatePWDRequestBody;
import com.example.login.api.requestbody.UpdatePhoneRequestBody;
import com.example.login.new_part.QuestionBean;
import com.example.network.response.ApiResponse;

import java.util.List;
import java.util.stream.LongStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 18:06.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public interface LoginApi {

    @GET("users/smsSend")
    Observable<ApiResponse<String>> sendVerifyCode(@Query("phone") String phone);


    @POST("users/loginBySms")
    Observable<ApiResponse<LoginResponseBean>> loginBySMS(
            @Query("schoolId") int schoolId,
            @Query("phone") String phone,
            @Query("smsCode") String smsCode);

    @POST("users/loginByPwd")
    Observable<ApiResponse<LoginResponseBean>> loginByPWD(
            @Query("phone") String phone,
            @Query("pwd") String pwd
    );

    @POST("users/loginBySms")
    Observable<ApiResponse<LoginResponseBean>> loginBySms(@Body LoginBySMSRequestBody body);


    @POST("users/loginByPwd")
    Observable<ApiResponse<LoginResponseBean>> loginByPwd(@Body LoginByPWDRequestBody body);

    @PUT("users/updatePassword")
    Observable<ApiResponse<String>> updatePwd(@Body UpdatePWDRequestBody body);

    @PUT("users/updatePhone")
    Observable<ApiResponse<String>> updatePhone(@Body UpdatePhoneRequestBody body);

    @GET("questionBanks/listBySchoolId")
    Observable<ApiResponse<List<QuestionBean>>> getQuestions(
            @Query(value = "schoolId") String schoolId,
            @Query(value = "page") String page,
            @Query(value = "size") String size
    );

    @GET("schools/listAll")
    Observable<ApiResponse<List<SchoolData>>> getSchools();

}

