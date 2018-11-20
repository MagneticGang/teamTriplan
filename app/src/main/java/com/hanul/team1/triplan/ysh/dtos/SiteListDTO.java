package com.hanul.team1.triplan.ysh.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SiteListDTO implements Serializable {

    @SerializedName("placeid")
    @Expose
    public String placeid;
    @SerializedName("siteName")
    @Expose
    public String siteName;
    @SerializedName("siteType")
    @Expose
    public String siteType;
    @SerializedName("seq")
    @Expose
    public Integer seq;
    @SerializedName("planid")
    @Expose
    public Integer planid;
    @SerializedName("dayid")
    @Expose
    public Integer dayid;
    @SerializedName("siteid")
    @Expose
    public Integer siteid;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lng")
    @Expose
    public Double lng;

}





/*
    protected SiteListDTO(Parcel in) {
        placeid = in.readString();
        siteName = in.readString();
        siteType = in.readString();
        if (in.readByte() == 0) {
            seq = null;
        } else {
            seq = in.readInt();
        }
        if (in.readByte() == 0) {
            planid = null;
        } else {
            planid = in.readInt();
        }
        if (in.readByte() == 0) {
            dayid = null;
        } else {
            dayid = in.readInt();
        }
        if (in.readByte() == 0) {
            siteid = null;
        } else {
            siteid = in.readInt();
        }
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
    }

    public static final Creator<SiteListDTO> CREATOR = new Creator<SiteListDTO>() {
        @Override
        public SiteListDTO createFromParcel(Parcel in) {
            return new SiteListDTO(in);
        }

        @Override
        public SiteListDTO[] newArray(int size) {
            return new SiteListDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeid);
        dest.writeString(siteName);
        dest.writeString(siteType);
        dest.writeInt(seq);
        dest.writeInt(planid);
        dest.writeInt(dayid);
        dest.writeInt(siteid);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }*/


/*
package com.hanul.team1.triplan.ysh.dtos;

import android.os.Parcelable;

public class SiteListDTO implements Parcelable {
    private String placeid, siteName, siteType;
    private int seq, planid, dayid, siteid;
    private double lat, lng;

    public SiteListDTO() {
    }

    public SiteListDTO(String placeid, String siteName, String siteType, int seq, int planid, int dayid, double lat, double lng, int siteid) {
        this.placeid = placeid;
        this.siteName = siteName;
        this.siteType = siteType;
        this.seq = seq;
        this.planid = planid;
        this.dayid = dayid;
        this.lat = lat;
        this.lng = lng;
        this.siteid = siteid;
    }

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public int getDayid() {
        return dayid;
    }

    public void setDayid(int dayid) {
        this.dayid = dayid;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }



}*/
