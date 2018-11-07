package com.hanul.team1.triplan.ggs.gnuserdto;

public class GNUserDTO {

    private String userid, userpwd, nickname;

    public GNUserDTO(){}

    public GNUserDTO(String userid, String userpwd, String nickname) {
        this.userid = userid;
        this.userpwd = userpwd;
        this.nickname = nickname;
    }

    public GNUserDTO(String userid, String nickname) {
        this.userid = userid;
        this.nickname = nickname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
