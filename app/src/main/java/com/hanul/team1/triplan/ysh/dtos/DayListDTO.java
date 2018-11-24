package com.hanul.team1.triplan.ysh.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DayListDTO implements Serializable {
    String dates, days, destName, placeid;
    int cntSite, dayid, day, planid;

    public DayListDTO(String dates, String days, String destName, int cntSite, int dayid, int day, int planid, String placeid) {
        this.dates = dates;
        this.days = days;
        this.destName = destName;
        this.cntSite = cntSite;
        this.dayid = dayid;
        this.day = day;
        this.planid = planid;
        this.placeid = placeid;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public int getCntSite() {
        return cntSite;
    }

    public void setCntSite(int cntSite) {
        this.cntSite = cntSite;
    }

    public int getDayid() {
        return dayid;
    }

    public void setDayid(int dayid) {
        this.dayid = dayid;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

}
