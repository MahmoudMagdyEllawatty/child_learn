package com.water.learnchild;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.water.learnchild.callback.ParentCallBack;
import com.water.learnchild.controller.ParentController;
import com.water.learnchild.model.Parent;
import com.water.learnchild.utils.LoadingHelper;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    LoadingHelper loadingHelper;
    TextInputEditText email,password,name;
    Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingHelper = new LoadingHelper(this);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        name = findViewById(R.id.et_name);
        createAccount = findViewById(R.id.btnCreateAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText() == null){
                    name.setError("Required");
                    return;
                }else if(name.getText().toString().isEmpty()){
                    name.setError("Required");
                    return;
                }

                if(password.getText() == null){
                    password.setError("Required");
                    return;
                }else if(password.getText().toString().isEmpty()){
                    password.setError("Required");
                    return;
                }

                if(email.getText() == null){
                    email.setError("Required");
                    return;
                }else if(email.getText().toString().isEmpty()){
                    email.setError("Required");
                    return;
                }

                loadingHelper.showLoading("Creating Account");
                Parent parent = new Parent("",name.getText().toString(),email.getText().toString(),password.getText().toString(),0);
                new ParentController().Save(parent, new ParentCallBack() {
                    @Override
                    public void onSuccess(ArrayList<Parent> parents) {
                        Toast.makeText(RegisterActivity.this, "Account Created Successfully,Please wait for admin approval", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        });
    }
}