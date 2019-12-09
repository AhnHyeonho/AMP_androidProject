package com.hansung.findfriendsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {
    private Context context;
    private FirebaseAuth firebaseAuth;
    /////////////////////////////////////임시 : 액티비티를 실행할 때 실시간 데이터 베이스에서 유저의 계정을 찾을 수 있는 identity를 intent를 통해서 얻는다.
    private String userID;   //key의 역할을 임시로 admin으로 지정.
    //------------------------------------------------------------------------------------------------------------------------------------//
    private ArrayList<String> stateArray = new ArrayList<>();
    //private String [] stateArray = {"online","offline","away"};
    private ArrayAdapter adapter;
    private EditText userNickname;
    private Spinner stateSpinner;
    private EditText phoneNumber;
    private Button applyBtn;
    private Button revertBtn;

    private String uNickname;
    private String uPhoneNumber;
    private String uStateMessage;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootRef.child("User");
    DatabaseReference userIdRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        context = this;

        firebaseAuth = FirebaseAuth.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();
        Log.e("test", "userID = " +userID);
        userIdRef = userRef.child(userID);

        userNickname = (EditText)findViewById(R.id.userNickname);
        stateSpinner = (Spinner)findViewById(R.id.stateSpinner);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        applyBtn = (Button)findViewById(R.id.applyBtn);
        revertBtn = (Button)findViewById(R.id.revertBtn);
        uNickname = "";
        uPhoneNumber="";
        uStateMessage = "";

        applyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String inputNickname = userNickname.getText().toString();
                String inputPhoneNumber = phoneNumber.getText().toString();
                String inputState = stateSpinner.getSelectedItem().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림").setMessage("정보를 변경하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputNickname = userNickname.getText().toString();
                        String inputPhoneNumber = phoneNumber.getText().toString();
                        String inputState = stateSpinner.getSelectedItem().toString();
                        userIdRef.child("nickName").setValue(inputNickname);
                        userIdRef.child("phoneNumber").setValue(inputPhoneNumber);
                        userIdRef.child("state").setValue(inputState);

                        uNickname = inputNickname;
                        uPhoneNumber = inputPhoneNumber;
                        uStateMessage = inputState;
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                if(!inputNickname.equals(uNickname) ||                      ////사용자가 데이터를 변경하고 클릭했을 시
                        !inputPhoneNumber.equals(uPhoneNumber) ||
                        !inputState.equals(uStateMessage)){
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else{
                    Toast.makeText(context,"변경 사항이 없습니다.", Toast.LENGTH_LONG);
                }
            }
        });
        revertBtn.setOnClickListener(new View.OnClickListener(){    //이전 액티비티로 돌아가기 버튼
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"로그 아웃", Toast.LENGTH_SHORT);
                finish();
            }
        });

        dataInit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /////////////////////////////////파이어베이스에서 상태메세지목록을 가져와서 어댑터 초기화
                String [] arrayText = dataSnapshot.child("StateArray").getValue().toString().split("@");

                stateArray.clear();
                for(int i=0; i<arrayText.length;i++){
                    stateArray.add(arrayText[i]);
                }
                //adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,stateArray);   //스피너 어댑터 초기화
                adapter.notifyDataSetChanged();

                ////////////////////////////////데이터 임시 저장 : apply버튼시 변경한 데이터가 있는지 확인을 위함.///////////////
                DataSnapshot userDataSnapShot = dataSnapshot.child("User").child(userID);
                uNickname = userDataSnapShot.child("nickName").getValue().toString();
                uStateMessage = userDataSnapShot.child("state").getValue().toString();
                uPhoneNumber = userDataSnapShot.child("phoneNumber").getValue().toString();

                ///////////////////안드로이드 사용자 입력란 default value set/////////////////
                userNickname.setText(uNickname);
                stateSpinner.setAdapter(adapter);   //리스너에 어댑터를 설정하고,
                for(int i=0; i<stateArray.size();i++) { //데이터베이스로부터 저장되어있는 상태메세지를 스피너의 초깃값으로 지정.
                    if (stateArray.get(i).equals(uStateMessage)) {
                        stateSpinner.setSelection(i);
                        break;
                    }
                }
                phoneNumber.setText(userDataSnapShot.child("phoneNumber").getValue().toString());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        userIdRef.child("state").setValue("Offline");
    }


    public void dataInit(){     //데이터베이스로부터 데이터를 가져와 초기화
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /////////////////////////////////파이어베이스에서 상태메세지목록을 가져와서 어댑터 초기화
                String [] arrayText = dataSnapshot.child("StateArray").getValue().toString().split("@");
                for(int i=0; i<arrayText.length;i++){
                    stateArray.add(arrayText[i]);
                }
                adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,stateArray);   //스피너 어댑터 초기화

                ////////////////////////////////데이터 임시 저장 : apply버튼시 변경한 데이터가 있는지 확인을 위함.///////////////
                DataSnapshot userDataSnapShot = dataSnapshot.child("User").child(userID);
                uNickname = userDataSnapShot.child("nickName").getValue().toString();
                uStateMessage = userDataSnapShot.child("state").getValue().toString();
                uPhoneNumber = userDataSnapShot.child("phoneNumber").getValue().toString();

                ///////////////////안드로이드 사용자 입력란 default value set/////////////////
                userNickname.setText(userDataSnapShot.child("nickName").getValue().toString());
                stateSpinner.setAdapter(adapter);   //리스너에 어댑터를 설정하고,
                for(int i=0; i<stateArray.size();i++) { //데이터베이스로부터 저장되어있는 상태메세지를 스피너의 초깃값으로 지정.
                    if(uStateMessage.equals("")){
                        userIdRef.child("state").setValue("Online");        //계정을 처음만들어 정보가 없는 경우 Online으로 셋팅
                        uStateMessage = "Online";
                    }
                    if (stateArray.get(i).equals(uStateMessage)) {
                        stateSpinner.setSelection(i);
                        break;
                    }
                }
                phoneNumber.setText(userDataSnapShot.child("phoneNumber").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }


}
