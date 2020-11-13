package com.example.myprotobuf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.example.myprotobuf.UserInfo.Person;

public class MainActivity extends AppCompatActivity {
    Person protoPerson;
    Person.Builder protoPersonBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Integer mId = 1;
        String mUserName = "rick";
        String mAddress = "asdf1234@gmail.com";
        String[] mNumberList = {"010-1234-5678",  "064-123-4567"};
        Person.PhoneType[] mTypeList = {Person.PhoneType.MOBILE, Person.PhoneType.HOME};

        protoPersonBuilder = Person.newBuilder();
        protoPersonBuilder.setId(mId)
                .setName(mUserName)
                .setEmail(mAddress);

        for (int i = 0; i < 2; i++) {
            Person.PhoneNumber.Builder phoneNumberBuilder = Person.PhoneNumber.newBuilder();
            phoneNumberBuilder.setNumber(mNumberList[i])
                    .setType(mTypeList[i])
                    .build();
            protoPersonBuilder.addPhone(phoneNumberBuilder);
        }

        protoPerson = protoPersonBuilder.build();

        Log.e("MainAct: get builder", protoPersonBuilder.toString());
        Log.e("MainAct: get person", protoPerson.toString());
        Log.e("proto person name: ", protoPerson.getName());
        Log.e("second phoneNumber", protoPerson.getPhone(1).getNumber());

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("person", Base64.encodeToString(protoPerson.toByteArray(), Base64.DEFAULT));
        startActivity(intent);
        finish();
    }
}