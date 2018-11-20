package com.hanul.team1.triplan.ysh.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlanListDTO implements Parcelable {
    private String name, placeid, userid, dates;
    private int planid, cntDay, cntSite, totDistance;

    public PlanListDTO(String name, String placeid, String userid, String dates, int planid, int cntDay, int cntSite, int totDistance) {
        this.name = name;
        this.placeid = placeid;
        this.userid = userid;
        this.dates = dates;
        this.planid = planid;
        this.cntDay = cntDay;
        this.cntSite = cntSite;
        this.totDistance = totDistance;
    }

    protected PlanListDTO(Parcel in) {
        name = in.readString();
        placeid = in.readString();
        userid = in.readString();
        dates = in.readString();
        planid = in.readInt();
        cntDay = in.readInt();
        cntSite = in.readInt();
        totDistance = in.readInt();
    }

    public static final Creator<PlanListDTO> CREATOR = new Creator<PlanListDTO>() {
        @Override
        public PlanListDTO createFromParcel(Parcel in) {
            return new PlanListDTO(in);
        }

        @Override
        public PlanListDTO[] newArray(int size) {
            return new PlanListDTO[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public int getCntDay() {
        return cntDay;
    }

    public void setCntDay(int cntDay) {
        this.cntDay = cntDay;
    }

    public int getCntSite() {
        return cntSite;
    }

    public void setCntSite(int cntSite) {
        this.cntSite = cntSite;
    }

    public int getTotDistance() {
        return totDistance;
    }

    public void setTotDistance(int totDistance) {
        this.totDistance = totDistance;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(placeid);
        dest.writeString(userid);
        dest.writeString(dates);
        dest.writeInt(planid);
        dest.writeInt(cntDay);
        dest.writeInt(cntSite);
        dest.writeInt(totDistance);
    }
}
