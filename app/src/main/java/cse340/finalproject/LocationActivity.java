package cse340.finalproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.maps.MapView;

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
                            (dialog, id) -> {
                                dialog.cancel();
                                Toast.makeText(view.getContext(), R.string.unimplemented,
                                        Toast.LENGTH_SHORT).show();
                            });
                    alertBuilder.create().show();
                });

        MapView mapView = findViewById(R.id.map_view);


    }
}
