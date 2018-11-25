package com.hanul.team1.triplan.ysh.objectbox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class UserEntity {
    @Id(assignable = true) public long id;
    public String userid;

    public UserEntity(long id, String userid) {
        this.id = id;
        this.userid = userid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
