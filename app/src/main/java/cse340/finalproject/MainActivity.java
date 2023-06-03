package cse340.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_EXERCISE_LOG_KEY = "logKey";

    private List<ExerciseBlock> currentLog;

    /**
     * Grant permission popup asking users to grant location accesses.
     *
     * Original Source: https://developer.android.com/training/location/permissions
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
                            // Precise and coarse location access granted--go to location activity
                            Intent intent = new Intent(this, LocationActivity.class);
                            startActivity(intent);
                        } else {
                            showPermissionDeniedAlert();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        // https://stackoverflow.com/a/28107791
        String serializedExerciseLog = pref.getString(SHARED_PREFERENCES_EXERCISE_LOG_KEY, null);
        if (serializedExerciseLog != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ExerciseBlock>>(){}.getType();
            currentLog = gson.fromJson(serializedExerciseLog, type);
        } else {
            currentLog = new LinkedList<>();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null
                && extras.containsKey("name")
                && extras.containsKey("info")) {
            String name = extras.getString("name");
            String info = extras.getString("info");

            // If I had more time I would better optimize my data structure
            // usage to avoid this O(n) lookup on adding every exercise.
            // However, for this prototype demo there are only 3 hard-coded
            // options for exercises, so the currentLog should never be more than
            // 3 long anyway and this shouldn't be TOO bad (hopefully)...

            // oh man this flag thing is kind of gross but I can't think of a better
            // way to do this off the top of my head without reworking a bunch of
            // other stuff that isn't super high priority

            // The idea here is that if you add an exercise that's already in the log
            // it will just group with that instead of making an entirely new card.
            boolean found = false;
            for (ExerciseBlock e : currentLog) {
                if (e.getExercise().equals(name)) {
                    e.addSet(info);
                    found = true;
                }
            }
            if (!found) {
                List<String> newBlockList = new LinkedList<>();
                newBlockList.add(info);
                currentLog.add(new ExerciseBlock(name, newBlockList));
            }
        }

        // https://www.tutorialkart.com/java/how-to-get-current-date-in-yyyy-mm-dd-format-in-java/#gsc.tab=0
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = dateObj.format(formatter);

        Button currDate = findViewById(R.id.main_screen_current_date);
        currDate.setText(date);
        // There's a good chance the calendar stuff never gets featured and we only end up
        // saving and editing one workout session because I really am running out of time
        LinearLayout cardContainer = findViewById(R.id.exercise_cards_container);
        for (ExerciseBlock e : currentLog) {
            // add exercise card
            cardContainer.addView(new ExerciseCardView(this, e));

            // add divider
            View viewDivider = new View(this);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (int) getResources().getDimension(R.dimen.subdivider_thickness));
            viewDivider.setLayoutParams(params);
            viewDivider.setBackgroundColor(Color.BLACK);
            cardContainer.addView(viewDivider);
        }

        // Buttons
        findViewById(R.id.fab_add_exercise).setOnClickListener(
                v -> startAddExerciseIntent()
        );
        findViewById(R.id.fab_clear).setOnClickListener(
                v -> clearAll()
        );

        findViewById(R.id.main_screen_menu_button).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_left_arrow).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_current_date).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_right_arrow).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_location_button).setOnClickListener(
                v -> {
                    // Only proceed to location activity if location permissions granted
                    // otherwise, request permissions
                    if (ActivityCompat.checkSelfPermission(v.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(v.getContext(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {
                        // Granted; proceed to location activity
                        Intent intent = new Intent(v.getContext(), LocationActivity.class);
                        startActivity(intent);
                    } else {
                        // Request permissions
                        locationPermissionRequest.launch(new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        });
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        // https://stackoverflow.com/a/28107791
        SharedPreferences.Editor prefEdit = getPreferences(MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentLog);
        prefEdit.putString(SHARED_PREFERENCES_EXERCISE_LOG_KEY, json);
        prefEdit.apply();
    }

    /**
     * Switch to the Add Exercise screen
     */
    private void startAddExerciseIntent() {
        // Borrowed from AS3-Accessibility
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivity(intent);
    }

    /**
     * Delete everything in current log (including shared preferences) and update view
     */
    private void clearAll() {
        currentLog.clear();
        SharedPreferences.Editor prefEdit = getPreferences(MODE_PRIVATE).edit();
        prefEdit.clear();
        prefEdit.apply();
        LinearLayout cardContainer = findViewById(R.id.exercise_cards_container);
        cardContainer.removeAllViews();
        Toast.makeText(this, getResources().getString(R.string.clear_all_result),
                Toast.LENGTH_SHORT).show();
    }

    /**
     *  Raise an Alert if the user denied location permissions.
     *
     *  Borrowed from sensing-and-location exercise
     */
    private void showPermissionDeniedAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.go_to_settings),
                (dialog, id) -> {
                    Intent settingsIntent =
                            new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    settingsIntent.setData(uri);
                    startActivity(settingsIntent);
                });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, id) ->
                Toast.makeText(this, getString(R.string.location_reminder),
                        Toast.LENGTH_SHORT).show());
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