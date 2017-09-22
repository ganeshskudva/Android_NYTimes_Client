package com.example.gkudva.android_nytimes_client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by gkudva on 18/09/17.
 */

@Parcel
public class Byline {

    @SerializedName("original")
    @Expose
    public String original;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

}
