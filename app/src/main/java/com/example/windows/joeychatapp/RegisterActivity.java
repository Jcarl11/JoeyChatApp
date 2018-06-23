package com.example.windows.joeychatapp;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

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
    TextView TV_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }


    @OnClick(R.id.BTN_SUBMIT)
    public void submit(View view)
    {
        if(hasNullFields().equals(false))
        {
            TV_MESSAGE.setText("Success");
            TV_MESSAGE.setVisibility(View.VISIBLE);
        }
        else
        {
            TV_MESSAGE.setText("Don't leave blank spaces");
            TV_MESSAGE.setVisibility(View.VISIBLE);
        }
    }

    private Boolean hasNullFields()
    {
        if(TextUtils.isEmpty(ET_DISPLAYNAME.getText().toString().trim()) || TextUtils.isEmpty(ET_EMAIL.getText().toString().trim()) || TextUtils.isEmpty(ET_PASSWORD.getText().toString().trim()))
            return false;
        return true;
    }
}
