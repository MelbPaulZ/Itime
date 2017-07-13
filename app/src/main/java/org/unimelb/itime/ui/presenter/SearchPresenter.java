package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchPresenter<V extends SearchMvpView> extends ItimeBasePresenter<V> {
    private Context context;

    public SearchPresenter(Context context) {
        super(context);
        this.context = context;
    }

    public  <T> void loadData(final Class<T> tClass){
        Observable<List<T>> observable = Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                subscriber.onNext(DBManager.getInstance(context).getAll(tClass));
            }
        });

        Subscriber<List<T>> subscriber = new Subscriber<List<T>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<T> meetings) {
                if (getView() != null){
                    getView().onDataLoaded(tClass, meetings);
                }
            }
        };

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }
}
