package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;
import com.developer.paul.closabledatabindingview.closableItem.RowItem;

import com.android.databinding.library.baseAdapters.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;

import java.util.HashMap;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreateViewModel extends ItimeBaseViewModel{

    private EventCreatePresenter presenter;
    private HashMap<String, Integer> orderHashMap = new HashMap<>();
    private ObservableList<ButtonItem> buttonItems = new ObservableArrayList<>();
    private ObservableList<RowItem> rowItems = new ObservableArrayList<>();

    public EventCreateViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
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

            }
        };
    }

    public View.OnClickListener onClickUrl(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    public View.OnClickListener onClickRepeat(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }



    /*  add buttons */
    public View.OnClickListener addPhotoButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContainBtn(getString(R.string.photos_toolbar_btn))){
                    return;
                }
                buttonItems.add(createButtonItem(getString(R.string.photos_toolbar_btn)));
                notifyPropertyChanged(BR.buttonItems);
            }
        };
    }

    public View.OnClickListener addNoteButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContainBtn(getString(R.string.note_toolbar_btn))){
                    return;
                }
                buttonItems.add(createButtonItem(getString(R.string.note_toolbar_btn)));
                notifyPropertyChanged(BR.buttonItems);
            }
        };
    }

    public View.OnClickListener addUrlButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContainBtn(getString(R.string.url_toolbar_btn))){
                    return;
                }
                buttonItems.add(createButtonItem(getString(R.string.url_toolbar_btn)));
                notifyPropertyChanged(BR.buttonItems);
            }
        };
    }

    public View.OnClickListener addRepeatButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContainBtn(getString(R.string.repeat_toolbar_btn))){
                    return;
                }
                buttonItems.add(createButtonItem(getString(R.string.repeat_toolbar_btn)));
                notifyPropertyChanged(BR.buttonItems);
            }
        };
    }



    /*  add rows */
    public View.OnClickListener addRepeatRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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

                addInList(getString(R.string.repeat_toolbar_btn), v.getResources().getDrawable(R.drawable.icon_event_repeat), getString(R.string.repeat_toolbar_btn), onClickListener, onDeleteListener);

            }
        };
    }

    public View.OnClickListener addNoteRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (containRow(getString(R.string.note_toolbar_btn))){
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

                addInList(getString(R.string.note_toolbar_btn), v.getResources().getDrawable(R.drawable.icon_event_toolbar_note), getString(R.string.note_toolbar_btn), onClickListener, onDeleteListener);

            }
        };
    }

    public View.OnClickListener addUrlRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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

                addInList(getString(R.string.url_toolbar_btn), v.getResources().getDrawable(R.drawable.icon_event_toolbar_url), getString(R.string.url_toolbar_btn), onClickListener, onDeleteListener);

            }
        };
    }

    public View.OnClickListener addPhotoRow(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
                addInList(getString(R.string.photos_toolbar_btn), v.getResources().getDrawable(R.drawable.icon_event_photo), getString(R.string.photos_toolbar_btn), onClickListener, onDeleteListener);

            }
        };
    }

    private String getString(int stringId){
        return presenter.getContext().getString(stringId);
    }

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
        for (RowItem rowItem: rowItems){
            if (rowItem.getItemName().equals(rowName)){
                return true;
            }
        }
        return false;
    }

    private boolean isContainBtn(String buttonName){
        for (ButtonItem buttonItem: buttonItems){
            if (buttonItem.getItemName().equals(buttonName)){
                return true;
            }
        }
        return false;
    }


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
