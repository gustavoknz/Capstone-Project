package com.bora.gustavo.models;

import android.support.annotation.NonNull;

import java.util.Date;

public class Vote {
    private String userId;
    private String gymId;
    private boolean up;
    private Date date;

    public Vote() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Vote{" +
                "userId=" + userId +
                ", gymId=" + gymId +
                ", up=" + up +
                ", date=" + date +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
