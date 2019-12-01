package com.hansung.findfriendsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    /////////////////////////////////////임시 : 액티비티를 실행할 때 실시간 데이터 베이스에서 유저의 계정을 찾을 수 있는 identity를 intent를 통해서 얻는다.
    private String userID = "test2";   //key의 역할을 임시로 admin으로 지정.
    //------------------------------------------------------------------------------------------------------------------------------------//
    //private ArrayList<String> stateArray = new ArrayList<>();
    private String [] stateArray = {"online","offline","away"};
    private ArrayAdapter adapter;
    private EditText userAlias;
    private Spinner stateSpinner;
    private EditText phoneNumber;
    private Button applyBtn;
    private Button revertBtn;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference accountRef = rootRef.child("account");
    DatabaseReference userRef = accountRef.child(userID);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userAlias = (EditText)findViewById(R.id.userAlias);
        stateSpinner = (Spinner)findViewById(R.id.stateSpinner);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        applyBtn = (Button)findViewById(R.id.applyBtn);
        revertBtn = (Button)findViewById(R.id.revertBtn);

        revertBtn.setOnClickListener(new View.OnClickListener(){    //이전 액티비티로 돌아가기 버튼
            @Override
            public void onClick(View view) {

            }
        });
        adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,stateArray);   //스피너 어댑터 초기화
        dataInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //dataInit();
        /*userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAlias.setText(dataSnapshot.child("alias").getValue().toString());
                stateSpinner.setAdapter(adapter);   //리스너에 어댑터를 설정하고,
                stateSpinner.setSelection(0);
                phoneNumber.setText(dataSnapshot.child("phoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        userRef.child("state").setValue("offline");
    }


    public void dataInit(){     //데이터베이스로부터 데이터를 가져와 초기화
        Log.e("test","listener작동");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot userDataSnapShot = dataSnapshot.child("account").child(userID);
                userAlias.setText(userDataSnapShot.child("alias").getValue().toString());
                stateSpinner.setAdapter(adapter);   //리스너에 어댑터를 설정하고,
                stateSpinner.setSelection(0);
                phoneNumber.setText(userDataSnapShot.child("phoneNumber").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }


}
