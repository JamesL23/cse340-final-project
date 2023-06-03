package cse340.finalproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mapbox.maps.MapView;

/**
 * Allows user to define a geofence where they can be reminded to track a workout
 *
 * (Heavily references the sensing-and-location section exercise)
 */
public class LocationActivity extends AppCompatActivity {

    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        TextView title = findViewById(R.id.back_button_top_bar_text);
        title.setText(getResources().getString(R.string.location_screen_title));

        // TODO ask user for location permission


        if (!(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                !(ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage(getResources()
                    .getString(R.string.location_screen_description));
            alertBuilder.setPositiveButton(
                    "OK",
                    (dialog, id) -> {
                        dialog.cancel();
                        Toast.makeText(this, R.string.unimplemented,
                                Toast.LENGTH_SHORT).show();
                    });
            alertBuilder.create().show();
            finish();
            // if location permissions not granted, return to starting screen
        } else {
            System.out.println("Location permissions granted!");
        }

        // info icon displays popup with information on the (unimplemented) geofencing feature
        ImageView info = findViewById(R.id.info_icon);
        info.setOnClickListener(view -> {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                    alertBuilder.setMessage(view.getContext().getResources()
                            .getString(R.string.location_screen_description));
                    alertBuilder.setPositiveButton(
                            "OK",
                            (dialog, id) -> {
                                dialog.cancel();
                            });
                    alertBuilder.create().show();
                });

        MapView mapView = findViewById(R.id.map_view);


    }
}
