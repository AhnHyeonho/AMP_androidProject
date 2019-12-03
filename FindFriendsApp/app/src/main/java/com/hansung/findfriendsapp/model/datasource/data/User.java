package com.hansung.findfriendsapp.model.datasource.data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String userName; // 실명
    public String userEmail; // email
    public String location; // 위치
    public String nickName; // 별명
    public String statusMessage; // 상태 메세지
    public String phoneNumber; // 핸드폰 번호
    public String userGroups; // 가입된 그룹. #을 기준으로 나뉨. ex. A#B#C 면 A그룹, B그룹,C그룹에 소속

    public User() {
    }

    public User(String userName, String userEmail, String location, String nickName, String statusMessage, String phoneNumber, String userGroups) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.location = location;
        this.nickName = nickName;
        this.statusMessage = statusMessage;
        this.phoneNumber = phoneNumber;
        this.userGroups = userGroups;
    }
}