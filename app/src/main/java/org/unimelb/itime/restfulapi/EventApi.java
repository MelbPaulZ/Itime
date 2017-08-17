package org.unimelb.itime.restfulapi;

import android.databinding.ObservableList;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Paul on 23/09/2016.
 */
public interface EventApi {
    public static final int OPERATION_TRUE = 1;
    public static final int OPERATION_FALSE = 2;
    String REASON = "reason";

    @GET("event/list/{calendarUid}")
    Observable<HttpResult<List<Event>>> list(
            @Path("calendarUid") String calendarUid,
            @Query("syncToken") String syncToken);

    @GET("event/get/{calendarUid}/{eventUid}")
    Observable<HttpResult<Event>> get(@Path("calendarUid") String calendarUid, @Path("eventUid") String eventUid);

    @POST("event/insert")
    Observable<HttpResult<List<Event>>> insert(
            @Body Event event,
            @Query("syncToken") String syncToken);

    @POST("event/update/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> update(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Body Event event,
            @Query("originalStartTime") String originStartTime,
            @Query("syncToken") String syncToken);

//    @POST("event/update/{calendarUid}/{eventUid}")
//Observable<HttpResult<List<Event>>> update(
//        @Path("calendarUid") String calendarUid,
//        @Path("eventUid") String eventUid,
//        @Body Event event,
//        @Query("type") String type,
//        @Query("originalStartTime") long originalStartTime,
//        @Query("syncToken") String syncToken);


    @POST("event/delete/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> delete(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Query("type") String type,
            @Query("originalStartTime") long originalStartTime,
            @Query("syncToken") String syncToken);


    @POST("event/confirm/{calendarUid}/{eventUid}/{timeslotUid}")
    Observable<HttpResult<List<Event>>> confirm(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Path("timeslotUid") String timeslotUid,
            @Query("syncToken") String syncToken);

    // after event has been confirmed, use accept event
    @POST("event/invitee/accept/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> acceptEvent(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Query("type") String type,
            @Query("originalStartTime") long originalStartTime,
            @Query("syncToken") String syncToken);


    @POST("event/invitee/quit/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> quitEvent(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Query("type") String type,
            @Query("originalStartTime") long originalStartTime,
            @Query("syncToken") String syncToken);

    @POST("event/timeslot/accept/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> acceptTimeslot(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Body HashMap<String, Object> parameters,
            @Query("syncToken") String syncToken);

    @POST("event/timeslot/reject/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> rejectTimeslot(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Body Map<String, Object> parameters,
            @Query("syncToken") String syncToken);

    @POST("event/timeslot/recommend")
    Observable<HttpResult<List<TimeSlot>>> recommend(
            @Body HashMap<String, Object> params
    );

    // waiting for testing
    @POST("event/reminder/update/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> reminderUpdate(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Body HashMap<String, Object> body,
            @Query("syncToken") String syncToken);

    @POST("event/mute/{calendarUid}/{eventUid}/{operation}")//operation: 1 for mute, 0 for unmute
    Observable<HttpResult<List<Event>>> mute(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Path("operation") int operation,
            @Query("syncToken") String syncToken);

    @POST("event/pin/{calendarUid}/{eventUid}/{operation}")//operation: 1 for pin, 0 for unpin
    Observable<HttpResult<List<Event>>> pin(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Path("operation") int operation,
            @Query("syncToken") String syncToken);

    @POST("event/archive/{calendarUid}/{eventUid}/{operation}")//Operatoin: 1 for archive, 0 for un-archive
    Observable<HttpResult<List<Event>>> archive(
            @Path("calendarUid") String calendarUid,
            @Path("eventUid") String eventUid,
            @Path("operation") int operation,
            @Query("syncToken") String syncToken);

//    @POST("event/update_cover_photo/{calendarUid}/{eventUid}")//body parameter(x-xxx-form-urlencode):
//    ObservableList<HttpResult<List<Event>> archive(
//            @Path("calendarUid") String calendarUid,
//            @Path("eventUid") String eventUid,
//            @Path("operation") int operation,
//            @Query("syncToken") String syncToken);
}
