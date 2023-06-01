package cse340.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_EXERCISE_LOG_KEY = "logKey";

    private List<ExerciseBlock> currentLog;

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
//        LocalDate dateObj = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String date = dateObj.format(formatter);

        Button currDate = findViewById(R.id.main_screen_current_date);
        currDate.setText("TODAY"); // TODO: REPLACE WITH ACTUAL CURRENT DATE
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

        findViewById(R.id.main_screen_menu_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO this should not clearAll
                        clearAll();
                    }
                }
        );

        findViewById(R.id.main_screen_left_arrow).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_current_date).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_right_arrow).setOnClickListener(v -> unimplemented());
        findViewById(R.id.main_screen_location_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO
                        unimplemented();
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
    }

    /**
     * Show toast denoting that this feature is not yet implemented
     */
    private void unimplemented() {
        Toast.makeText(this, getResources().getString(R.string.unimplemented),
                Toast.LENGTH_SHORT).show();
    }
}