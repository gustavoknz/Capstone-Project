package com.bora.gustavo.models;

import java.util.Date;
import java.util.List;

public class Gym {
    private String id;
    private double latitude;
    private double longitude;
    private int votesUp;
    private int votesDown;
    private String address;
    private String userId;
    private Date registeredAt;
    private boolean pcdAble;
    private List<Equipment> equipmentList;

    public Gym() {
    }

    public Gym(String id, double latitude, double longitude, int votesUp, int votesDown, String address, String userId, Date registeredAt, boolean pcdAble, List<Equipment> equipmentList) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.address = address;
        this.userId = userId;
        this.registeredAt = registeredAt;
        this.pcdAble = pcdAble;
        this.equipmentList = equipmentList;
    }

    @Override
    public String toString() {
        return "Gym{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", votesUp=" + votesUp +
                ", votesDown=" + votesDown +
                ", address='" + address + '\'' +
                ", userId='" + userId + '\'' +
                ", registeredAt=" + registeredAt +
                ", pcdAble=" + pcdAble +
                ", equipmentList=" + equipmentList +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
