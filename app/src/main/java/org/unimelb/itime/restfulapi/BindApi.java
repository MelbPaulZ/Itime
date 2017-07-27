package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Account;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Qiushuo Huang on 2017/1/6.
 */

public interface BindApi {
    @POST("account/google/bind")
    Observable<HttpResult<List<Account>>> bindGoogle(@Query("authcode") String authCode, @Query("syncToken") String syncToken);

    @POST("account/google/unbind/{accountUid}")
    Observable<HttpResult<List<Account>>> unbindGoogle(@Path("accountUid") String accountUid, @Query("syncToken") String syncToken);

    @POST("account/university/bind/{source}")
    Observable<HttpResult<List<Account>>> bindUni(@Path("source") String source, @Body Map<String, Object> params, @Query("syncToken") String syncToken);

    @POST("account/university/unbind/{accountUid}")
    Observable<HttpResult<List<Account>>> unbindUni(@Path("accountUid") String accountUid, @Query("syncToken") String syncToken);

    @GET("account/list")
    Observable<HttpResult<List<Account>>> getAccountList(@Query("syncToken") String syncToken);

}
