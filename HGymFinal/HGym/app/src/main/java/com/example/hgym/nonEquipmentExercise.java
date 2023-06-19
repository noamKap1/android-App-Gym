package com.example.hgym;
//class for the object non equipment exercise.
public class nonEquipmentExercise extends exercise {
    protected double duration;

    //builder for the object.
    public nonEquipmentExercise(String muscleGroup, String name, String levelOfDifficulty, String exp, double duration) {
        super(muscleGroup, name, levelOfDifficulty, exp);
        this.duration = duration;
    }
    //an empty builder.
    public nonEquipmentExercise(){
        super();
        this.duration=0;
    }
    //get the duration.
    public double getDuration() {
        return duration;
    }
    //set the duration.
    public void setDuration(double duration) {
        this.duration = duration;
    }
}
