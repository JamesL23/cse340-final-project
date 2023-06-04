package cse340.finalproject;

import java.util.LinkedList;
import java.util.List;

public class ExerciseBlock {
    /**
     * Name of the exercise associated with the ExerciseBlock
     */
    private final String exercise;

    /**
     * List of string info for each set (the exercise kind) done in the ExerciseBlock
     */
    private final List<String> sets;

    /**
     * ExerciseBlock constructor
     *
     * @param exercise Name of exercise
     * @param sets List of strings with info on each set (the exercise kind) done in the ExerciseBlock
     */
    public ExerciseBlock(String exercise, List<String> sets) {
        this.exercise = exercise;
        this.sets = new LinkedList<>();
        this.sets.addAll(sets);
    }

    /**
     * Getter for name of exercise
     *
     * @return Name of exercise associated with the ExerciseBlock
     */
    public String getExercise() {
        return exercise;
    }

    /**
     * Getter for info on sets (the exercise kind) done in ExerciseBlock
     *
     * @return List of strings, each one representing info on a set (the exercise kind) done in the
     * ExerciseBlock
     */
    public List<String> getSets() {
        return sets;
    }

    /**
     * Public modifier method to add sets (the exercise kind) to the ExerciseBlock
     *
     * @param set A string with information on the set (the exercise kind) to add to the
     *            ExerciseBlock
     * @return true on success, false otherwise
     */
    public boolean addSet(String set) {
        return sets.add(set);
    }
}
