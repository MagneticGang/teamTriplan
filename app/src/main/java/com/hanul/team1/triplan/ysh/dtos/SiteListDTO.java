package com.hanul.team1.triplan.ysh.dtos;

import java.io.Serializable;

public class SiteListDTO implements Serializable {
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
}