package org.codegrinders.treasure_hunter_mobile.retrofit;

public interface RetroCallBack {
    void onCallFinished(String callType);
    void onCallFailed(String errorMessage);
}
