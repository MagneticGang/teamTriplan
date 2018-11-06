package com.hanul.team1.triplan.jiyoon;

public class MemberDTO {
    String uuid, nickname;

    //빈깡통
    public  MemberDTO(){};

    //초기화, getter-setter
    public  MemberDTO(String uuid, String nickname){
        this.uuid = uuid;
        this.nickname = nickname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



}
