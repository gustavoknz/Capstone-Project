package com.bora.gustavo.models;

import android.support.annotation.NonNull;

import java.util.Date;

public class User {
    private String name;
    private String facebookId;
    private String email;
    private double lastLatitude;
    private double lastLongitude;
    private int votesUp;
    private int votesDown;
    private Date joinedAt;

    public User() {
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", email='" + email + '\'' +
                ", lastLatitude=" + lastLatitude +
                ", lastLongitude=" + lastLongitude +
                ", votesUp=" + votesUp +
                ", votesDown=" + votesDown +
                ", joinedAt=" + joinedAt +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public int getVotesUp() {
        return votesUp;
    }

    public void setVotesUp(int votesUp) {
        this.votesUp = votesUp;
    }

    public int getVotesDown() {
        return votesDown;
    }

    public void setVotesDown(int votesDown) {
        this.votesDown = votesDown;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }
}
