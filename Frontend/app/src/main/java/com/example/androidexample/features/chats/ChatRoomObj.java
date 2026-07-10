package com.example.androidexample.features.chats;

import java.time.LocalTime;

public class ChatRoomObj {
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;
    private int duration;

    public ChatRoomObj(LocalTime startTime, LocalTime endTime, String day, int duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.duration = duration;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getDay() {
        return day;
    }

    public int getDuration() {
        return duration;
    }
}
