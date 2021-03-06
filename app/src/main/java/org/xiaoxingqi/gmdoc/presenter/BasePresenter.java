package org.xiaoxingqi.gmdoc.presenter;

import android.content.Context;

import org.xiaoxingqi.gmdoc.core.http.HttpServer;
import org.xiaoxingqi.gmdoc.impl.ApiServer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 控制网络请求, 控制视图的切换展示
 */
public abstract class BasePresenter {
    protected ApiServer apiServer;
    private CompositeSubscription composite = new CompositeSubscription();

    public BasePresenter(Context context) {
        apiServer = HttpServer.getInstance(context).getApiServer();
    }

    protected void addObserve(Observable observable, Subscriber subscriber) {
        composite.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }

    public void onDetach() {
        if (composite != null && composite.hasSubscriptions()) {
            composite.unsubscribe();
        }
    }
}
