package com.example.windows.joeychatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartingActivity extends AppCompatActivity
{

    @BindView(R.id.BTN_LOGIN)
    Button BTN_LOGIN;

    @BindView(R.id.BTN_CREATEACC)
    Button BTN_CREATEACC;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.BTN_LOGIN)
    public void login(View view)
    {
        openNextActivity(LoginActivity.class);
    }

    @OnClick(R.id.BTN_CREATEACC)
    public void register(View view)
    {
        openNextActivity(RegisterActivity.class);
    }

    private void openNextActivity(Class<?> destination)
    {
        Intent send = new Intent(StartingActivity.this, destination);
        startActivity(send);
    }
}
