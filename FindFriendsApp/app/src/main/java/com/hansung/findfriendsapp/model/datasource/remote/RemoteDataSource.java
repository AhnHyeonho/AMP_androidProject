package com.hansung.findfriendsapp.model.datasource.remote;

import com.hansung.findfriendsapp.data.User;

public interface RemoteDataSource {
    boolean doLogin(); // 로그인에 관련된 처리를 해주는 메소드
    User getuser(); // 로그인을 시도한 User에 대한 정보를 읽어오는 메소드
}
