package cse340.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

/**
 * Allows user to define a geofence where they can be reminded to track a workout
 *
 * (Heavily references the sensing-and-location section exercise)
 */
public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        MapView mapView = findViewById(R.id.map_view);

    }
}
