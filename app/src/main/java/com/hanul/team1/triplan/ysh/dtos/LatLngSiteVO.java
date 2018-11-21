package com.hanul.team1.triplan.ysh.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLngSiteVO {

    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lng")
    @Expose
    public Double lng;
    @SerializedName("dayid")
    @Expose
    public Integer dayid;
    @SerializedName("seq")
    @Expose
    public Integer seq;

}