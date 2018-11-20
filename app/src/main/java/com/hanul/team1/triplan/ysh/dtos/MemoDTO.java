package com.hanul.team1.triplan.ysh.dtos;

public class MemoDTO {
    int planid, dayid, siteid;
    String content;

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

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
