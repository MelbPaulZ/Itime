package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;
import com.developer.paul.closabledatabindingview.closableItem.RowItem;

import com.android.databinding.library.baseAdapters.BR;
import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreateViewModel extends ItimeBaseViewModel{

    private EventCreatePresenter presenter;
    private HashMap<String, Integer> orderHashMap = new HashMap<>();
    private ObservableList<ButtonItem> buttonItems = new ObservableArrayList<>();
    private ObservableList<RowItem> rowItems = new ObservableArrayList<>();
    private Event event;
    private EventCreateMvpView mvpView;

    public EventCreateViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
        mvpView = (EventCreateMvpView) presenter.getView();
        init();
    }

    private void init(){
        orderHashMap.put(getString(R.string.photos_toolbar_btn), 0);
        orderHashMap.put(getString(R.string.note_toolbar_btn), 1);
        orderHashMap.put(getString(R.string.url_toolbar_btn),2);
        orderHashMap.put(getString(R.string.repeat_toolbar_btn),3);


        buttonItems.add(createButtonItem(getString(R.string.photos_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.note_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.url_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.repeat_toolbar_btn)));

        setButtonItems(buttonItems);
    }

    /**
     * Every time event attributes change, this method will be called
     */
    private void resetButtonsAndRows(){
        if (!event.getNote().equals("")){
            addNoteToRow(event.getNote());
            removeItem(buttonItems,getString(R.string.note_toolbar_btn));
        }else{
            addButton(getString(R.string.note_toolbar_btn));
            removeItem(rowItems,getString(R.string.note_toolbar_btn));
        }

        if (!event.getUrl().equals("")){
            addUrlToRow();
            removeItem(buttonItems,getString(R.string.url_toolbar_btn));
        }
    }

    private void removeItem(ObservableList<? extends ClosableItem> items, String name){
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
    public ObservableList<ButtonItem> getButtonItems() {
        return buttonItems;
    }

    public void setButtonItems(ObservableList<ButtonItem> buttonItems) {
        this.buttonItems = buttonItems;
        notifyPropertyChanged(BR.buttonItems);
    }

    @Bindable
    public ObservableList<RowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(ObservableList<RowItem> rowItems) {
        this.rowItems = rowItems;
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

            }
        };
    }


    public View.OnClickListener onClickTimeslot(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    public View.OnClickListener onClickLocation(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    public View.OnClickListener onClickCalendars(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    public View.OnClickListener onClickPhoto(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }


    public View.OnClickListener onClickNote(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    mvpView.toNote(event);
                }
            }
        };
    }

    public View.OnClickListener onClickUrl(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    mvpView.toUrl(event);
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

    private void addButton(String name){
        if (isContainBtn(name)){
            return;
        }
        buttonItems.add(createButtonItem(name));
        notifyPropertyChanged(BR.buttonItems);
    }

    public View.OnClickListener addPhotoButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.photos_toolbar_btn));
            }
        };
    }



    public View.OnClickListener addNoteButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.note_toolbar_btn));
            }
        };
    }

    public View.OnClickListener addUrlButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.url_toolbar_btn));
            }
        };
    }

    public View.OnClickListener addRepeatButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.repeat_toolbar_btn));
            }
        };
    }



    /*  update rows */

    private void addRepeatToRow(){
        if (containRow(getString(R.string.repeat_toolbar_btn))){
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
                addRepeatButton().onClick(v);
            }
        };

        addInList(getString(R.string.repeat_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_repeat),
                getString(R.string.repeat_toolbar_btn), onClickListener, onDeleteListener);
    }


    public View.OnClickListener addRepeatRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               addRepeatToRow();
            }
        };
    }

    private void addNoteToRow(){
        addNoteToRow(getString(R.string.note_toolbar_btn));
    }

    private void addNoteToRow(String text){
        if (containRow(getString(R.string.note_toolbar_btn))){
            RowItem item = findRowItem(getString(R.string.note_toolbar_btn));
            if (item!=null){
                item.setText(text);
                notifyPropertyChanged(BR.rowItems);
            }
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNote().onClick(v);
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteButton().onClick(v);
            }
        };
        addInList(getString(R.string.note_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_note),
                text, onClickListener, onDeleteListener);
    }



    public View.OnClickListener addNoteRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    mvpView.toNote(event);
                }
                addNoteToRow();
            }
        };
    }

    private void addUrlToRow(){
        if (containRow(getString(R.string.url_toolbar_btn))){
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUrl().onClick(v);

            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUrlButton().onClick(v);
            }
        };

        addInList(getString(R.string.url_toolbar_btn), presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_url), getString(R.string.url_toolbar_btn), onClickListener, onDeleteListener);
    }

    public View.OnClickListener addUrlRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addUrlToRow();
            }
        };
    }

    private void addPhotoToRow(){
        if (containRow(getString(R.string.photos_toolbar_btn))){
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhoto().onClick(v);
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhotoButton().onClick(v);
            }
        };
        addInList(getString(R.string.photos_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_photo),
                getString(R.string.photos_toolbar_btn), onClickListener, onDeleteListener);

    }

    public View.OnClickListener addPhotoRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addPhotoToRow();
            }
        };
    }

    private String getString(int stringId){
        return presenter.getContext().getString(stringId);
    }

    /**
     * update row to rowItems
     * @param rowName
     * @param icon
     * @param text
     * @param onClickListener
     * @param onDeleteListener
     */
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
    private ButtonItem createButtonItem(String name){
        if (name.equals(getString(R.string.photos_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_photo), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPhotoRow().onClick(v);
                }
            });
        }

        if (name.equals(getString(R.string.note_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_note),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNoteRow().onClick(v);
                }
            });
        }

        if (name.equals(getString(R.string.url_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_url),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUrlRow().onClick(v);
                }
            });
        }

        if (name.equals(getString(R.string.repeat_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_repeat),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRepeatRow().onClick(v);
                }
            });
        }
        return null;
    }

    private ButtonItem createButtonItem(String name, Drawable drawable, View.OnClickListener onClickListener){
        ButtonItem btnItem = new ButtonItem();
        btnItem.setItemName(name);
        btnItem.setIcon(drawable);
        btnItem.setText(name);
        btnItem.setOnClickListener(onClickListener);
        return btnItem;
    }
}
