package org.codegrinders.treasure_hunter_mobile.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
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
import org.codegrinders.treasure_hunter_mobile.menu.ActivityUserMenu;
import org.codegrinders.treasure_hunter_mobile.model.Markers;
import org.codegrinders.treasure_hunter_mobile.model.User;
import org.codegrinders.treasure_hunter_mobile.retrofit.MarkersCall;
import org.codegrinders.treasure_hunter_mobile.retrofit.PuzzlesCall;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;
import org.codegrinders.treasure_hunter_mobile.settings.MediaService;
import org.codegrinders.treasure_hunter_mobile.settings.Sound;

import java.util.ArrayList;
import java.util.List;

public class ActivityMap extends AppCompatActivity implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnInfoWindowClickListener {
    GoogleMap mMap;

    Button bt_pause;
    TextView tv_username;
    TextView tv_points;

    List<Marker> markerList = new ArrayList<>();
    MarkersCall markersCall = new MarkersCall();
    PuzzlesCall puzzlesCall = new PuzzlesCall();
    UsersCall usersCall = new UsersCall();

    public static Markers currentMarkerData = null;
    public static Marker currentMarker = null;

    MediaService audioService;
    boolean isBound = false;

    boolean firstTime = true;
    boolean started = false;
    Handler handler = new Handler();
    Runnable runnable = () -> {
        usersCall.oneUserGetRequest(usersCall.getUser().getId());
        if (started) {
            start();
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

        bt_pause = findViewById(R.id.bt_pause);
        bt_pause.setOnClickListener(v -> {
            audioService.play(Sound.buttonSound, 0);
            openActivityUserMenu();
        });
        tv_points = findViewById(R.id.tv_points);
        tv_username = findViewById(R.id.tv_username);

        usersCall.setUser((User) getIntent().getSerializableExtra("User"));
        tv_username.setText(usersCall.getUser().getUsername());
        tv_points.setText("Score: " + usersCall.getUser().getPoints());
    }

    @SuppressLint("PotentialBehaviorOverride")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        RetroCallBack retroCallBack = new RetroCallBack() {
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
                    usersCall.getUser().setPoints(usersCall.getUser().getPoints());
                    usersCall.getUser().setHasWon(usersCall.getUser().isHasWon());
                    tv_points.setText("Score: " + usersCall.getUser().getPoints());
                    if (usersCall.getUser().isHasWon() && firstTime) {
                        firstTime = false;
                        audioService.play(Sound.buttonSound, 0);
                        openActivityResults();
                    }
                }
                proximityMarkers();
            }

            @Override
            public void onCallFailed(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        };
        showLocationPermission();
        markersCall.setCallBack(retroCallBack);
        puzzlesCall.setCallBack(retroCallBack);
        usersCall.setCallBack(retroCallBack);
        puzzlesCall.puzzlesGetRequest();
        markersCall.markersGetRequest();
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
                        && markersCall.getMarkers().get(i).isVisibility());
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
        currentMarkerData = markersCall.searchMarkerByTitle(marker.getTitle());
        currentMarker = marker;
        puzzlesCall.searchPuzzleByID(currentMarkerData.getPuzzleId());
        openActivityPuzzles();
    }

    private void showLocationPermission() {
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
        startActivity(intent);
    }

    private void openActivityResults() {
        Intent intent = new Intent(this, ActivityResults.class);
        startActivity(intent);
    }

    private void openActivityUserMenu() {
        Intent intent = new Intent(this, ActivityUserMenu.class);
        startActivity(intent);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            audioService = binder.getService();
            isBound = true;
            audioService.stop(Sound.menuMusic);
            audioService.play(Sound.gameMusic, Sound.get(Sound.gameMusic).position);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MediaService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        started = false;
        handler.removeCallbacks(runnable);
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 2000);
    }

}