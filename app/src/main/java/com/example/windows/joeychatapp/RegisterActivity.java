package com.example.windows.joeychatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

public class RegisterActivity extends AppCompatActivity
{

    @BindView(R.id.BTN_SUBMIT)
    Button BTN_SUBMIT;
    @BindView(R.id.BTN_CHOOSEIMAGE)
    Button BTN_CLEAR;
    @BindView(R.id.ET_DISPLAYNAME)
    TextInputEditText ET_DISPLAYNAME;
    @BindView(R.id.ET_EMAIL)
    TextInputEditText ET_EMAIL;
    @BindView(R.id.ET_PASSWORD)
    TextInputEditText ET_PASSWORD;
    @BindView(R.id.TV_MESSAGE)
    TextView TV_MESSAGE;
    @BindView(R.id.IMV_REGISTER)
    ImageView IMV_REGISTER;
    FirebaseAuth auth;
    ProgressDialog PROGRESS_DIALOG;

    Uri imagePathURI;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        PROGRESS_DIALOG = new ProgressDialog(this);
        PROGRESS_DIALOG.setMessage("loading...");
        PROGRESS_DIALOG.setTitle("Registering account");
        PROGRESS_DIALOG.setCancelable(false);
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
            PROGRESS_DIALOG.show();
            String email = ET_EMAIL.getText().toString().trim();
            String password = ET_PASSWORD.getText().toString().trim();
            hideKeyboard(this);
            try
            {
                uploadImage(imagePathURI, email, password);
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
        else
        {
            TV_MESSAGE.setText("Don't leave blank spaces");
            TV_MESSAGE.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.BTN_CHOOSEIMAGE)
    public void chooseImage(View view)
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setAspectRatio(1,1)
                .setActivityTitle("Choose an image")
                .start(RegisterActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            IMV_REGISTER.setImageDrawable(null);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //Uri resultUri = result.getUri();
            imagePathURI = result.getUri();
            Picasso.get().load(imagePathURI).placeholder(R.drawable.defaultimage).into(IMV_REGISTER);
        }
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

    private void uploadImage(Uri image, final String email, final String password) throws Exception {
        if(image != null)
        {

                File compressedImage = new Compressor(this)
                        .setQuality(50)
                        .compressToFile(new File(image.getPath()));

            StorageReference storageRef = FirebaseStorage.getInstance().getReference("actual_image");
            storageRef.child(UUID.randomUUID().toString()).putFile(Uri.fromFile(new File(String.valueOf(compressedImage))))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            String urlString = taskSnapshot.getDownloadUrl().toString();
                            uploadInfo(email, password, urlString);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            TV_MESSAGE.setText(e.getMessage());
                            PROGRESS_DIALOG.dismiss();
                            TV_MESSAGE.setVisibility(View.VISIBLE);
                        }
                    });
        }
        else
        {
            Uri uri = Uri.parse("android.resource://com.example.windows.joeychatapp/drawable/defaultimage");
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("actual_image");
            storageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            String urlString = taskSnapshot.getDownloadUrl().toString();
                            uploadInfo(email, password, urlString);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            TV_MESSAGE.setText(e.getMessage());
                            PROGRESS_DIALOG.dismiss();
                            TV_MESSAGE.setVisibility(View.VISIBLE);
                        }
                    });
        }


    }

    private void uploadInfo(String email, String password, final String imageDownloadPath)
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("User").child(userUID);
                            HashMap<String, String> userData = new HashMap<>();
                            userData.put("USER_DISPLAYNAME", ET_DISPLAYNAME.getText().toString().trim());
                            userData.put("USER_AVATAR", imageDownloadPath);
                            dbReference.setValue(userData);
                            PROGRESS_DIALOG.dismiss();
                            Intent sendToNextPage = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(sendToNextPage);
                            finish();
                        }
                        else
                        {
                            TV_MESSAGE.setText("Register not successful");
                            PROGRESS_DIALOG.dismiss();
                            TV_MESSAGE.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        TV_MESSAGE.setText(e.getMessage());
                        PROGRESS_DIALOG.dismiss();
                        TV_MESSAGE.setVisibility(View.VISIBLE);
                    }
                });
    }
}
