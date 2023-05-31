package cse340.finalproject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_activity);

        TextView title = findViewById(R.id.back_button_top_bar_text);
        title.setText(getResources().getString(R.string.add_exercise_screen_title));

        LinearLayout options = findViewById(R.id.add_exercise_options);
        for (String s : EXERCISES) {
            // add exercise card
            TextView curr = new TextView(this);
            curr.setText(s);
            curr.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Go to log exercise screen
                        }
                    }
            );
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
    }
}
