package org.unimelb.itime.ui.viewmodel.activity;

import org.unimelb.itime.base.ItimeBaseViewModel;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 3/7/17.
 */

public class ItimeActivitiesViewModel extends ItimeBaseViewModel {

    private List<ActivityMessageViewModel> messageGroups = new ArrayList<>();
    public final OnItemBind<ActivityMessageViewModel> onItemBind = new OnItemBind<ActivityMessageViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ActivityMessageViewModel item) {

        }
    };


    private class ActivityMessageViewModel{
        private boolean isSystem;
        private String user = "";
        private String message = "";

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(boolean system) {
            isSystem = system;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
