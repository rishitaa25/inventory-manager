package com.example.androidexample.features.shifts;

import java.time.LocalTime;

public class ShiftObject {
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;
    private int duration;

    /**
     *  creates a shift object represeenting the shift a worker might be assigned to on a given day.
     * @param startTime starting time of shift
     * @param endTime ending time of shift
     * @param day date of the shift
     * @param duration total duration of the shift
     */

    public ShiftObject(LocalTime startTime, LocalTime endTime, String day, int duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.duration = duration;
    }

    /**
     *
     * @return start time of the shift
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     *
     * @return end time of the shift
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     *
     * @return date the shift occurs
     */
    public String getDay() {
        return day;
    }







}
