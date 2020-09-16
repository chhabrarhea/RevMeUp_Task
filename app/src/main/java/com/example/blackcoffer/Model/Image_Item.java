package com.example.blackcoffer.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image_Item implements Parcelable {
     private String url;
     private String description;

    public Image_Item(String url, String description) {
        this.url = url;
        this.description = description;
    }
    Image_Item(){}

    protected Image_Item(Parcel in) {
        url = in.readString();
        description = in.readString();
    }

    public static final Creator<Image_Item> CREATOR = new Creator<Image_Item>() {
        @Override
        public Image_Item createFromParcel(Parcel in) {
            return new Image_Item(in);
        }

        @Override
        public Image_Item[] newArray(int size) {
            return new Image_Item[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(description);
    }
}
