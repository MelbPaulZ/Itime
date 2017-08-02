package org.unimelb.itime.ui.fragment.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;

import java.util.Date;

import david.itimecalendar.calendar.listeners.ITimeCalendarMonthAgendaViewListener;
import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.ui.agendaview.AgendaViewBody;
import david.itimecalendar.calendar.ui.agendaview.MonthAgendaView;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendarAgenda extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface {
    private View root;
    private EventManager eventManager;
    private MonthAgendaView agendaView;
    private FragmentCalendar.OnDateChanged onDateChanged;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar_agenda, container, false);
        eventManager = EventManager.getInstance(getContext());
        initView();
        return root;
    }

    @Override
    public CalendarPresenter<CalendarMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

    }

    public FragmentCalendar.OnDateChanged getOnDateChanged() {
        return onDateChanged;
    }

    public void setOnDateChanged(FragmentCalendar.OnDateChanged onDateChanged) {
        this.onDateChanged = onDateChanged;
    }


    @Subscribe
    public void refreshEvent(MessageEvent msg){
        if (msg.task == MessageEvent.RELOAD_EVENT){
            agendaView.setDayEventMap(EventManager.getInstance(getContext()).getEventsPackage());
        }
    }

    private void initView(){
        agendaView = (MonthAgendaView) root.findViewById(R.id.agenda_view);
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        agendaView.setDayEventMap(eventManager.getEventsPackage());
        agendaView.setITimeCalendarMonthAgendaViewListener(listener);
    }

    private ITimeCalendarMonthAgendaViewListener listener = new ITimeCalendarMonthAgendaViewListener() {
        @Override
        public void onDateChanged(Date date) {
            onDateChanged.onDateChanged(date);
        }

        @Override
        public void onEventClick(ITimeEventInterface iTimeEventInterface) {
            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
            intent.putExtra(EventDetailActivity.EVENT, (Event)iTimeEventInterface);
            getActivity().startActivity(intent);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EventCreateActivity.CREATE_EVENT) {
            if (resultCode == Activity.RESULT_OK) {
                agendaView.setDayEventMap(EventManager.getInstance(getContext()).getEventsPackage());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
