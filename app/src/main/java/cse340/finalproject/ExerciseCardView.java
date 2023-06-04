package cse340.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

@SuppressLint("ViewConstructor")
public class ExerciseCardView extends FrameLayout {

    /**
     * ExerciseCardView constructor
     *
     * @param context Context to create the view in
     * @param e ExerciseBlock with information to include in the view
     */
    public ExerciseCardView(Context context, ExerciseBlock e) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.exercise_card, this, true);
        TextView nameField = findViewById(R.id.exercise_name);
        nameField.setText(e.getExercise());

        LinearLayout setsList = findViewById(R.id.sets_list);
        List<String> sets = e.getSets();
        for (String s : sets) {
            TextView currSet = new TextView(context);
            currSet.setText(s);
            setsList.addView(currSet);
        }
    }
}
