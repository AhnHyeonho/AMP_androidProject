package com.hansung.findfriendsapp.model.datasource;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.hansung.findfriendsapp.model.datasource.data.Pair;
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
    public void doLoginUser(Pair<String, String> loginInfo, final LoginCallBack callBack) {
        remoteDataSource.doLoginUser(loginInfo, new LoginCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();   // 계정 로그인에 대한 성공 콜백
            }

            @Override
            public void onFail() {
                callBack.onFail();   // 계정 로그인에 대한 실패 콜백
            }
        });
    }

    @Override
    public void doCreateUser(Pair<String, String> loginInfo, final LoginCallBack callBack) {
        remoteDataSource.doCreateUser(loginInfo, new LoginCallBack() {
            @Override
            public void onSuccess() {
                Log.d("ahn","repository onComplete Success");
                callBack.onSuccess();   // 계정 생성에 대한 성공 콜백
            }

            @Override
            public void onFail() {
                Log.d("ahn","repository onComplete Fail");
                callBack.onFail();   // 계정 생성에 대한 실패 콜백
            }
        });
    }

    @Override
    public void doFirebaseAuthWithGoogle(AuthCredential credential, final LoginCallBack callBack) {
        remoteDataSource.doFirebaseAuthWithGoogle(credential, new LoginCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();   // 계정 생성에 대한 성공 콜백
            }

            @Override
            public void onFail() {
                callBack.onFail();   // 계정 생성에 대한 실패 콜백
            }
        });
    }

    @Override
    public GoogleSignInOptions initGoogleLogin(String id) {
        return remoteDataSource.initGoogleLogin(id);
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
