package com.bora.gustavo.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GymParcelable implements Parcelable {
    private String id;
    private double latitude;
    private double longitude;
    private int votesUp;
    private int votesDown;
    private String address;
    private Date registeredAt;
    private boolean pcdAble;
    private List<EquipmentParcelable> equipmentList;

    protected GymParcelable(Parcel in) {
        id = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        votesUp = in.readInt();
        votesDown = in.readInt();
        address = in.readString();
        long tmpRegisteredAt = in.readLong();
        registeredAt = tmpRegisteredAt != -1 ? new Date(tmpRegisteredAt) : null;
        pcdAble = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            equipmentList = new ArrayList<>();
            in.readList(equipmentList, Equipment.class.getClassLoader());
        } else {
            equipmentList = null;
        }
    }

    public GymParcelable(Gym gym) {
        this.id = gym.getId();
        this.latitude = gym.getLatitude();
        this.longitude = gym.getLongitude();
        this.votesUp = gym.getVotesUp();
        this.votesDown = gym.getVotesDown();
        this.address = gym.getAddress();
        this.registeredAt = gym.getRegisteredAt();
        this.pcdAble = gym.isPcdAble();
        this.equipmentList = new ArrayList<>();
        EquipmentParcelable ep;
        for (Equipment e : gym.getEquipmentList()) {
            ep = new EquipmentParcelable();
            ep.setId(e.getId());
            ep.setName(e.getName());
            this.equipmentList.add(ep);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(votesUp);
        dest.writeInt(votesDown);
        dest.writeString(address);
        dest.writeLong(registeredAt != null ? registeredAt.getTime() : -1L);
        dest.writeByte((byte) (pcdAble ? 0x01 : 0x00));
        if (equipmentList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(equipmentList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GymParcelable> CREATOR = new Parcelable.Creator<GymParcelable>() {
        @Override
        public GymParcelable createFromParcel(Parcel in) {
            return new GymParcelable(in);
        }

        @Override
        public GymParcelable[] newArray(int size) {
            return new GymParcelable[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "Gym{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", votesUp=" + votesUp +
                ", votesDown=" + votesDown +
                ", address='" + address + '\'' +
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

    public List<EquipmentParcelable> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<EquipmentParcelable> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
