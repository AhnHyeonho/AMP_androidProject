package com.hansung.findfriendsapp.model.datasource;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.hansung.findfriendsapp.model.datasource.data.Pair;
import com.hansung.findfriendsapp.model.datasource.data.User;

public interface Repository {
    // 추후 localDataSource 추가 가능

    void doLoginUser(Pair<String, String> loginInfo, LoginCallBack callBack); // 로그인에 관련된 처리를 해주는 메소드

    void doCreateUser(Pair<String, String> loginInfo, LoginCallBack callBack); // 로그인에 관련된 처리를 해주는 메소드

    void doFirebaseAuthWithGoogle(AuthCredential credential, LoginCallBack callBack); // 구글로그인에 관련된 처리를 해주는 메소드

    GoogleSignInOptions initGoogleLogin(String id); // Google 로그인 관련 초기화 메소드

    void initFirebase(); // firebase 관련 초기화 메소드

    User getUser(); // 로그인을 시도한 User에 대한 정보를 읽어오는 메소드
}
