package com.example.windows.joeychatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.ET_LOGIN_EMAIL)
    TextInputEditText ET_LOGIN_EMAIL;
    @BindView(R.id.ET_LOGIN_PASSWORD)
    TextInputEditText ET_LOGIN_PASSWORD;
    @BindView(R.id.BTN_LOGIN_SUBMIT)
    Button BTN_LOGIN_SUBMIT;
    @BindView(R.id.TV_LOGIN_MESSAGE)
    TextView TV_LOGIN_MESSAGE;
    FirebaseAuth AUTH;
    ProgressDialog PROGRESS_DIALOG_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AUTH = FirebaseAuth.getInstance();
        PROGRESS_DIALOG_LOGIN = new ProgressDialog(this);
        PROGRESS_DIALOG_LOGIN.setTitle("Logging in");
        PROGRESS_DIALOG_LOGIN.setMessage("Loading...");
        PROGRESS_DIALOG_LOGIN.setCancelable(false);
    }

    @OnClick(R.id.BTN_LOGIN_SUBMIT)
    public void submit(View view)
    {
        if(hasNullFields().equals(false))
        {
            PROGRESS_DIALOG_LOGIN.show();
            String email = ET_LOGIN_EMAIL.getText().toString().trim();
            String password = ET_LOGIN_PASSWORD.getText().toString().trim();
            hideKeyboard(this);
            AUTH.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                PROGRESS_DIALOG_LOGIN.dismiss();
                                Intent sendToNextPage = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(sendToNextPage);
                                finish();
                            }
                            else
                            {
                                TV_LOGIN_MESSAGE.setText("Something went wrong");
                                PROGRESS_DIALOG_LOGIN.dismiss();
                                TV_LOGIN_MESSAGE.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            TV_LOGIN_MESSAGE.setText(e.getMessage());
                            PROGRESS_DIALOG_LOGIN.dismiss();
                            TV_LOGIN_MESSAGE.setVisibility(View.VISIBLE);
                        }
                    });
        }
        else
        {
            TV_LOGIN_MESSAGE.setText("Don't leave blank spaces");
            TV_LOGIN_MESSAGE.setVisibility(View.VISIBLE);
        }
    }

    private Boolean hasNullFields()
    {
        if(!TextUtils.isEmpty(ET_LOGIN_EMAIL.getText().toString().trim()) && !TextUtils.isEmpty(ET_LOGIN_PASSWORD.getText().toString().trim()))
            return false;
        return true;
    }

    private void hideKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
