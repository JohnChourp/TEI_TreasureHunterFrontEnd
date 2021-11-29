package org.codegrinders.treasure_hunter_mobile.retrofit;

import org.codegrinders.treasure_hunter_mobile.model.Markers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkersCall {

    private List<Markers> markers;
    private RetroCallBack callBack;
    private Call<List<Markers>> call;

    public Markers searchMarkerByTitle(String title) {
        Markers marker = null;
        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).getTitle().equals(title)) {
                marker = markers.get(i);
                break;
            }
        }
        return marker;
    }

    public void markersGetRequest() {
        call = RetroInstance.initializeAPIService().getMarkers();
        call.enqueue(new Callback<List<Markers>>() {
            @Override
            public void onResponse(@NotNull Call<List<Markers>> call, @NotNull Response<List<Markers>> response) {
                if (!response.isSuccessful()) {
                    callBack.onCallFailed("code: " + response.code());
                    return;
                }

                markers = response.body();
                callBack.onCallFinished("Markers");
            }

            @Override
            public void onFailure(@NotNull Call<List<Markers>> call, @NotNull Throwable t) {
                callBack.onCallFailed(t.getMessage());
            }
        });
    }

    public List<Markers> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Markers> markers) {
        this.markers = markers;
    }

    public Call<List<Markers>> getCall() {
        return call;
    }

    public void setCall(Call<List<Markers>> call) {
        this.call = call;
    }

    public RetroCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(RetroCallBack callBack) {
        this.callBack = callBack;
    }

}
