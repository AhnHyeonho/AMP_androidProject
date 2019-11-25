package com.hansung.findfriendsapp.model.datasource.remote;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.hansung.findfriendsapp.model.datasource.data.User;

public class RemoteDataSourceImpl implements RemoteDataSource {

    private FirebaseAuth firebaseAuth;

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
    public GoogleSignInOptions initGoogleLogin(String id) {
        try {
            return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(id)
                    .requestEmail()
                    .build();
        }
        catch (Exception e){
            throw e;
        }
    }

    // firebase 관련 초기화 메소드
    @Override
    public void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance(); // FirebaseAuth 초기화
    }

    //로그인을 시도한 User에 대한 정보를 읽어오는 메소드
    @Override
    public User getUser() {
        return null; // 작성해야함
    }
}
