package com.example.windows.joeychatapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;

public class RegisterActivity extends AppCompatActivity
{

    @BindView(R.id.BTN_SUBMIT)
    Button BTN_SUBMIT;
    @BindView(R.id.BTN_CLEAR)
    Button BTN_CLEAR;
    @BindView(R.id.ET_DISPLAYNAME)
    TextInputEditText ET_DISPLAYNAME;
    @BindView(R.id.ET_EMAIL)
    TextInputEditText ET_EMAIL;
    @BindView(R.id.ET_PASSWORD)
    TextInputEditText ET_PASSWORD;
    @BindView(R.id.TV_MESSAGE)
    TextView TB_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
