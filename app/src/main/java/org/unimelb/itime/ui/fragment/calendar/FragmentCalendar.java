package org.unimelb.itime.ui.fragment.calendar;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.developer.paul.itimerecycleviewgroup.ITimeRecycleViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.SpinnerWrapper;
import org.unimelb.itime.databinding.FragmentCalendarBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.ArchiveActivity;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.SearchActivity;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendar extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface{
    private static final String TAG = "lifecycle";

    public static final String SCROLL_TO_DATE = "scroll_to_date";

    private EventManager eventManager;
    private FragmentCalendarBinding binding;

    private FragmentCalendarMonthDay monthDayFragment;
    private FragmentCalendarAgenda agendaFragment;
    private FragmentCalendarWeekDay weekFragment;

    private CalendarListener currentCalendar;

    private MainCalendarViewModel mainCalendarViewModel;
    private OnToolbarClick onToolbarClick = new OnToolbarClick() {
        @Override
        public void onSearchClick() {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra(SearchActivity.TASK,SearchActivity.CALENDAR_SEARCH);
            startActivity(intent);
        }

        @Override
        public void onTodayClick() {
            if (monthDayFragment != null){
                monthDayFragment.backToToday();
            }
            if (weekFragment != null){
                weekFragment.backToToday();
            }
            if (agendaFragment != null){
                agendaFragment.backToToday();
            }
        }
    };
    private OnDateChanged onDateChanged = date -> mainCalendarViewModel.setToolbarTitle(EventUtil.getEventTitlebarDateStr(date));

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        eventManager = EventManager.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainCalendarViewModel = new MainCalendarViewModel();
        binding.setCalendarVM(mainCalendarViewModel);
        initSpinner();
        initCalendars();
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



    public void initSpinner(){
        ArrayList<SpinnerWrapper> wrappers = new ArrayList<>();
        wrappers.add(new SpinnerWrapper(getString(R.string.day),1));
        wrappers.add(new SpinnerWrapper(getString(R.string.week), 0));
        wrappers.add(new SpinnerWrapper(getString(R.string.agenda), 0));
        mainCalendarViewModel.setMenuItems(wrappers);
        mainCalendarViewModel.setOnToolbarClick(onToolbarClick);
        mainCalendarViewModel.setOnMenuSpinnerClicked((adapterView, view, i, l) -> {
            mainCalendarViewModel.setShowSpinnerMenu(false);
            mainCalendarViewModel.resetOtherWrappers(i);
            changeView(i);
        });

    }

    public void changeView(int index){
        switch (index){
            case 0:
                showCalendar(monthDayFragment);
                break;
            case 1:
                showCalendar(weekFragment);
                break;
            case 2:
                showCalendar(agendaFragment);
                break;
            default:
                showCalendar(monthDayFragment);
        }

    }

    /**
     * this method controls where to scroll to when change between different calendars
     * @param fragment
     */
    public <T extends ItimeBaseFragment & CalendarListener> void showCalendar(T fragment){
        currentCalendar = fragment;
        getFragmentManager().beginTransaction().replace(R.id.calendar_framelayout, fragment).commit();
    }

    public void initCalendars(){
        monthDayFragment = new FragmentCalendarMonthDay();
        weekFragment = new FragmentCalendarWeekDay();
        agendaFragment = new FragmentCalendarAgenda();

        monthDayFragment.setOnDateChanged(onDateChanged);
        weekFragment.setOnDateChanged(onDateChanged);
        agendaFragment.setOnDateChanged(onDateChanged);

        currentCalendar = monthDayFragment;
        getFragmentManager().beginTransaction().add(R.id.calendar_framelayout, monthDayFragment).commit();
    }

    public interface OnDateChanged{
        void onDateChanged(Date date);
    }

    public interface OnToolbarClick{
        void onSearchClick();
        void onTodayClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Date targetDate = (Date) data.getSerializableExtra(FragmentCalendar.SCROLL_TO_DATE);
            if (targetDate != null && currentCalendar != null){
                currentCalendar.scrollToDate(targetDate,true);
            }
        }
    }

    interface CalendarListener{
        void scrollToDate(Date date, boolean toTime);
    }
}
