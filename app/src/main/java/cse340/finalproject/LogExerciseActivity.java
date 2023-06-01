package cse340.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogExerciseActivity extends AppCompatActivity {

    private String sets;

    private String reps;

    private String weight;

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_exercise_activity);

        Bundle mExerciseInfo = getIntent().getExtras();

        TextView title = findViewById(R.id.back_button_top_bar_text);
        title.setText(getResources().getString(R.string.logging_exercise_title,
                mExerciseInfo.getString("name")));


        ImageButton back = findViewById(R.id.exit_button);
        // https://stackoverflow.com/questions/14848590/return-back-to-mainactivity-from-another-activity
        // I'm aware that this doesn't close all the way back to the main activity but that
        // actually is better for what I want the "back" buttons to do
        back.setOnClickListener(v -> finish());

        Button save = findViewById(R.id.save_button);
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        // https://stackoverflow.com/questions/7075349/android-clear-activity-stack
                        String exerciseName = mExerciseInfo.getString("name");
                        String info =  getResources().getString(R.string.block_info, sets, reps,
                                weight);

                        Bundle updateInfo = new Bundle();
                        updateInfo.putString("name", exerciseName);
                        updateInfo.putString("info", info);
                        intent.putExtras(updateInfo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
        );
    }
}
