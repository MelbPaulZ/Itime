package org.unimelb.itime.ui.mvpview;

/**
 * Created by Paul on 15/6/17.
 */

public interface ItimeTaskInterface {
    void onTaskError(int task);
    void onTaskSuccess(int task);
}
