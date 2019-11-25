package com.hansung.findfriendsapp.model.datasource;

import com.hansung.findfriendsapp.model.datasource.data.User;
import com.hansung.findfriendsapp.model.datasource.remote.RemoteDataSource;

public class RepositoryImpl implements Repository {
    RemoteDataSource remoteDataSource; // remoteDataSource 지정
    // 추후 localDataSource 추가 가능

    // 만약 localDataSource가 추가된다면 생성자의 매개변수 부분에 추가해줘야함. 생성자 오버로딩으로는 하지말자. 할거면 인터페이스에서 각 액티비티 별 필요한 Repository로 추가하여 작성.
    public RepositoryImpl(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public boolean doLogin() {
        return remoteDataSource.doLogin();
    }

    @Override
    public void initGoogleLogin(String id) {
        remoteDataSource.initGoogleLogin(id);
    }

    @Override
    public void initFirebase() {
        remoteDataSource.initFirebase();
    }

    @Override
    public User getUser() {
        return remoteDataSource.getUser();
    }
}
