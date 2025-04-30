package com.water.learnchild;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.water.learnchild.activities.admin.AdminDashboard;
import com.water.learnchild.activities.child.ChildDashboard;
import com.water.learnchild.activities.parent.ChildDataActivity;
import com.water.learnchild.activities.parent.ParentDashboard;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.callback.ParentCallBack;
import com.water.learnchild.controller.ChildrenController;
import com.water.learnchild.controller.ParentController;
import com.water.learnchild.model.Child;
import com.water.learnchild.model.Parent;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    LoadingHelper loadingHelper;
    TextInputEditText email,password;
    Button login;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingHelper = new LoadingHelper(this);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        if(SharedData.type == 2){
            register.setVisibility(View.VISIBLE);
        }else{
            register.setVisibility(View.GONE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText() == null){
                    email.setError("Required");
                    return;
                }else if(email.getText().toString().equals("")){
                    email.setError("Required");
                    return;
                }

                if(password.getText() == null){
                    password.setError("Required");
                    return;
                }else if(password.getText().toString().equals("")){
                    password.setError("Required");
                    return;
                }

                loadingHelper.showLoading("Validating Data");
                if(SharedData.type == 1){ // Administrator
                    loadingHelper.dismissLoading();
                    if(email.getText().toString().equals("admin@app.com") && password.getText().toString().equals("123456")){
                        Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Email or password", Toast.LENGTH_SHORT).show();
                    }
                }else if(SharedData.type == 2){ //Parent
                    new ParentController()
                            .checkLogin(email.getText().toString(), password.getText().toString(), new ParentCallBack() {
                                @Override
                                public void onSuccess(ArrayList<Parent> parents) {
                                    loadingHelper.dismissLoading();
                                    if(parents.isEmpty()){
                                        Toast.makeText(LoginActivity.this, "invalid email or password", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Parent parent = parents.get(0);
                                        if(parent.getState() == 0){
                                            Toast.makeText(LoginActivity.this, "Waiting For admin approval", Toast.LENGTH_SHORT).show();
                                        }else if(parent.getState() == 1){
                                            SharedData.currentParent = parent;
                                            Intent intent = new Intent(LoginActivity.this, ParentDashboard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else if(parent.getState() == -1){
                                            Toast.makeText(LoginActivity.this, "Sorry,Your account has been rejected", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFail(String msg) {

                                }
                            });
                }else if(SharedData.type == 3){ //Child
                    new ChildrenController()
                            .checkLogin(email.getText().toString(), password.getText().toString(), new ChildCallBack() {
                                @Override
                                public void onSuccess(ArrayList<Child> parents) {
                                    loadingHelper.dismissLoading();
                                    if(parents.isEmpty()){
                                        Toast.makeText(LoginActivity.this, "invalid email or password", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Child parent = parents.get(0);
                                        SharedData.currentChild = parent;
                                        Intent intent = new Intent(LoginActivity.this, ChildDashboard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFail(String msg) {

                                }
                            });
                }
            }
        });
    }
}