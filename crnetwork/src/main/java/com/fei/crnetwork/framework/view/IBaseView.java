package com.fei.crnetwork.framework.view;


import android.support.v4.app.FragmentManager;

import com.fei.crnetwork.response.RequestException;

/**
 * Created by PengFeifei on 17-6-13.
 */

public interface IBaseView {

    boolean onRequestStart();

    boolean onRequestCacell();

    boolean onRequestEnd();

    boolean onRequestError(RequestException exception);

    boolean onPageVisible();

    FragmentManager onRequestIng();

}
