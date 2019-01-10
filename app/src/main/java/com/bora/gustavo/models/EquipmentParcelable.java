package com.bora.gustavo.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class EquipmentParcelable implements Parcelable {
    private int id;
    private String name;

    protected EquipmentParcelable(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public EquipmentParcelable() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EquipmentParcelable> CREATOR = new Parcelable.Creator<EquipmentParcelable>() {
        @Override
        public EquipmentParcelable createFromParcel(Parcel in) {
            return new EquipmentParcelable(in);
        }

        @Override
        public EquipmentParcelable[] newArray(int size) {
            return new EquipmentParcelable[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "EquipmentParcelable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public EquipmentParcelable(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
