package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;
import com.developer.paul.closabledatabindingview.closableItem.RowItem;

import com.android.databinding.library.baseAdapters.BR;
import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreateViewModel extends ItimeBaseViewModel{

    protected EventCreatePresenter presenter;
    private HashMap<String, Integer> orderHashMap = new HashMap<>();
    private List<ButtonItem> buttonItems = new ArrayList<>();
    private List<RowItem> rowItems = new ArrayList<>();
    protected Event event;
    protected EventCreateMvpView mvpView;

    protected List<String> mockAvatorLists = new ArrayList<>();

    public EventCreateViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
        mvpView = (EventCreateMvpView) presenter.getView();
        init();

        mockData();
    }

    private void init(){
        orderHashMap.put(getString(R.string.photos_toolbar_btn), 0);
        orderHashMap.put(getString(R.string.note_toolbar_btn), 1);
        orderHashMap.put(getString(R.string.url_toolbar_btn),2);
        orderHashMap.put(getString(R.string.repeat_toolbar_btn),3);
        notifyPropertyChanged(BR.orderHashMap);

        buttonItems.add(createButtonItem(getString(R.string.photos_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.note_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.url_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.repeat_toolbar_btn)));
        notifyPropertyChanged(BR.buttonItems);
    }

    private void mockData(){
        mockAvatorLists.add("http://i.imgur.com/DvpvklR.png");
        mockAvatorLists.add("http://i.imgur.com/DvpvklR.png");
        mockAvatorLists.add("http://i.imgur.com/DvpvklR.png");

    }

    @Bindable
    public List<String> getMockAvatorLists() {
        return mockAvatorLists;
    }



    public String getTitleString(Event event){
        if (event.getTitle().equals("")){
            return presenter.getContext().getString(R.string.event_title_hint);
        }
        return event.getTitle();
    }

    public int getTitleColor(Event event){
        if (event.getTitle().equals("")){
            return presenter.getContext().getResources().getColor(R.color.whiteTwo);
        }else{
            return presenter.getContext().getResources().getColor(R.color.black);
        }
    }

    public int getLocationColor(Event event){
        if (event.getLocation().equals("")){
            return presenter.getContext().getResources().getColor(R.color.whiteTwo);
        }else{
            return presenter.getContext().getResources().getColor(R.color.black);
        }
    }

    public String getLocationString(Event event){
        if (event.getLocation().equals("")){
            return getString(R.string.event_location_hint);
        }else{
            return event.getLocation();
        }
    }

    @Bindable
    public List<ButtonItem> getButtonItems() {
        return buttonItems;
    }

    public void setButtonItems(List<ButtonItem> buttonItems) {
        this.buttonItems = buttonItems;
        notifyPropertyChanged(BR.buttonItems);
    }

    @Bindable
    public List<RowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(List<RowItem> rowItems) {
        this.rowItems = rowItems;
        notifyPropertyChanged(BR.rowItems);
    }

    /**
     * Every time event attributes change, this method will be called
     */
    protected void resetButtonsAndRows(){
        if (!event.getNote().equals("")){
            addNoteToRow(event.getNote());
            removeItem(buttonItems,getString(R.string.note_toolbar_btn));
        }else{
            addButton(getString(R.string.note_toolbar_btn));
            removeItem(rowItems,getString(R.string.note_toolbar_btn));
        }

        if (!event.getUrl().equals("")){
            addUrlToRow(event.getUrl());
            removeItem(buttonItems,getString(R.string.url_toolbar_btn));
        }else{
            addButton(getString(R.string.url_toolbar_btn));
            removeItem(rowItems, getString(R.string.url_toolbar_btn));
        }


//        notifyPropertyChanged(BR.rowItems);
        notifyPropertyChanged(BR.buttonItems);
    }

    /**
     * Use this method to remove Item from items.
     * @param items
     * @param name
     */
    private void removeItem(List<? extends ClosableItem> items, String name){
        ClosableItem closableItem = null;
        for (ClosableItem item: items){
            if (item.getItemName().equals(name)){
                closableItem = item;
                break;
            }
        }
        if (closableItem==null){
            return;
        }
        items.remove(closableItem);
    }



    @Bindable
    public HashMap<String, Integer> getOrderHashMap() {
        return orderHashMap;
    }

    public void setOrderHashMap(HashMap<String, Integer> orderHashMap) {
        this.orderHashMap = orderHashMap;
        notifyPropertyChanged(BR.orderHashMap);
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        resetButtonsAndRows();
        notifyPropertyChanged(BR.event);
    }

    /**
     *
     * @return
     */
    public View.OnClickListener onClickInvitees(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }


    public View.OnClickListener onClickDuration(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toDuration(event);
                }
            }
        };
    }

    public View.OnClickListener onClickTitle(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toTitle(event);
                }
            }
        };
    }


    public View.OnClickListener onClickTimeslot(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toTimeslot(event);
                }
            }
        };
    }

    public View.OnClickListener onClickLocation(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toLocation(event);
                }
            }
        };
    }

    public View.OnClickListener onClickCalendars(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toCalendars(event);
                }
            }
        };
    }


    public View.OnClickListener onClickRepeat(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    mvpView.toRepeat(event);
                }
            }
        };
    }



    /*  update buttons */

    /**
     * Use this method to add Button
     * @param name
     */
    private void addButton(String name){
        if (isContainBtn(name)){
            return;
        }
        buttonItems.add(createButtonItem(name));
        notifyPropertyChanged(BR.buttonItems);
    }


    /*  update rows */




    /**
     * This method update row text
     */
    private void updateRow(String itemName, String text){
        RowItem item = findRowItem(itemName);
        if (item!=null){
            item.setText(text);
            notifyPropertyChanged(BR.rowItems);
        }
    }

    private void addRepeatToRow(String text){
        if (containRow(getString(R.string.repeat_toolbar_btn))){
            updateRow(getString(R.string.repeat_toolbar_btn), text);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRepeat().onClick(v);

            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.repeat_toolbar_btn));
            }
        };

        addInList(getString(R.string.repeat_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_repeat),
                getString(R.string.repeat_toolbar_btn), onClickListener, onDeleteListener);
        notifyPropertyChanged(BR.rowItems);
    }

    /**
     * when click note button, add note to row,
     * when click this view, goto note page
     * when click close icon, reset event note to ""
     * @param text
     */
    private void addNoteToRow(String text){
        if (containRow(getString(R.string.note_toolbar_btn))){
            updateRow(getString(R.string.note_toolbar_btn), text);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toNote(event);
                }
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.note_toolbar_btn));
                event.setNote("");
                setEvent(event);
            }
        };
        addInList(getString(R.string.note_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_note),
                text, onClickListener, onDeleteListener);
