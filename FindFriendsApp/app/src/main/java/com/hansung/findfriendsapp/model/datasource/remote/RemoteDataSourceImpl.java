package com.hansung.findfriendsapp.model.datasource.remote;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.hansung.findfriendsapp.R;
import com.hansung.findfriendsapp.model.datasource.data.User;

public class RemoteDataSourceImpl implements RemoteDataSource {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInOptions googleSignInOptions;

    private RemoteDataSourceImpl(){}

    // 안전한 싱글톤 객체 생성을 위한 LazyHolder inner class
    private static class LazyHolder{
        public static final RemoteDataSourceImpl INSTANCE = new RemoteDataSourceImpl();
    }

    public static RemoteDataSourceImpl getInstance(){
        return LazyHolder.INSTANCE;
    }

    // 로그인에 관련된 처리를 해주는 메소드
    @Override
    public boolean doLogin() {
        return false;
    }
    // Google 로그인 관련 초기화 메소드

    @Override
    public void initGoogleLogin(Activity activity) {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    // firebase 관련 초기화 메소드
    @Override
    public void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance(); // FirebaseAuth 초기화
    }

    //로그인을 시도한 User에 대한 정보를 읽어오는 메소드
    @Override
    public User getUser() {
        return null;
    }
}
