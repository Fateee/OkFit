package com.fei.crnetwork.framework.subscriber;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fei.crnetwork.error.ErrorCode;
import com.fei.crnetwork.framework.ObservableHandler;
import com.fei.crnetwork.framework.adapter.ExceptionAdapter;
import com.fei.crnetwork.framework.view.IBaseView;
import com.fei.crnetwork.framework.view.loading.LoadingDialog;
import com.fei.crnetwork.response.RequestException;

import rx.Subscriber;

import static com.fei.crnetwork.framework.view.loading.LoadingDialog.TOTAL_TIME;

/**
 * Created by PengFeifei on 17-6-14.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> implements LoadingDialog.OnDismissListener {

    private IBaseView baseView;
    private LoadingDialog loadingDialog;
    private boolean cancelled;

    public BaseSubscriber(@NonNull IBaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onNext(T t) {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onNext");
        if (baseView.onPageVisible() && t != null) {
            onHandleData(t);
        }
    }

    public abstract void onHandleData(T t);

    @Override
    public void onStart() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onStart");
        super.onStart();
        boolean onStart = baseView.onRequestStart();
        if (!onStart) {
            loadingDialog = LoadingDialog.showLoading(baseView.onRequestIng());
            if (loadingDialog != null) {
                loadingDialog.setOnDismissListener(this);
            }
        }
    }

    @Override
    public void onCompleted() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onCompleted");
        if (!cancelled) {
            baseView.onRequestEnd();
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            cancelled = false;
        }
        ObservableHandler.setHttpUrl(null);
    }

    @Override
    public void onError(Throwable e) {
        onError(e, true);
    }

    protected void onError(Throwable e, boolean consumedException) {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onError");
        Log.e("BaseSubscriber-->", e.getMessage());
        if (!cancelled) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            cancelled = false;
        }
        if (!consumedException) {
            baseView.onRequestError(ExceptionAdapter.toRequestException(e, ObservableHandler.getHttpUrl()));
        }
        ObservableHandler.setHttpUrl(null);
    }

    @Override
    public void onDismiss() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onDismiss");
        unsubscribe();
        ObservableHandler.setHttpUrl(null);
    }

    @Override
    public void onCancell() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onCancell");
        cancelled = true;
        baseView.onRequestCacell();
        unsubscribe();
        ObservableHandler.setHttpUrl(null);
    }

    @Override
    public void onTimeOutDismiss() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onTimeOutDismiss");
        if (!isUnsubscribed()) {
            throw new RequestException(ObservableHandler.getHttpUrl(), ErrorCode.REQUEST_TIMEOUT_ERR, "request take more than " + TOTAL_TIME + " millis");
        }
        ObservableHandler.setHttpUrl(null);
    }
}
