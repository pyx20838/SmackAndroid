package com.xmpp.smackchat.base.repository.api.common;



import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ResponseCode.SUCCESS, ResponseCode.LOADING, ResponseCode.PAGING_EMPTY, ResponseCode.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface ResponseCode {
    int SUCCESS = 0;
    int LOADING = 1;
    int PAGING_EMPTY = 2;

    int ERROR = -999;
}
