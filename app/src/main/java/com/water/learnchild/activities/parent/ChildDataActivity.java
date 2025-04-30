package com.water.learnchild.activities.parent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.water.learnchild.R;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.callback.ImageCallback;
import com.water.learnchild.controller.ChildrenController;
import com.water.learnchild.model.Child;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class ChildDataActivity extends AppCompatActivity {

    ImageView image;
    TextInputEditText name,age,password;
    Button save;
    LinearLayout boy,girl;

    String imageURL = "";
    public static final int PICK_IMAGE = 1;
    String gender;
    Child child;

    LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_data);

        loadingHelper = new LoadingHelper(this);
        image = findViewById(R.id.imgLogo);
        name = findViewById(R.id.et_name);
        age = findViewById(R.id.et_age);
        password = findViewById(R.id.et_password);
        boy = findViewById(R.id.boy_layout);
        girl = findViewById(R.id.girl_layout);
        save = findViewById(R.id.btnSave);

        if(SharedData.currentChild == null){
            child = new Child();
            child.setKey("");
            child.setParent(SharedData.currentParent);
            gender = "";
        }else{
            child = SharedData.currentChild;
            imageURL = child.getImage();
            gender = child.getGender();
            name.setText(child.getName());
            age.setText(child.getAge());
            password.setText(child.getPassword());
            Picasso.get()
                    .load(imageURL)
                    .into(image);
            if(gender.equals("Boy")){
                boy.setBackgroundResource(R.drawable.main_round_btn);
                girl.setBackgroundResource(R.drawable.white_rounded);
            }else{
                girl.setBackgroundResource(R.drawable.main_round_btn);
                boy.setBackgroundResource(R.drawable.white_rounded);
            }
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if(checkReadPermission()){
                        pickImage();
                    }
                }else{
                    pickImage();
                }
            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "Boy";
                boy.setBackgroundResource(R.drawable.main_round_btn);
                girl.setBackgroundResource(R.drawable.white_rounded);
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "Girl";
                girl.setBackgroundResource(R.drawable.main_round_btn);
                boy.setBackgroundResource(R.drawable.white_rounded);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText() == null){
                    name.setError("Required");
                    return;
                }else if(name.getText().toString().isEmpty()){
                    name.setError("Required");
                    return;
                }

                if(age.getText() == null){
                    age.setError("Required");
                    return;
                }else if(age.getText().toString().isEmpty()){
                    age.setError("Required");
                    return;
                }

                if(password.getText() == null){
                    password.setError("Required");
                    return;
                }else if(password.getText().toString().isEmpty()){
                    password.setError("Required");
                    return;
                }

                if(gender.isEmpty()){
                    Toast.makeText(ChildDataActivity.this, "Please, Select your Child Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(imageURL.isEmpty()){
                    Toast.makeText(ChildDataActivity.this, "Please, Select your child image", Toast.LENGTH_SHORT).show();
                    return;
                }

                child.setName(name.getText().toString());
                child.setAge(age.getText().toString());
                child.setGender(gender);
                child.setPassword(password.getText().toString());
                child.setImage(imageURL);

                loadingHelper.showLoading("Saving Data");
                new ChildrenController().Save(child, new ChildCallBack() {
                    @Override
                    public void onSuccess(ArrayList<Child> children) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(ChildDataActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkReadPermission(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_MEDIA_IMAGES);
            if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{ android.Manifest.permission.READ_MEDIA_IMAGES},2);
                return false;
            }else{
                return true;
            }
        }else{
            int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},2);
                return false;
            }else{
                return true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickImage();
            }
        }
    }

    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }

            image.setBackground(null);
            Picasso.get()
                    .load(data.getData())
                    .into(image);


            loadingHelper.showLoading("Uploading Image");
            new ChildrenController().uploadImage(data.getData(), new ImageCallback() {

                @Override
                public void onSuccess(String URL) {
                    imageURL = URL;
                    loadingHelper.dismissLoading();
                }

                @Override
                public void onFail(String msg) {
                    loadingHelper.dismissLoading();
                }


            });
        }
    }
}