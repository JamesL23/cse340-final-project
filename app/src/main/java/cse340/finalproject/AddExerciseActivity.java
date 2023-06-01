package cse340.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddExerciseActivity extends AppCompatActivity {

    private static final List<String> EXERCISES = Collections.unmodifiableList(
            Arrays.asList("Back squat", "Bench press", "Deadlift"));;

    /**
     * Bundle used to transfer name of selected exercise to the log exercise screen.
     * Borrowed from AS3 Accessibility
     */
    private Bundle mExerciseInfo;

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_activity);

        TextView title = findViewById(R.id.back_button_top_bar_text);
        title.setText(getResources().getString(R.string.add_exercise_screen_title));

        mExerciseInfo = new Bundle();
        mExerciseInfo.putString("name", "none");

        LinearLayout options = findViewById(R.id.add_exercise_options);
        for (String s : EXERCISES) {
            // add exercise card
            TextView curr = new TextView(this);
            curr.setText(s);
            curr.setOnClickListener(this::startLogExerciseIntent);
            options.addView(curr);

            // add divider
            View viewDivider = new View(this);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (int) getResources().getDimension(R.dimen.subdivider_thickness));
            viewDivider.setLayoutParams(params);
            viewDivider.setBackgroundColor(Color.BLACK);
            options.addView(viewDivider);
        }

        ImageButton back = findViewById(R.id.exit_button);
        // https://stackoverflow.com/questions/14848590/return-back-to-mainactivity-from-another-activity
        back.setOnClickListener(v -> finish());
    }

    /**
     * Store the id of the view that was just pressed in the bundle, then switch to the
     * Log Exercise screen for the next step.
     * @param view The text view that was just clicked by the user. This will have the request
     *             name that will be sent.
     */
    private void startLogExerciseIntent(View view) {
        // Borrowed from AS3-Accessibility
        Intent intent = new Intent(this, LogExerciseActivity.class);
        String exercise =  ((TextView)view).getText().toString();
        mExerciseInfo.putString("name", exercise);
        intent.putExtras(mExerciseInfo);
        startActivity(intent);
    }
}
