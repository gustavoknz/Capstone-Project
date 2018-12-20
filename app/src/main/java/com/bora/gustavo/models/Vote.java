package com.bora.gustavo.models;

import java.util.Date;

public class Vote {
    private int userId;
    private int gymId;
    private boolean up;
    private Date date;

    public Vote() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
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