//        notifyPropertyChanged(BR.rowItems);
    }


    private void addUrlToRow(String text){
        if (containRow(getString(R.string.url_toolbar_btn))){
            updateRow(getString(R.string.url_toolbar_btn), text);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toUrl(event);
                }
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.url_toolbar_btn));
                event.setUrl("");
                setEvent(event);
            }
        };

        addInList(getString(R.string.url_toolbar_btn), presenter.getContext().getResources().getDrawable(R.drawable.icon_event_url), text, onClickListener, onDeleteListener);
//        notifyPropertyChanged(BR.rowItems);
    }

    private String[] getMessages(){
        return new String[]{getString(R.string.action_sheet_take_photo), getString(R.string.action_sheet_choose_from_photos)};
    }

    private void photoActionSheetPopup(){
        new AlertView.Builder().setContext(presenter.getContext())
                .setStyle(AlertView.Style.ActionSheet)
                .setCancelText(getString(R.string.action_sheet_cancel))
                .setOthers(getMessages())
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Toast.makeText(presenter.getContext(), position + "", Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .setCancelable(true)
                .show();
    }

    private void addPhotoToRow(){
        if (containRow(getString(R.string.photos_toolbar_btn))){
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoActionSheetPopup();
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.photos_toolbar_btn));
            }
        };
        addInList(getString(R.string.photos_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_photo),
                getString(R.string.photos_toolbar_btn), onClickListener, onDeleteListener);
        notifyPropertyChanged(BR.rowItems);

    }


    protected String getString(int stringId){
        return presenter.getContext().getString(stringId);
    }
//
//    /**
//     * update row to rowItems
//     * @param rowName
//     * @param icon
//     * @param text
//     * @param onClickListener
//     * @param onDeleteListener
//     */
    private void addInList(String rowName, Drawable icon, String text,
                           View.OnClickListener onClickListener, View.OnClickListener onDeleteListener){
        RowItem rowItem = new RowItem();
        rowItem.setItemName(rowName);
        rowItem.setIcon(icon);
        rowItem.setText(text);
        rowItem.setClickListener(onClickListener);
        rowItem.setOnDeleteClickListener(onDeleteListener);
        rowItems.add(rowItem);
        notifyPropertyChanged(BR.rowItems);
    }

    private boolean containRow(String rowName){
        return isIn(rowName, rowItems);
    }

    private boolean isContainBtn(String buttonName){
        return isIn(buttonName, buttonItems);
    }

    private boolean isIn(String name, List<? extends ClosableItem> items){
        for (ClosableItem item: items){
            if (item.getItemName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private RowItem findRowItem(String name){
        return (RowItem) findClosableItem(name, rowItems);
    }

    private ButtonItem findButtomItem(String name){
        return (ButtonItem) findClosableItem(name, buttonItems);
    }

    private ClosableItem findClosableItem(String name, List<? extends ClosableItem> items){
        for (ClosableItem item: items){
            if (item.getItemName().equals(name)){
                return item;
            }
        }
        return null;
    }


    /**
     * Use this function to create buttons
     * @param name
     * @return
     */
    protected ButtonItem createButtonItem(String name){
        if (name.equals(getString(R.string.photos_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_photo), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoActionSheetPopup();
                }
            });
        }

        if (name.equals(getString(R.string.note_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_note),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mvpView!=null){
                        mvpView.toNote(event);
                    }
                }
            });
        }

        if (name.equals(getString(R.string.url_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_url),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mvpView!=null){
                        mvpView.toUrl(event);
                    }
                }
            });
        }

        if (name.equals(getString(R.string.repeat_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_repeat),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mvpView!=null){
                        mvpView.toRepeat(event);
                    }
                }
            });
        }
        return null;
    }

    protected ButtonItem createButtonItem(String name, Drawable drawable, View.OnClickListener onClickListener){
        ButtonItem btnItem = new ButtonItem();
        btnItem.setItemName(name);
        btnItem.setIcon(drawable);
        btnItem.setText(name);
        btnItem.setOnClickListener(onClickListener);
        return btnItem;
    }
}
