package org.codegrinders.treasure_hunter_mobile.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.model.User;
import org.codegrinders.treasure_hunter_mobile.retrofit.MarkersCall;
import org.codegrinders.treasure_hunter_mobile.retrofit.PuzzlesCall;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityMap extends AppCompatActivity implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private final List<Marker> markerList = new ArrayList<>();
    private final MarkersCall markersCall = new MarkersCall();

    Button bt_leaderBoard;
    TextView tv_username;
    TextView tv_points;
    boolean isActivityOpen = false;
    PuzzlesCall puzzlesCall = new PuzzlesCall();
    public static UsersCall usersCall = new UsersCall();
    RetroCallBack retroCallBack;
    User user;

    private Timer timer;
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            markersCall.markersGetRequest();
            usersCall.oneUserGetRequest(user.getId());
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        bt_leaderBoard = findViewById(R.id.bt_leaderBoard);
        bt_leaderBoard.setOnClickListener(v -> openActivityLeaderBoard());
        tv_points = findViewById(R.id.tv_points);
        tv_username = findViewById(R.id.tv_username);
        user = (User) getIntent().getSerializableExtra("User");
        tv_username.setText(user.getUsername());
        tv_points.setText("Score: " + user.getPoints());
    }

    @SuppressLint("PotentialBehaviorOverride")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        retroCallBack = new RetroCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCallFinished(String callType) {
                if (callType.equals("Markers")) {
                    for (int i = 0; i < markersCall.getMarkers().size(); i++) {
                        markerList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(markersCall.getMarkers().get(i).getLatitude(),
                                markersCall.getMarkers().get(i).getLongitude())).title(markersCall.getMarkers().get(i).getTitle()).snippet(markersCall.getMarkers().get(i).getSnippet()).visible(false)));
                    }
                }
                if (callType.equals("OneUser")) {
                    user.setPoints(usersCall.user.getPoints());
                    user.setHasWon(usersCall.user.isHasWon());
                    tv_points.setText("Score: " + user.getPoints());
                }
                proximityMarkers();
            }

            @Override
            public void onCallFailed(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        };
        showPhoneStatePermission();
        markersCall.setCallBack(retroCallBack);
        puzzlesCall.setCallBack(retroCallBack);
        usersCall.setCallBack(retroCallBack);
        puzzlesCall.puzzlesGetRequest();
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.07529, 23.55330), 17));
    }

    private void proximityMarkers() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double[] longitude = {location.getLongitude()};
        final double[] latitude = {location.getLatitude()};

        final LocationListener locationListener = location1 -> {
            longitude[0] = location1.getLongitude();
            latitude[0] = location1.getLatitude();
            for (int i = 0; i < markersCall.getMarkers().size(); i++) {
                markerList.get(i).setVisible(SphericalUtil
                        .computeDistanceBetween(new LatLng(location1.getLatitude(), location1.getLongitude()), markerList.get(i).getPosition()) < 50
                        && markersCall.getMarkers().get(i).getVisibility());
            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "I am Here", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        puzzlesCall.searchPuzzleByID(markersCall.searchMarkerByTitle(marker.getTitle()).getPuzzleId());
        openActivityPuzzles();
    }

    private void showPhoneStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation();
            } else {
                requestPermission();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Needed")
                .setMessage("Rationale")
                .setPositiveButton(android.R.string.ok, (dialog, id) -> requestPermission());
        builder.create().show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private void openActivityPuzzles() {
        Intent intent = new Intent(this, ActivityPuzzle.class);
        isActivityOpen = true;
        startActivity(intent);
    }

    private void openActivityLeaderBoard() {
        Intent intent = new Intent(this, ActivityLeaderBoard.class);
        isActivityOpen = true;
        startActivity(intent);
    }

    private void openActivityWon() {
        Intent intent = new Intent(this, ActivityWon.class);
        isActivityOpen = true;
        startActivity(intent);
    }

    private void openActivityLost() {
        Intent intent = new Intent(this, ActivityLost.class);
        isActivityOpen = true;
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isActivityOpen) {
            timer.cancel();
            timer = null;
        } else {
            isActivityOpen = false;
        }
    }
}