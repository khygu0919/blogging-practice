package com.example.myprotobuf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.example.myprotobuf.UserInfo.Person;
import com.google.protobuf.InvalidProtocolBufferException;

public class SecondActivity extends AppCompatActivity {
    private Person mPerson, mLoadedPerson;
    private String mConvertedProto, mLoadedProto;
    private GlobalDBHelper mDBHelper;
    private static final String COL_PROTO_STRING = "proto_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        try {
            mPerson = Person.parseFrom(Base64.decode(getIntent().getStringExtra("person"), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("before converting", mPerson.toString());
        mDBHelper = GlobalDBHelper.getInstance(getApplicationContext());
        mDBHelper.cleanProtoTable();

        mConvertedProto = Base64.encodeToString(mPerson.toByteArray(), Base64.DEFAULT);
        Log.e("before DB save", mConvertedProto);
        mLoadedProto = processSaveLoadProto(mConvertedProto);
        try {
            mLoadedPerson = Person.parseFrom(Base64.decode(mLoadedProto, Base64.DEFAULT));
            Log.e("converted", mLoadedPerson.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            Log.e("error string", "got error by Base64");
        }

        mConvertedProto = new String(mPerson.toByteArray());
        Log.e("before DB save", mConvertedProto);
        mLoadedProto = processSaveLoadProto(mConvertedProto);
        try {
            mLoadedPerson = Person.parseFrom(mLoadedProto.getBytes());
            Log.e("converted", mLoadedPerson.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            Log.e("error string", "got error by newString");
        }

        mConvertedProto = mPerson.toByteArray().toString();
        Log.e("before DB save", mConvertedProto);
        mLoadedProto = processSaveLoadProto(mConvertedProto);
        try {
            mLoadedPerson = Person.parseFrom(mLoadedProto.getBytes());
            Log.e("converted", mLoadedPerson.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            Log.e("error string", "got error by toString");
        }
    }

    private String processSaveLoadProto(String convertedString) {
        mDBHelper.cleanProtoTable(); // 1개만 저장한다고 하자.
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROTO_STRING, convertedString);
        mDBHelper.insertProto(contentValues);
        Cursor protoCursor = mDBHelper.getProto();
        protoCursor.moveToFirst();
        String loadedProtoString = protoCursor.getString(protoCursor.getColumnIndex(COL_PROTO_STRING));
        Log.e("Load", loadedProtoString);
        return loadedProtoString;
    }
}