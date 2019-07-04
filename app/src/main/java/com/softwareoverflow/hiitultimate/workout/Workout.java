package com.softwareoverflow.hiitultimate.workout;

import java.util.ArrayList;
import java.util.List;

/**
 * A Workout is a group of {@link WorkoutSet}s completed in order, usually with some form of
 * 'recovery' time between them.
 */
public class Workout {

    private String name, description;
    private List<WorkoutSet> sets = new ArrayList<>();

}
