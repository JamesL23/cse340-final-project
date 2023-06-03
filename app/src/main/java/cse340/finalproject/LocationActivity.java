package cse340.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;

import java.util.List;

/**
 * Allows user to define a geofence where they can be reminded to track a workout
 *
 * (Heavily references the sensing-and-location section exercise)
 */
public class LocationActivity extends AppCompatActivity {

    // A series of field borrowed from the sensing-and-location exercise
    /** location update every 30 seconds */
    private static final int DEFAULT_UPDATE_INTERVAL = 30;

    /** only update if phone at max power */
    private static final int FAST_UPDATE_INTERVAL = 10;

    /** default zoom level **/
    private static final int DEFAULT_ZOOM_LEVEL = 16;

    /**
     * Grant permission popup asking users to grant location accesses.
     *
     * Original Source:
     * <a href="https://developer.android.com/training/location/permissions">...</a>
     * Found from sensing-and-location exercise
     */
    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        if (fineLocationGranted != null && fineLocationGranted
                                && coarseLocationGranted != null && coarseLocationGranted) {
                            // Precise and coarse location access granted
                            Intent intent = new Intent(this, LocationActivity.class);
                            startActivity(intent);
                        } else {
                            showPermissionErrorAlert();
                        }
                    }
            );

    /** Callback handles new location detected from device and displays it on screen. */
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                mCurrentLocation = locationList.get(locationList.size() - 1);
            }
        }
    };

    /** Handle Location requests */
    private FusedLocationProviderClient mFusedLocationClient;

    /** Mapbox Mapview */
    private MapView mMapView;

    /** variable for zoom level for the map */
    private double mZoomLevel;

    /** Variable for current location for the map */
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        TextView title = findViewById(R.id.back_button_top_bar_text);
        title.setText(getResources().getString(R.string.location_screen_title));

        // info icon displays popup with information on the (unimplemented) geofencing feature
        ImageView info = findViewById(R.id.info_icon);
        info.setOnClickListener(view -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
            alertBuilder.setMessage(view.getContext().getResources()
                    .getString(R.string.location_screen_description));
            alertBuilder.setPositiveButton(
                    "OK",
                    (dialog, id) -> dialog.cancel());
            alertBuilder.create().show();
        });

        ImageButton back = findViewById(R.id.exit_button);
        back.setOnClickListener(view -> finish());

        Button save = findViewById(R.id.save_button);
        save.setOnClickListener(view -> unimplemented());

        initMap();
        initLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Things that I'm not 100% sure about "cleaning up" manually but I figured I should throw
        // them in here just in case
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void initMap() {
        mMapView = findViewById(R.id.map_view);
        FloatingActionButton zoomIn = findViewById(R.id.zoomIn);
        FloatingActionButton zoomOut = findViewById(R.id.zoomOut);
        FloatingActionButton findMyLocation = findViewById(R.id.findMyLocation);
        mZoomLevel = DEFAULT_ZOOM_LEVEL;

        zoomIn.setOnClickListener(view -> {
            mZoomLevel = Math.min(mZoomLevel + 1, 20);
            // Get the mapview map and set the camera to the new zoom level
            // Build a camera options object
            CameraOptions co = new CameraOptions.Builder().zoom(mZoomLevel).build();
            // Set the camera to use the CameraOptions object
            mMapView.getMapboxMap().setCamera(co);
        });
        zoomOut.setOnClickListener(view -> {
            mZoomLevel = Math.max(mZoomLevel - 1, 0);
            // Get the mapview map and set the camera to the new zoom level
            // Build a camera options object
            CameraOptions co = new CameraOptions.Builder().zoom(mZoomLevel).build();
            // Set the camera to use the CameraOptions object
            mMapView.getMapboxMap().setCamera(co);

        });
        findMyLocation.setOnClickListener(view -> {
            if (mCurrentLocation != null) {
                // Use the location grabbed from the user's device and display it on the map
                //  when the user presses "Find My Location".
                Point p = Point.fromLngLat(mCurrentLocation.getLongitude(),
                        mCurrentLocation.getLatitude());
                // Build a camera options object to be at a particular Point
                CameraOptions co = new CameraOptions.Builder().center(p).build();
                // Set the camera to use the CameraOptions object
                mMapView.getMapboxMap().setCamera(co);

                // Set zoom level to default
                mZoomLevel = DEFAULT_ZOOM_LEVEL;
                // Get the mapview map and set the camera to the new zoom level
                // Build a camera options object
                co = new CameraOptions.Builder().zoom(mZoomLevel).build();
                // Set the camera to use the CameraOptions object
                mMapView.getMapboxMap().setCamera(co);
            } else {
                // Handle the case where user did not turn on Location.
                Toast.makeText(this, this.getResources().getString(R.string.location_required),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(DEFAULT_UPDATE_INTERVAL * 1000);
        mLocationRequest.setInterval(FAST_UPDATE_INTERVAL * 1000);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // in case the user has revoked permissions after launching the location activity
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                Looper.myLooper());
    }

    private void showPermissionErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.go_to_settings),
                (dialog, id) -> {
                    Intent settingsIntent =
                            new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    settingsIntent.setData(uri);
                    startActivity(settingsIntent);
                });

        // if user does not grant permissions
        builder.setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
            Toast.makeText(this, getString(R.string.location_reminder),
                    Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setMessage(getString(R.string.enable_location_instructions))
                .setTitle(getString(R.string.location_required));
        builder.show();
    }

    /**
     * Show toast denoting that this feature is not yet implemented
     */
    private void unimplemented() {
        Toast.makeText(this, getResources().getString(R.string.unimplemented),
                Toast.LENGTH_SHORT).show();
    }
}
