package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Paul on 23/09/2016.
 */
public interface MeetingApi {
    @GET("meeting/list")
    Observable<HttpResult<List<Meeting>>> list(
            @Query("syncToken") String syncToken);

    @GET("meeting/get/{meetingUid}")
    Observable<HttpResult<Meeting>> get( @Path("meetingUid") String meetingUid);

}
