package com.example.windows.joeychatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Intent openNextPage = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(openNextPage);
            finish();
        }
    }

    @OnClick(R.id.BTN_SUBMIT)
    public void submit(View view)
    {
        if(hasNullFields().equals(false))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.setTitle("Registering account");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String email = ET_EMAIL.getText().toString().trim();
            String password = ET_PASSWORD.getText().toString().trim();
            hideKeyboard(this);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user = auth.getCurrentUser();
                                TV_MESSAGE.setText("Successful");
                                progressDialog.dismiss();
                            }
                            else
                                TV_MESSAGE.setText("Register not successful");
                        }
                    })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    TV_MESSAGE.setText(e.getMessage());
                    progressDialog.dismiss();
                }
            });

        }
        else
        {
            TV_MESSAGE.setText("Don't leave blank spaces");
            TV_MESSAGE.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.BTN_CLEAR)
    public void clearFields(View view)
    {
        ET_DISPLAYNAME.getText().clear();
        ET_EMAIL.getText().clear();
        ET_PASSWORD.getText().clear();
        TV_MESSAGE.setText("");
    }

    private Boolean hasNullFields()
    {
        if(!TextUtils.isEmpty(ET_DISPLAYNAME.getText().toString().trim()) && !TextUtils.isEmpty(ET_EMAIL.getText().toString().trim()) && !TextUtils.isEmpty(ET_PASSWORD.getText().toString().trim()))
            return false;
        return true;
    }

    private void hideKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
