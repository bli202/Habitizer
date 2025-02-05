package edu.ucsd.cse110.habitizer.lib.domain;

import java.time.Duration;
public class Task {
    private String name;
    private boolean checkedOff;

    // elapsed time in minutes
    private int elapsedTime;

    public Task(String name) {
        this.name = name;
        this.checkedOff = false;
        this.elapsedTime = 0;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isCheckedOff() {
        return checkedOff;
    }

    public void setCheckedOff(boolean checkedOff) {
        this.checkedOff = checkedOff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
