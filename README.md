# Context-aware Workout Tracker

## Configuration Settings

To allow for usage of Mapbox, this application has been modified to compile on SDK version 31 instead
of 29. \
To allow for use of the LocalDate and DateDateTimeFormatter objects, the minimum SDK version has also
been changed from 24 to 26.

Non-standard Libraries used:
- Google Gson library version 2.8.9: Save and load data between
sessions
- Mapbox API version 10.5.0: Display location info
- Android play services location 19.0.1: Get device location

## API Keys

This application requires a public and private Mapbox API key, which have both been emailed to Dr. Bricker. \
As per step 1 of the
[sensing and location exercise readme](https://gitlab.cs.washington.edu/cse340/exercises/cse340-sensing-and-location/-/blob/main/README.md),
the public access key must be placed in `app/src/main/res/values/mapbox.xml`:
```
<resources>
   <string name="mapbox_access_token"></string>
</resources>
```
and the private access key should be placed in a global gradle.properties file (`C:\Users\<username>\.gradle`)
in the format `MAPBOX_SECRET_TOKEN=<YOUR_SECRET_TOKEN>`.


## Reading the Code: 

I did not realize any of my activities would need any sort of shared functionality, so I did not
leverage the power of the inheritance hierarchy at all! Sorry!\
However, here's an ordering for looking through the codebase that I think might make sense:\

1. MainActivity - The main screen
   1. `app/src/main/java/cse340/finalproject/MainActivity.java`
      1. Makes use of:
         1. `app/src/main/res/layout/exercise_card.xml`
         2. `app/src/main/java/cse340/finalproject/ExerciseBlock.java`
         3. `app/src/main/java/cse340/finalproject/ExerciseCardView.java`
   2. `app/src/main/res/layout/activity_main.xml` 
2. AddExerciseActivity - Screen where you can choose an exercise to log
   1. `app/src/main/java/cse340/finalproject/AddExerciseActivity.java`
   2. `app/src/main/res/layout/add_exercise_activity.xml`
   3. Also makes use of `app/src/main/res/layout/back_button_top_bar.xml`
3. LogExerciseActivity - Screen where you input info on an exercise you are logging
   1. `app/src/main/java/cse340/finalproject/LogExerciseActivity.java`
   2. `app/src/main/res/layout/log_exercise_activity.xml`
   3. Also makes use of `app/src/main/res/layout/back_button_top_bar.xml`
4. LocationActivity - Screen where you would in theory be able to set a location to receive reminders to track a workout
   1. `app/src/main/java/cse340/finalproject/LocationActivity.java`
   2. `app/src/main/res/layout/location_activity.xml`
5. Other things to look at?
   1. Manifest (`app/src/main/AndroidManifest.xml`)
   2. Values folder (`app/src/main/res/values`) - Not sure how important this is but I do be using values and not hardcoding everything. Usually, at least.

## Other information

I cannot think of anything else that would be necessary to know in order to use and read my application,
but feel free to contact me if anything comes up.

Also, I vastly overestimated the amount of time I would have to work on this so I ended up having to shrink
the scope even more than anticipated. As explained in the app, the location page was originally going
to be more than just a map with a little crosshair on it but I couldn't figure out how to do the
Mapbox stuff in time. \
<font size="1">
_Plz I beg do not obliterate my grade for this I swear I really thought I was going to be able to get
geofence-based notifications working_
</font>
