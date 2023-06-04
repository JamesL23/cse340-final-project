package cse340.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogExerciseActivity extends AppCompatActivity {

    /**
     * String representing the number of sets (the exercise kind) done
     */
    private String sets;

    /**
     * String representing number of reps performed of the exercise
     */
    private String reps;

    /**
     * String representing amount of weight used for the exercise
     */
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
        save.setOnClickListener(v -> {
                    if (sets == null || reps == null || weight == null) {
                        Toast.makeText(v.getContext(),
                                v.getContext().getResources().getString(R.string.missing_fields),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Only proceed if all fields filled
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        // https://stackoverflow.com/questions/7075349/android-clear-activity-stack
                        String exerciseName = mExerciseInfo.getString("name");
                        String info = getResources().getString(R.string.block_info, sets, reps,
                                weight);

                        Bundle updateInfo = new Bundle();
                        updateInfo.putString("name", exerciseName);
                        updateInfo.putString("info", info);
                        intent.putExtras(updateInfo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

        // Update from fields
        // https://stackoverflow.com/questions/20824634/android-on-text-change-listener
        EditText setsField = findViewById(R.id.sets_field);
        setsField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sets = s.toString();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void afterTextChanged(Editable s) {}
                });

        EditText repsField = findViewById(R.id.reps_field);
        repsField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        reps = s.toString();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void afterTextChanged(Editable s) {}
                });

        EditText weightField = findViewById(R.id.weight_field);
        weightField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        weight = s.toString();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void afterTextChanged(Editable s) {}
                });
    }
}
