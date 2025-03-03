package com.water.learnchild.activities.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.water.learnchild.R;
import com.water.learnchild.utils.SharedData;

public class ParentDashboard extends AppCompatActivity {

    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        welcomeText = findViewById(R.id.welcome_text);

        (findViewById(R.id.children))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ParentDashboard.this,ChildrenActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.goals))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.which = 1;
                        Intent intent = new Intent(ParentDashboard.this,SelectChildActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.control_screen))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.which = 2;
                        Intent intent = new Intent(ParentDashboard.this,SelectChildActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.reports))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.which = 3;
                        Intent intent = new Intent(ParentDashboard.this,SelectChildActivity.class);
                        startActivity(intent);
                    }
                });


        welcomeText.setText(String.format("Welcome %s, Control Your Children", SharedData.currentParent.getName()));
    }
}