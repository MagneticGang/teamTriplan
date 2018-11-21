package com.hanul.team1.triplan.ysh.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlanListDTO implements Serializable {
    private String name, placeid, userid, dates;
    private int planid, cntDay, cntSite;
    private double totDistance;

    public PlanListDTO(String name, String placeid, String userid, String dates, int planid, int cntDay, int cntSite, double totDistance) {
        this.name = name;
        this.placeid = placeid;
        this.userid = userid;
        this.dates = dates;
        this.planid = planid;
        this.cntDay = cntDay;
        this.cntSite = cntSite;
        this.totDistance = totDistance;
    }

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

    public double getTotDistance() {
        return totDistance;
    }

    public void setTotDistance(double totDistance) {
        this.totDistance = totDistance;
    }
}
