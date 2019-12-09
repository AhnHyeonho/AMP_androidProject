package com.hansung.findfriendsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hansung.findfriendsapp.model.datasource.Repository;
import com.hansung.findfriendsapp.model.datasource.RepositoryImpl;
import com.hansung.findfriendsapp.model.datasource.remote.RemoteDataSourceImpl;

public class DummyMapsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private String userID;
    private Button myPageBtn;
    private Button spotlightBtn;
    private TextView loginUserUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_maps);

        myPageBtn = findViewById(R.id.myPageBtn);
        spotlightBtn = findViewById(R.id.spotlightBtn);
        loginUserUID = findViewById(R.id.loginUserUID);


        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        loginUserUID.setText("Login ID : " + userID);
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DummyMapsActivity.this,MyPageActivity.class);
                startActivity(intent);
            }
        });

        spotlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DummyMapsActivity.this,SpotlightActivity.class);
                startActivity(intent);
            }
        });
    }
}
