package cse340.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://www.tutorialkart.com/java/how-to-get-current-date-in-yyyy-mm-dd-format-in-java/#gsc.tab=0
//        LocalDate dateObj = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String date = dateObj.format(formatter);

        Button currDate = findViewById(R.id.main_screen_current_date);
        currDate.setText("TODAY"); // TODO: REPLACE WITH ACTUAL CURRENT DATE
        // There's a good chance the calendar stuff never gets featured and we only end up
        // saving and editing one workout session because I really am running out of time

        LinearLayout cardContainer = findViewById(R.id.exercise_cards_container);
        List<String> sets = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            sets.add("Set " + (i + 1));
        }
        cardContainer.addView(new ExerciseCardView(this, "Beans", sets));
    }
}