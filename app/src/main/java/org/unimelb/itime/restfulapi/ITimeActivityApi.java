package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Paul on 10/8/17.
 */

public interface ITimeActivityApi {
    @GET("message_group/list")
    Observable<HttpResult<List<MessageGroup>>> list(@Query("syncToken") String syncToken);
}
