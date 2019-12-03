package com.hansung.findfriendsapp.model.datasource.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hansung.findfriendsapp.model.datasource.LoginCallBack;
import com.hansung.findfriendsapp.model.datasource.data.Pair;
import com.hansung.findfriendsapp.model.datasource.data.User;

import java.util.HashMap;
import java.util.Map;

public class RemoteDataSourceImpl implements RemoteDataSource {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private DatabaseReference groupReference;

    private RemoteDataSourceImpl() {
    }

    // 안전한 싱글톤 객체 생성을 위한 LazyHolder inner class
    private static class LazyHolder {
        public static final RemoteDataSourceImpl INSTANCE = new RemoteDataSourceImpl();
    }

    public static RemoteDataSourceImpl getInstance() {
        return LazyHolder.INSTANCE;
    }

    // 로그인에 관련된 처리를 해주는 메소드
    @Override
    public void doLoginUser(Pair<String, String> loginInfo, final LoginCallBack callback) {
        firebaseAuth.signInWithEmailAndPassword(loginInfo.left, loginInfo.right)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            callback.onSuccess();
                            //Toast.makeText(MainActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            callback.onFail();
                            //Toast.makeText(MainActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        firebaseAuth.signInWithEmailAndPassword(loginInfo.left, loginInfo.right)
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void doCreateUser(Pair<String, String> loginInfo, final LoginCallBack callback) {
        firebaseAuth.createUserWithEmailAndPassword(loginInfo.left, loginInfo.right)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Log.d("ahn", "remoteDataSource onComplete Success");
                            // realtime database에 해당 이름으로 사용자 정보 추가
                            Log.d("ahn", "curretUser uid : " + firebaseAuth.getCurrentUser().getUid());

                            String userUid = firebaseAuth.getCurrentUser().getUid();
                            userReference.child(userUid).setValue(new User("","","","","","",""));

                            callback.onSuccess();
                            //Toast.makeText(MainActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Log.d("ahn", "remoteDataSource onComplete Fail");
                            callback.onFail();
                            //Toast.makeText(MainActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void doFirebaseAuthWithGoogle(AuthCredential credential, final LoginCallBack callback) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            String userUid = firebaseAuth.getCurrentUser().getUid();
                            userReference.child(userUid).setValue(new User("","","","","","",""));

                            callback.onSuccess();
                            //Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            callback.onFail();
                            //Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    // Google 로그인 관련 초기화 메소드
    @Override
    public GoogleSignInOptions initGoogleLogin(String id) {
        try {
            return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(id)
                    .requestEmail()
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    // firebase 관련 초기화 메소드
    @Override
    public void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance(); // FirebaseAuth 초기화
        firebaseDatabase = FirebaseDatabase.getInstance(); // FIrebaseDatabase 초기화
        userReference = firebaseDatabase.getReference("User"); // 사용자 목록 root인 User로 초기화
        groupReference = firebaseDatabase.getReference("Group"); // 사용자 목록 root인 User로 초기화
    }

    //로그인을 시도한 User에 대한 정보를 읽어오는 메소드
    @Override
    public User getUser() {
        return null; // 작성해야함
    }

}

