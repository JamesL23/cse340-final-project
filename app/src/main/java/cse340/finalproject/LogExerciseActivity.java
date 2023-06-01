package cse340.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogExerciseActivity extends AppCompatActivity {

    /** Bundle used to transfer name of selected exercise to the log exercise screen **/
    private Bundle mExerciseInfo;

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_exercise_activity);

        mExerciseInfo = getIntent().getExtras();

        TextView title = findViewById(R.id.back_button_top_bar_text);
        title.setText(getResources().getString(R.string.logging_exercise_title,
                mExerciseInfo.getString("name")));


        ImageButton back = findViewById(R.id.exit_button);
        // https://stackoverflow.com/questions/14848590/return-back-to-mainactivity-from-another-activity
        // I'm aware that this doesn't close all the way back to the main activity but that
        // actually is better for what I want the "back" buttons to do
        back.setOnClickListener(v -> finish());
    }
}
