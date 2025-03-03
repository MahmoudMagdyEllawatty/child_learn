package com.water.learnchild.activities.child;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.water.learnchild.R;
import com.water.learnchild.callback.ScreenTimeCallBack;
import com.water.learnchild.controller.ScreenTimeController;
import com.water.learnchild.model.ScreenTime;
import com.water.learnchild.utils.SharedData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChildDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);

        getScreenTime();

        TextView title = findViewById(R.id.tvTitle);
        title.setText("Welcome "+SharedData.currentChild.getName());

        // Load button animation
        Animation buttonClick = AnimationUtils.loadAnimation(this, R.anim.button_click);

        // English Learning Button
        ImageButton btnEnglish = findViewById(R.id.btnEnglish);
        btnEnglish.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            startActivity(new Intent(ChildDashboard.this, SelectEnglishLearningTypeActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // Apply transition animation
        });
        CardView btnEnglish2 = findViewById(R.id.en);
        btnEnglish2.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            startActivity(new Intent(ChildDashboard.this, SelectEnglishLearningTypeActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // Apply transition animation
        });

        // Arabic Learning Button
        ImageButton btnArabic = findViewById(R.id.btnArabic);
        btnArabic.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
          //  startActivity(new Intent(MainActivity.this, ArabicActivity.class));
            startActivity(new Intent(ChildDashboard.this,SelectArabicLearningType.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        CardView btnArabic2 = findViewById(R.id.ar);
        btnArabic2.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            //  startActivity(new Intent(MainActivity.this, ArabicActivity.class));
            startActivity(new Intent(ChildDashboard.this,SelectArabicLearningType.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        // Take Pictures Button
        ImageButton btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            startActivity(new Intent(ChildDashboard.this,TakePicture.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        CardView btnCamera2 = findViewById(R.id.camera);
        btnCamera2.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            startActivity(new Intent(ChildDashboard.this,TakePicture.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        // Apply fade-in animation to the entire screen
        View rootView = findViewById(android.R.id.content);
        rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }


    //Check Time
    private void getScreenTime(){
        new ScreenTimeController().getChildScreenTimes(SharedData.currentChild, new ScreenTimeCallBack() {
            @Override
            public void onSuccess(ArrayList<ScreenTime> data) {
                if(!data.isEmpty()){
                    SharedData.screenTimes = data;
                    try {
                        checkIfNowNotAvailable();
                    } catch (ParseException e) {
                        String nowParese = "";
                        Toast.makeText(ChildDashboard.this, "Cannot Parse Times", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
    
    private void checkIfNowNotAvailable() throws ParseException {
        boolean canUse = false  ;
        if(SharedData.screenTimes == null){
            return;
        }else if(SharedData.screenTimes.isEmpty()){
            return;
        }
        long diffInMilles = 0;
        for (ScreenTime screenTime : SharedData.screenTimes){
            Calendar calendar = Calendar.getInstance();
            String currentDay = String.format(Locale.ENGLISH,"%02d", calendar.get(Calendar.DAY_OF_MONTH));
            String currentMonth = String.format(Locale.ENGLISH,"%02d", calendar.get(Calendar.MONTH)+1);
            String currentYear = String.format(Locale.ENGLISH,"%d", calendar.get(Calendar.YEAR));

            Date startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).parse(currentDay+"/"+currentMonth+"/"+currentYear+" "+ screenTime.getFrom_hour());
            Date endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.ENGLISH).parse(currentDay+"/"+currentMonth+"/"+currentYear+" "+ screenTime.getTo_hour());
            Date now = Calendar.getInstance().getTime();
            
            if(now.before(endDate) && now.after(startDate)){
                // Child Can Use Application
                canUse = true;
                long diff = Math.abs(now.getTime() - endDate.getTime());
                if(diffInMilles == 0){
                    diffInMilles = diff;
                }else if(diffInMilles > diff){
                    diffInMilles = diff;
                }
            }
        }


        if(!canUse){
            //We should Start Activity
            Intent intent = new Intent(ChildDashboard.this,LockScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            scheduleAppClose(diffInMilles);
        }
    }

    public void scheduleAppClose(long targetTimeMillis) {
        if (targetTimeMillis > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(ChildDashboard.this,LockScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }, targetTimeMillis);
        }
    }
}