package com.bora.gustavo.models;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Gym {
    private String id;
    private float latitude;
    private float longitude;
    private int votesUp;
    private int votesDown;
    private String address;
    private Date registeredAt;
    private List<Equipment> equipmentList;

    public Gym() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Gym{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", votesUp=" + votesUp +
                ", votesDown=" + votesDown +
                ", address='" + address + '\'' +
                ", registeredAt=" + registeredAt +
                ", equipmentList=" + equipmentList +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
