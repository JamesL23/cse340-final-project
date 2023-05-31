package cse340.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
                            (int) getResources().getDimension(R.dimen.divider_thickness));
            viewDivider.setLayoutParams(params);
            viewDivider.setBackgroundColor(Color.BLACK);
            cardContainer.addView(viewDivider);
        }
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
}