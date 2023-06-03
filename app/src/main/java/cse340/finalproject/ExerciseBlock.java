package cse340.finalproject;

import java.util.LinkedList;
import java.util.List;

public class ExerciseBlock {
    private final String exercise;

    private final List<String> sets;

    public ExerciseBlock(String exercise, List<String> sets) {
        this.exercise = exercise;
        this.sets = new LinkedList<>();
        this.sets.addAll(sets);
    }

    public String getExercise() {
        return exercise;
    }

    public List<String> getSets() {
        return sets;
    }

    public boolean addSet(String set) {
        return sets.add(set);
    }
}
