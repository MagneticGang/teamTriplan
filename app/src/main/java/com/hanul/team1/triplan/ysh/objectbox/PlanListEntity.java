package com.hanul.team1.triplan.ysh.objectbox;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class PlanListEntity {
    @Id(assignable = true) public long id;
    private int planid,seq;

    public PlanListEntity(long id, int planid, int seq) {
        this.planid = planid;
        this.seq = seq;
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

}
