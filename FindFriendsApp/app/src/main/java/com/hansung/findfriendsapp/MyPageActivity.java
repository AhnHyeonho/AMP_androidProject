package com.hansung.findfriendsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    private String userID = "admin";
    private EditText userAlias;
    private EditText phoneNumber;
    private String [] stateArray = {"online","offline","away"};
    private ArrayAdapter adapter;

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
        stateSpinner = (Spinner)findViewById(R.id.stateSpinner);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,stateArray);
        stateSpinner.setAdapter(adapter);

        stateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                int index;
                switch(text){
                    case "online" : index =0;break;
                    case "offline" : index = 1; break;
                    case "away" : index = 2; break;
                    default: index= 0;break;
                }
                stateSpinner.setSelection(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //선택된 데이터로 저장
                String msg = adapterView.getSelectedItem().toString();
                Log.e("test",msg);
                stateRef.setValue(msg);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
