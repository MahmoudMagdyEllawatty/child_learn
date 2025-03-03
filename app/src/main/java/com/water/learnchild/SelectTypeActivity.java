package com.water.learnchild;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.water.learnchild.utils.SharedData;

public class SelectTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        SharedData.currentChild = null;
        SharedData.currentParent = null;


        (findViewById(R.id.btnAdmin))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.type = 1;
                        Intent intent = new Intent(SelectTypeActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.btnParent))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.type = 2;
                        Intent intent = new Intent(SelectTypeActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.btnChild))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.type = 3;
                        Intent intent = new Intent(SelectTypeActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }
}