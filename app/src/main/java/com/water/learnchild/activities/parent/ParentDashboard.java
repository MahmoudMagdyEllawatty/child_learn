package com.water.learnchild.activities.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.water.learnchild.R;
import com.water.learnchild.SelectTypeActivity;
import com.water.learnchild.activities.admin.AdminDashboard;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            SharedData.selectedChild = null;
            SharedData.currentChild = null;
            SharedData.currentParent = null;
            SharedData.type = 0;
            Intent intent = new Intent(ParentDashboard.this, SelectTypeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}