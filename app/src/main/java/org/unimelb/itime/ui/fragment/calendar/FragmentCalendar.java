package org.unimelb.itime.ui.fragment.calendar;

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
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentCalendar extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface {
    private static final String TAG = "lifecycle";
    private EventManager eventManager;
    private FragmentCalendarBinding binding;

    private FragmentCalendarMonthDay monthDayFragment;
    private FragmentCalendarAgenda agendaFragment;
    private FragmentCalendarWeekDay weekFragment;

    private MainCalendarViewModel mainCalendarViewModel;

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
        mainCalendarViewModel.setOnMenuSpinnerClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainCalendarViewModel.setShowSpinnerMenu(false);
                mainCalendarViewModel.resetOtherWrappers(i);
                changeView(i);
            }
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
    public void showCalendar(ItimeBaseFragment fragment){
//
//        if (weekFragment.isVisible()){
////            // from week fragment
//            if (fragment instanceof FragmentCalendarMonthDay){
//                // to day fragment
////                CalendarManager.getInstance().setCurrentShowCalendar(Calendar.getInstance());
//            }
//
//            if (fragment instanceof FragmentCalendarAgenda){
//                // to agenda fragment
//                CalendarManager.getInstance().setCurrentShowCalendar(Calendar.getInstance());
//            }
//
//        }
////
//        if (agendaFragment.isVisible()){
//            // from agenda fragment
//            if(fragment instanceof CalendarMonthDayFragment || fragment instanceof CalendarWeekFragment){
//                // to month day fragment or week fragment
//                Calendar curCal = Calendar.getInstance();
//                Calendar showCal = CalendarManager.getInstance().getCurrentShowCalendar();
//                showCal.set(Calendar.HOUR,curCal.get(Calendar.HOUR));
//                showCal.set(Calendar.MINUTE, curCal.get(Calendar.MINUTE));
//            }
//
//        }
        getFragmentManager().beginTransaction().replace(R.id.calendar_framelayout, fragment).commit();
    }

    public void initCalendars(){
        monthDayFragment = new FragmentCalendarMonthDay();
        weekFragment = new FragmentCalendarWeekDay();
        agendaFragment = new FragmentCalendarAgenda();
        getFragmentManager().beginTransaction().add(R.id.calendar_framelayout, monthDayFragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: " + "FragmentCalendar");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: " + "FragmentCalendar");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: " + "FragmentCalendar");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onPause: " + "FragmentCalendar");
    }
}
