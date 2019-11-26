package com.hansung.findfriendsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    private String userID = "admin";
    private EditText userAlias;
    private EditText userState;
    private EditText phoneNumber;
    private String [] stateArray = {"online","offline","away"};

    private Spinner stateSpinner;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference accountRef = mRootRef.child("account");
    DatabaseReference userRef = accountRef.child(userID);
    DatabaseReference stateRef = userRef.child("state");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userAlias = (EditText)findViewById(R.id.userAlias);
        userState = (EditText)findViewById(R.id.userState);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        stateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                userState.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        init(userID);
    }

    public void init(String userID) { //userID로 데이터 베이스에서 정보를 가져와서 초기화

    }
}
