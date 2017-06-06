package org.unimelb.itime.manager;

import android.content.Context;

import com.google.gson.Gson;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 6/6/17.
 */

public class EventManager {

    private static EventManager instance;
    private Context context;
    private Event event;
    private EventManager(Context context) {
        this.context = context;
    }

    public static EventManager getInstance(Context context){
        if (instance == null){
            instance = new EventManager(context);
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public Event copyEvent(Event event){
        Gson gson = new Gson();
        String eventString = gson.toJson(event);
        return gson.fromJson(eventString, Event.class);
    }


}
