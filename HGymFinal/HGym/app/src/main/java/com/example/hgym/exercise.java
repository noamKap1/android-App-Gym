package com.example.hgym;
//class for the object exercise.
public class exercise {
    protected String muscleGroup;
    protected String name;
    protected String levelOfDifficulty;
    protected String explanation;
    //a builder for the object.
    public exercise()
    {
        this.muscleGroup = "";
        this.name = "";
        this.levelOfDifficulty = "";
        this.explanation ="";
    }
    //a builder for the object.
    public exercise(String muscleGroup, String name, String levelOfDifficulty, String explanation) {
        this.muscleGroup = muscleGroup;
        this.name = name;
        this.levelOfDifficulty = levelOfDifficulty;
        this.explanation = explanation;
    }
    //get the explanation.
    public String getExplanation() {
        return explanation;
    }
    //get the explanation.
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    //get muscle group.
    public String getMuscleGroup() {
        return muscleGroup;
    }
    //set muscle group.
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
    //get name.
    public String getName() {
        return name;
    }
    //set name.
    public void setName(String name) {
        this.name = name;
    }
    //get level.
    public String getLevelOfDifficulty() {
        return levelOfDifficulty;
    }
    //set level.
    public void setLevelOfDifficulty(String levelOfDifficulty) {
        this.levelOfDifficulty = levelOfDifficulty;
    }
}
