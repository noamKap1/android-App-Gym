package com.example.hgym;
//the class for the object equipment exercise.
public class equipmentExercise extends exercise{
    protected double weight;

    //a builder for the object.
    public equipmentExercise(String muscleGroup, String name, String levelOfDifficulty, String exp, double weight) {
        super(muscleGroup, name, levelOfDifficulty, exp);
        this.weight = weight;
    }
    //an empty builder for the object.
    public equipmentExercise(){
        super();
        this.weight=0;
    }
    //get the weight.
    public double getWeight() {
        return weight;
    }
    //set the weight.
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
