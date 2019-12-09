package com.hansung.findfriendsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hansung.findfriendsapp.model.datasource.Repository;
import com.hansung.findfriendsapp.model.datasource.RepositoryImpl;
import com.hansung.findfriendsapp.model.datasource.remote.RemoteDataSourceImpl;

import java.util.Random;

public class SpotlightActivity extends AppCompatActivity {

    // Repository 생성 및 RemoteDataSource 생성
    private Repository repository = new RepositoryImpl(RemoteDataSourceImpl.getInstance());


    private FirebaseAuth firebaseAuth;

    private String userID;
    private Button backBtn;
    private TextView nickNameTextView;
    private TextView spotlightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotlight);

        nickNameTextView = findViewById(R.id.nickNameTextView);
        spotlightView = findViewById(R.id.spotlightView);
        backBtn = findViewById(R.id.backBtn);

        repository.initFirebase();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        nickNameTextView.setText("nickName");

        int color = getRandomColor(); // 랜덤 색상 지정
        spotlightView.setBackgroundColor(color); // spotcolorView 배경색 변경
        repository.setSpotlightColor(Integer.toString(color)); // firebase의 컬러 변경

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DummyMapsActivity.class);
                startActivity(intent);
                repository.setSpotlightColor(""); // firebase의 컬러 초기화
            }
        });
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}
