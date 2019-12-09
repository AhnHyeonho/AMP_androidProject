package com.hansung.findfriendsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.hansung.findfriendsapp.model.datasource.LoginCallBack;
import com.hansung.findfriendsapp.model.datasource.Repository;
import com.hansung.findfriendsapp.model.datasource.RepositoryImpl;
import com.hansung.findfriendsapp.model.datasource.data.Pair;
import com.hansung.findfriendsapp.model.datasource.remote.RemoteDataSourceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    // Repository 생성 및 RemoteDataSource 생성
    private Repository repository = new RepositoryImpl(RemoteDataSourceImpl.getInstance());

    private FirebaseDatabase userRef;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";
    //
    //    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;
    //
    //    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;

    // 구글  로그인 버튼
    private SignInButton buttonGoogle;

    //TODO private Repository repo = RepositoryImpl.get~~~~()
    //remoteDataSource.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //kakaomap api에 등록할 프로젝트 hasykey를 얻는 메소드
        Log.d("Jisung", "테스트중입니다.");
        getHashKey();

        viewInit();

        //permission 얻기
        getLocationPermission();

        // 파이어베이스 인증 객체 선언
        repository.initFirebase();

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        String defaultWebClientId = getString(R.string.default_web_client_id);
        GoogleSignInOptions googleSignInOptions = repository.initGoogleLogin(defaultWebClientId);
        //google 로그인에 관련된 객체
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        Log.d("ahn", "signUp// email : " + email + " password : " + password);
        Pair<String, String> loginInfo = Pair.create(email, password);

        if (isValidEmail() && isValidPasswd()) {
            createUser(loginInfo);
        }
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        Log.d("ahn", "signIn// email : " + email + " password : " + password);
        Pair<String, String> loginInfo = Pair.create(email, password);

        if (isValidEmail() && isValidPasswd()) {
            loginUser(loginInfo);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        // 이메일 형식 불일치
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        // 비밀번호 형식 불일치
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else return PASSWORD_PATTERN.matcher(password).matches();
    }

    // 일반계정 회원가입
    private void createUser(Pair<String, String> loginInfo) {
        repository.doCreateUser(loginInfo, new LoginCallBack() {
            @Override
            public void onSuccess() {
                //회원가입 성공에 대한 처리
                Toast.makeText(MainActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                //realtime database의 users 트리안에 해당 uid로 child 생성.
            }

            @Override
            public void onFail() {
                //회원가입 실패에 대한 처리
                Toast.makeText(MainActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 일반계정 로그인
    private void loginUser(Pair<String, String> loginInfo) {
        repository.doLoginUser(loginInfo, new LoginCallBack() {
            @Override
            public void onSuccess() {
                //로그인 성공에 대한 처리
                //여기에 intent를 주면된다.
                Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();

                Log.d("ahn", "normal login success");
                getDeviceLocation();
                Intent intent = new Intent(getApplicationContext(), DummyMapsActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFail() {
                //로그인 실패에 대한 처리
                Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    // 구글계정 로그인
    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        repository.doFirebaseAuthWithGoogle(credential, new LoginCallBack() {
            @Override
            public void onSuccess() {
                // 로그인 성공
                Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();

                Log.d("ahn", "google login success");
                getDeviceLocation();
                Intent intent = new Intent(MainActivity.this, DummyMapsActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFail() {
                //로그인 실패
                Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewInit() {
        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);
        buttonGoogle = findViewById(R.id.btn_googleSignIn);
    }

    // 카카오맵 api에 등록할 해쉬키를 얻는 메소드
    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }


    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private LocationRequest locationRequest;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    private void getDeviceLocation() {

        locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();

                            LatLng mLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            String location = Double.toString(currentLocation.getLatitude()) + "%" + Double.toString(currentLocation.getLongitude());
                            repository.setLocation(location);
                            Log.d("ahn", "location:" + repository.getLocation());
                        } else {
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
        }
    }


    private void getLocationPermission() {
        Log.d("ahn", "getLocationPermission: Getting Location Permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}