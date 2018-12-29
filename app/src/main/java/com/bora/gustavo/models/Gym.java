package com.bora.gustavo.models;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Gym {
    private double latitude;
    private double longitude;
    private int votesUp;
    private int votesDown;
    private String address;
    private Date registeredAt;
    private boolean pcdAble;
    private List<Equipment> equipmentList;

    public Gym() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Gym{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", votesUp=" + votesUp +
                ", votesDown=" + votesDown +
                ", address='" + address + '\'' +
                ", registeredAt=" + registeredAt +
                ", pcdAble=" + pcdAble +
                ", equipmentList=" + equipmentList +
                '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public boolean isPcdAble() {
        return pcdAble;
    }

    public void setPcdAble(boolean pcdAble) {
        this.pcdAble = pcdAble;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
