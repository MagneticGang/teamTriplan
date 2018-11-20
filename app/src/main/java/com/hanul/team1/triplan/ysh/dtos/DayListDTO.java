package com.hanul.team1.triplan.ysh.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DayListDTO implements Parcelable {
    String dates, days, destName;
    int cntSite, dayid, day, planid;

    public DayListDTO(String dates, String days, String destName, int cntSite, int dayid, int day, int planid) {
        this.dates = dates;
        this.days = days;
        this.destName = destName;
        this.cntSite = cntSite;
        this.dayid = dayid;
        this.day = day;
        this.planid = planid;
    }

    protected DayListDTO(Parcel in) {
        dates = in.readString();
        days = in.readString();
        destName = in.readString();
        cntSite = in.readInt();
        dayid = in.readInt();
        day = in.readInt();
        planid = in.readInt();
    }

    public static final Creator<DayListDTO> CREATOR = new Creator<DayListDTO>() {
        @Override
        public DayListDTO createFromParcel(Parcel in) {
            return new DayListDTO(in);
        }

        @Override
        public DayListDTO[] newArray(int size) {
            return new DayListDTO[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dates);
        dest.writeString(days);
        dest.writeString(destName);
        dest.writeInt(cntSite);
        dest.writeInt(dayid);
        dest.writeInt(day);
        dest.writeInt(planid);
    }
}
